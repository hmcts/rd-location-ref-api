package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LrdBuildingLocationServiceTest {

    @Mock
    private BuildingLocationRepository buildingLocationRepository;

    @InjectMocks
    private LrdBuildingLocationServiceImpl lrdBuildingLocationService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_RetrieveBuildingLocationsByEpimsIDs_OneIdPassed() {

        when(buildingLocationRepository.findByEpimmsId(anyList())).thenReturn(prepareBuildingLocation());

        List<LrdBuildingLocationResponse> buildingLocations =
            (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
                .retrieveBuildingLocationDetails("1", "",  "", "");

        LrdBuildingLocationResponse buildingLocation = buildingLocations.get(0);

        verifySingleResponse(buildingLocation);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_RetrieveBuildingLocationsByEpimsIDs_MultipleIdsPassed() {

        when(buildingLocationRepository.findByEpimmsId(anyList())).thenReturn(prepareMultiBuildLocationResponse());
        List<LrdBuildingLocationResponse> buildingLocations =
            (List<LrdBuildingLocationResponse>) lrdBuildingLocationService
                .retrieveBuildingLocationDetails("1,2", "",  "", "");
        verifyMultiResponse(buildingLocations);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAllBuildingLocations_EpimmsIdALL() {
        when(buildingLocationRepository.findAll())
            .thenReturn(prepareMultiBuildLocationResponse());
        List<LrdBuildingLocationResponse> buildingLocations = (List<LrdBuildingLocationResponse>)
            lrdBuildingLocationService.retrieveBuildingLocationDetails("ALL", "",  "", "");
        verifyMultiResponse(buildingLocations);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAllBuildingLocations() {
        when(buildingLocationRepository.findByBuildingLocationStatusOpen()).thenReturn(prepareBuildingLocation());
        List<LrdBuildingLocationResponse> buildingLocations = (List<LrdBuildingLocationResponse>)
            lrdBuildingLocationService.retrieveBuildingLocationDetails("", "",  "", "");
        verifySingleResponse(buildingLocations.get(0));
    }

    @Test
    public void test_RetrieveBuildingLocationsByEpimmsId_NoBuildLocationFound() {
        when(buildingLocationRepository.findByEpimmsId(anyList())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                     () -> lrdBuildingLocationService.retrieveBuildingLocationDetails("123", "", "", ""));

        verify(buildingLocationRepository, times(1)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    public void test_RetrieveBuildingLocationsByClusterId_NoBuildLocationFound() {
        when(buildingLocationRepository.findByClusterId(anyString())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                     () -> lrdBuildingLocationService.retrieveBuildingLocationDetails("", "", "", "1"));

        verify(buildingLocationRepository, times(1)).findByClusterId("1");
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    public void test_RetrieveBuildingLocationsByClusterId_NonNumericClusterId() {
        when(buildingLocationRepository.findByClusterId(anyString())).thenReturn(null);
        assertThrows(
            InvalidRequestException.class,
            () -> lrdBuildingLocationService
                .retrieveBuildingLocationDetails("", "", "", "1abc"));
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    public void test_RetrieveBuildingLocationsByClusterId_MultipleClusterIdPassed() {
        when(buildingLocationRepository.findByClusterId(anyString())).thenReturn(null);
        assertThrows(
            InvalidRequestException.class,
            () -> lrdBuildingLocationService
                .retrieveBuildingLocationDetails("", "", "", "1,2,3"));
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    public void test_RetrieveBuildingLocationsByRegionId_NoBuildLocationFound() {
        when(buildingLocationRepository.findByRegionId(anyString())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                            () -> lrdBuildingLocationService
                                .retrieveBuildingLocationDetails("", "", "1", ""));
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(1)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    public void test_RetrieveBuildingLocationsByRegionId_NonNumericRegionId() {
        when(buildingLocationRepository.findByRegionId(anyString())).thenReturn(null);
        assertThrows(
            InvalidRequestException.class,
            () -> lrdBuildingLocationService
                .retrieveBuildingLocationDetails("", "", "1abc", ""));
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    public void test_RetrieveBuildingLocationsByRegionId_MultipleRegionIdPassed() {
        when(buildingLocationRepository.findByRegionId(anyString())).thenReturn(null);
        assertThrows(
            InvalidRequestException.class,
            () -> lrdBuildingLocationService
                .retrieveBuildingLocationDetails("", "", "1,2,3", ""));
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findAll();
        verify(buildingLocationRepository, times(0)).findByBuildingLocationStatusOpen();
    }

    @Test
    public void test_GetAllBuildingLocations_NoBuildLocationFound() {
        when(buildingLocationRepository.findByBuildingLocationStatusOpen()).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                     () -> lrdBuildingLocationService.retrieveBuildingLocationDetails("", "", "", ""));

        verify(buildingLocationRepository, times(1)).findByBuildingLocationStatusOpen();
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());

    }

    @Test
    public void test_GetAllOpenBuildingLocations_NoBuildLocationFound() {
        when(buildingLocationRepository.findAll()).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                     () -> lrdBuildingLocationService.retrieveBuildingLocationDetails("ALL", "", "", ""));

        verify(buildingLocationRepository, times(1)).findAll();
        verify(buildingLocationRepository, times(0))
            .findByBuildingLocationNameIgnoreCase(anyString());
        verify(buildingLocationRepository, times(0)).findByEpimmsId(anyList());
        verify(buildingLocationRepository, times(0)).findByClusterId(anyString());
        verify(buildingLocationRepository, times(0)).findByRegionId(anyString());

    }

    @Test
    public void test_RetrieveBuildingLocationsByBuildingLocationName() {

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
    public void shouldThrowResourceNotFoundExceptionForInvalidBuildingLocationName() {
        when(buildingLocationRepository.findByBuildingLocationNameIgnoreCase("test"))
            .thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                     () -> lrdBuildingLocationService.retrieveBuildingLocationDetails("", "test", "", ""));

    }

    private void verifyMultiResponse(List<LrdBuildingLocationResponse> buildingLocations) {
        assertThat(buildingLocations).hasSize(2);

        buildingLocations.forEach(buildingLocation -> {
            if (buildingLocation.getEpimmsId().equalsIgnoreCase("epimmsId")) {
                assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("1");
                assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId");
                assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName");
                assertThat(buildingLocation.getBuildingLocationStatus()).isEqualTo("OPEN");
                assertThat(buildingLocation.getArea()).isEqualTo("area");
                assertThat(buildingLocation.getRegionId()).isEqualTo("regionId");
                assertThat(buildingLocation.getRegion()).isEqualTo("region");
                assertThat(buildingLocation.getClusterId()).isEqualTo("clusterId");
                assertThat(buildingLocation.getClusterName()).isEqualTo("cluster name");
                assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl");
                assertThat(buildingLocation.getPostcode()).isEqualTo("postcode");
                assertThat(buildingLocation.getAddress()).isEqualTo("address");
            } else {
                assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("2");
                assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId222");
                assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName_2");
                assertThat(buildingLocation.getBuildingLocationStatus()).isEqualTo("CLOSED");
                assertThat(buildingLocation.getArea()).isEqualTo("area 2");
                assertThat(buildingLocation.getRegionId()).isEqualTo("regionId");
                assertThat(buildingLocation.getRegion()).isEqualTo("region");
                assertThat(buildingLocation.getClusterId()).isEqualTo("clusterId");
                assertThat(buildingLocation.getClusterName()).isEqualTo("cluster name");
                assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl 2");
                assertThat(buildingLocation.getPostcode()).isEqualTo("postcode 2");
                assertThat(buildingLocation.getAddress()).isEqualTo("address 2");
            }
        });
    }

    private void verifySingleResponse(LrdBuildingLocationResponse buildingLocation) {
        assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("1");
        assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId");
        assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName");
        assertThat(buildingLocation.getBuildingLocationStatus()).isEqualTo("OPEN");
        assertThat(buildingLocation.getArea()).isEqualTo("area");
        assertThat(buildingLocation.getRegionId()).isEqualTo("regionId");
        assertThat(buildingLocation.getRegion()).isEqualTo("region");
        assertThat(buildingLocation.getClusterId()).isEqualTo("clusterId");
        assertThat(buildingLocation.getClusterName()).isEqualTo("cluster name");
        assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl");
        assertThat(buildingLocation.getPostcode()).isEqualTo("postcode");
        assertThat(buildingLocation.getAddress()).isEqualTo("address");
    }

    private List<BuildingLocation> prepareBuildingLocation() {

        var locations = new ArrayList<BuildingLocation>();

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
                         .build());

        return locations;
    }

    private List<BuildingLocation> prepareMultiBuildLocationResponse() {
        var locations = new ArrayList<BuildingLocation>();
        locations.addAll(prepareBuildingLocation());
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
