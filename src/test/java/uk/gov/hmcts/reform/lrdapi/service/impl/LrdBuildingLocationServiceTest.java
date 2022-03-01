package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LrdBuildingLocationServiceTest {

    @Mock
    private BuildingLocationRepository buildingLocationRepository;

    @InjectMocks
    private LrdBuildingLocationServiceImpl lrdBuildingLocationService;

    @Test
    @SuppressWarnings("unchecked")
    void test_RetrieveBuildingLocationsByEpimsIDs_OneIdPassed() {

        when(buildingLocationRepository.findByEpimmsId(anyList())).thenReturn(prepareBuildingLocation());

        var buildingLocations = (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
            .retrieveBuildingLocationDetails("1", "",  "", "");

        LrdBuildingLocationResponse buildingLocation = buildingLocations.get(0);

        verifySingleResponse(buildingLocation);
    }

    @Test
    @SuppressWarnings("unchecked")
    void test_RetrieveBuildingLocationsByEpimsIDs_MultipleIdsPassed() {

        when(buildingLocationRepository.findByEpimmsId(anyList())).thenReturn(prepareMultiBuildLocationResponse());
        var buildingLocations = (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
            .retrieveBuildingLocationDetails("1,2", "",  "", "");
        verifyMultiResponse(buildingLocations);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetAllBuildingLocations_EpimmsIdAll() {
        when(buildingLocationRepository.findAll())
            .thenReturn(prepareMultiBuildLocationResponse());
        var buildingLocations = (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
            .retrieveBuildingLocationDetails("ALL", "",  "", "");
        verifyMultiResponse(buildingLocations);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetAllBuildingLocations_EpimmsIdAllMultipleIds() {
        when(buildingLocationRepository.findAll())
            .thenReturn(prepareMultiBuildLocationResponse());
        var buildingLocations = (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
            .retrieveBuildingLocationDetails("ALL,1,2", "",  "", "");
        verifyMultiResponse(buildingLocations);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetAllBuildingLocations() {
        when(buildingLocationRepository.findByBuildingLocationStatusOpen()).thenReturn(prepareBuildingLocation());
        var buildingLocations = (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "",  "", "");
        verifySingleResponse(buildingLocations.get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetBuildingLocationsByRegionId() {
        when(buildingLocationRepository.findByRegionId(anyString())).thenReturn(prepareBuildingLocation());
        var buildingLocations = (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "", "1", "");
        verifySingleResponse(buildingLocations.get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetBuildingLocationsByInvalidRegionId() {
        assertThrows(InvalidRequestException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "", "abc", ""));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetBuildingLocationsByInvalidClusterIdParam() {
        assertThrows(InvalidRequestException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "", "", "1,2"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetBuildingLocationsByClusterId() {
        when(buildingLocationRepository.findByClusterId(anyString())).thenReturn(prepareBuildingLocation());
        var buildingLocations = (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "", "", "1");
        verifySingleResponse(buildingLocations.get(0));
    }

    @Test
    void test_RetrieveBuildingLocationsByEpimmsId_InvalidEpimmsId() {
        assertThrows(InvalidRequestException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("{123}", "", "", ""));

        verify(buildingLocationRepository, times(0)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    void test_RetrieveBuildingLocationsByEpimmsId_NoBuildLocationFound() {
        when(buildingLocationRepository.findByEpimmsId(anyList())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("123", "", "", ""));

        verify(buildingLocationRepository, times(1)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    void test_RetrieveBuildingLocationsByClusterId_NoBuildLocationFound() {
        when(buildingLocationRepository.findByClusterId(anyString())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,() -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "", "", "1"));

        verify(buildingLocationRepository, times(1)).findByClusterId("1");
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1abc","1,2,3"})
    void test_RetrieveBuildingLocationsByClusterId_InvalidRequest(String input) {
        assertThrows(
            InvalidRequestException.class,() -> lrdBuildingLocationService
                .retrieveBuildingLocationDetails("", "", "", input));
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","123"})
    void test_RetrieveBuildingLocationsByRegionId_NoBuildLocationFound(String input) {
        when(buildingLocationRepository.findByRegionId(anyString())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "", input, ""));
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(1)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1abc","1,2,3"})
    void test_RetrieveBuildingLocationsByRegionId_InvalidRequest() {
        assertThrows(InvalidRequestException.class,() -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "", "1abc", ""));
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @ParameterizedTest
    @EmptySource
    void test_GetAllBuildingLocations_NoBuildLocationFound(String input) {
        when(buildingLocationRepository.findByBuildingLocationStatusOpen()).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails(input,input,input,input));

        verify(buildingLocationRepository, times(1)).findByBuildingLocationStatusOpen();
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());

    }

    @Test
    void test_GetAllOpenBuildingLocations_NoBuildLocationFound() {
        when(buildingLocationRepository.findAll()).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("ALL", "", "", ""));

        verify(buildingLocationRepository, times(1)).findAll();
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());

    }

    @Test
    void test_RetrieveBuildingLocationsByBuildingLocationName() {

        when(buildingLocationRepository.findByBuildingLocationNameIgnoreCase("test"))
            .thenReturn(prepareBuildingLocation().get(0));

        LrdBuildingLocationResponse buildingLocation =
            (LrdBuildingLocationResponse) lrdBuildingLocationService
                .retrieveBuildingLocationDetails("", "test",  "", "");

        verifySingleResponse(buildingLocation);
        verify(buildingLocationRepository, times(1))
            .findByBuildingLocationNameIgnoreCase("test");
        verify(buildingLocationRepository, times(0)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());

    }

    @Test
    void shouldThrowResourceNotFoundExceptionForInvalidBuildingLocationName() {
        when(buildingLocationRepository.findByBuildingLocationNameIgnoreCase("test"))
            .thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "test", "", ""));
    }

    @Test
    void shouldThrowInvalidRequestExceptionForMultipleBuildingLocationNames() {

        assertThrows(InvalidRequestException.class, () -> lrdBuildingLocationService
            .retrieveBuildingLocationDetails("", "test, test 2", "", ""));

        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase("test, test 2");
        verify(buildingLocationRepository, times(0)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
    }

    private void verifyMultiResponse(List<LrdBuildingLocationResponse> buildingLocations) {
        assertThat(buildingLocations).hasSize(2);

        buildingLocations.forEach(buildingLocation -> {
            if (buildingLocation.getEpimmsId().equalsIgnoreCase("epimmsId")) {
                assertEquals("1", buildingLocation.getBuildingLocationId());
                assertEquals("epimmsId", buildingLocation.getEpimmsId());
                assertEquals("buildingLocationName", buildingLocation.getBuildingLocationName());
                assertEquals("OPEN", buildingLocation.getBuildingLocationStatus());
                assertEquals("area", buildingLocation.getArea());
                assertEquals("regionId", buildingLocation.getRegionId());
                assertEquals("region", buildingLocation.getRegion());
                assertEquals("clusterId", buildingLocation.getClusterId());
                assertEquals("cluster name", buildingLocation.getClusterName());
                assertEquals("courtFinderUrl", buildingLocation.getCourtFinderUrl());
                assertEquals("postcode", buildingLocation.getPostcode());
                assertEquals("address", buildingLocation.getAddress());
            } else {
                assertEquals("2", buildingLocation.getBuildingLocationId());
                assertEquals("epimmsId222", buildingLocation.getEpimmsId());
                assertEquals("buildingLocationName_2", buildingLocation.getBuildingLocationName());
                assertEquals("CLOSED", buildingLocation.getBuildingLocationStatus());
                assertEquals("area 2", buildingLocation.getArea());
                assertEquals("regionId", buildingLocation.getRegionId());
                assertEquals("region", buildingLocation.getRegion());
                assertEquals("clusterId", buildingLocation.getClusterId());
                assertEquals("cluster name", buildingLocation.getClusterName());
                assertEquals("courtFinderUrl 2", buildingLocation.getCourtFinderUrl());
                assertEquals("postcode 2", buildingLocation.getPostcode());
                assertEquals("address 2", buildingLocation.getAddress());
            }
        });
    }

    private void verifySingleResponse(LrdBuildingLocationResponse buildingLocation) {
        assertEquals("1", buildingLocation.getBuildingLocationId());
        assertEquals("epimmsId", buildingLocation.getEpimmsId());
        assertEquals("buildingLocationName", buildingLocation.getBuildingLocationName());
        assertEquals("OPEN", buildingLocation.getBuildingLocationStatus());
        assertEquals("area", buildingLocation.getArea());
        assertEquals("regionId", buildingLocation.getRegionId());
        assertEquals("region", buildingLocation.getRegion());
        assertEquals("clusterId", buildingLocation.getClusterId());
        assertEquals("cluster name", buildingLocation.getClusterName());
        assertEquals("courtFinderUrl", buildingLocation.getCourtFinderUrl());
        assertEquals("postcode", buildingLocation.getPostcode());
        assertEquals("address", buildingLocation.getAddress());
        assertFalse(buildingLocation.getCourtVenues().isEmpty());
    }

    private List<BuildingLocation> prepareBuildingLocation() {

        var locations = new ArrayList<BuildingLocation>();

        CourtType courtType = CourtType.builder()
            .typeOfCourt("courtType")
            .courtTypeId("10")
            .build();

        CourtVenue courtVenue = CourtVenue.builder()
            .courtVenueId(1L)
            .courtType(courtType)
            .openForPublic(Boolean.TRUE)
            .build();

        locations.add(BuildingLocation.builder()
                          .epimmsId("epimmsId")
                          .buildingLocationId(1L)
                          .buildingLocationName("buildingLocationName")
                          .buildingLocationStatus("OPEN")
                          .region(getRegion())
                          .address("address")
                          .cluster(getCluster())
                          .courtFinderUrl("courtFinderUrl")
                          .area("area")
                          .postcode("postcode")
                          .created(getCurrentTime())
                          .lastUpdated(getCurrentTime())
                          .courtVenues(Set.of(courtVenue))
                          .build());

        return locations;
    }

    private List<BuildingLocation> prepareMultiBuildLocationResponse() {
        var locations = new ArrayList<BuildingLocation>(prepareBuildingLocation());
        locations.add(BuildingLocation.builder()
                          .epimmsId("epimmsId222")
                          .buildingLocationId(2L)
                          .buildingLocationName("buildingLocationName_2")
                          .buildingLocationStatus("CLOSED")
                          .region(getRegion())
                          .address("address 2")
                          .cluster(getCluster())
                          .courtFinderUrl("courtFinderUrl 2")
                          .area("area 2")
                          .postcode("postcode 2")
                          .created(getCurrentTime())
                          .lastUpdated(getCurrentTime())
                          .build());

        return locations;
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    private Cluster getCluster() {
        Cluster cluster = new Cluster();
        cluster.setClusterId("clusterId");
        cluster.setClusterName("cluster name");
        return cluster;
    }

    private Region getRegion() {
        Region region = new Region();
        region.setRegionId("regionId");
        region.setDescription("region");
        return region;
    }
}

