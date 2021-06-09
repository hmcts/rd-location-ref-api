package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocationStatus;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
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
    public void test_RetrieveBuildingLocationsByEpimsIDs_OneIdPassed() {

        when(buildingLocationRepository.findByEpimmsIdIn(anyList())).thenReturn(prepareBuildingLocation());

        List<LrdBuildingLocationResponse> buildingLocations =
            lrdBuildingLocationService.retrieveBuildingLocationByEpimsIds(getListContainingOneEpimId());

        LrdBuildingLocationResponse buildingLocation = buildingLocations.get(0);

        assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("buildingLocationId");
        assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId");
        assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName");
        assertThat(buildingLocation.getBuildingLocationStatus()).isEqualTo("LIVE");
        assertThat(buildingLocation.getArea()).isEqualTo("area");
        assertThat(buildingLocation.getRegionId()).isEqualTo("regionId");
        assertThat(buildingLocation.getRegion()).isEqualTo("region");
        assertThat(buildingLocation.getClusterId()).isEqualTo("clusterId");
        assertThat(buildingLocation.getClusterName()).isEqualTo("cluster name");
        assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl");
        assertThat(buildingLocation.getPostcode()).isEqualTo("postcode");
        assertThat(buildingLocation.getAddress()).isEqualTo("address");
    }

    @Test
    public void test_RetrieveBuildingLocationsByEpimsIDs_MultipleIdsPassed() {

        when(buildingLocationRepository.findByEpimmsIdIn(anyList())).thenReturn(prepareMultiBuildLocationResponse());
        List<LrdBuildingLocationResponse> buildingLocations =
            lrdBuildingLocationService.retrieveBuildingLocationByEpimsIds(getListContainingMultipleEpimIds());
        verifyMultiResponse(buildingLocations);
    }

    @Test
    public void testGetAllBuildingLocations() {
        when(buildingLocationRepository.findAll()).thenReturn(prepareMultiBuildLocationResponse());
        List<LrdBuildingLocationResponse> buildingLocations = lrdBuildingLocationService.getAllBuildingLocations();
        verifyMultiResponse(buildingLocations);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_RetrieveBuildingLocations_NoBuildLocationFound() {
        when(buildingLocationRepository.findByEpimmsIdIn(anyList())).thenReturn(null);
        lrdBuildingLocationService.retrieveBuildingLocationByEpimsIds(getListContainingOneEpimId());
        verify(buildingLocationRepository.findByEpimmsIdIn(anyList()), times(1));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_GetAllBuildingLocations_NoBuildLocationFound() {
        when(buildingLocationRepository.findAll()).thenReturn(null);
        lrdBuildingLocationService.getAllBuildingLocations();
        verify(buildingLocationRepository.findAll(), times(1));
    }

    private void verifyMultiResponse(List<LrdBuildingLocationResponse> buildingLocations) {
        assertThat(buildingLocations.size()).isEqualTo(2);

        buildingLocations.forEach(buildingLocation -> {
            if (buildingLocation.getEpimmsId().equalsIgnoreCase("epimmsId")) {
                assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("buildingLocationId");
                assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId");
                assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName");
                assertThat(buildingLocation.getBuildingLocationStatus()).isEqualTo("LIVE");
                assertThat(buildingLocation.getArea()).isEqualTo("area");
                assertThat(buildingLocation.getRegionId()).isEqualTo("regionId");
                assertThat(buildingLocation.getRegion()).isEqualTo("region");
                assertThat(buildingLocation.getClusterId()).isEqualTo("clusterId");
                assertThat(buildingLocation.getClusterName()).isEqualTo("cluster name");
                assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl");
                assertThat(buildingLocation.getPostcode()).isEqualTo("postcode");
                assertThat(buildingLocation.getAddress()).isEqualTo("address");
            } else {
                assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("buildingLocationId_2");
                assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId222");
                assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName_2");
                assertThat(buildingLocation.getBuildingLocationStatus()).isEqualTo("LIVE");
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

    private List<String> getListContainingOneEpimId() {
        List<String> epimIdList = new ArrayList<>();
        epimIdList.add("1");
        return epimIdList;
    }

    private List<String> getListContainingMultipleEpimIds() {
        List<String> epimIdList = new ArrayList<>();
        epimIdList.addAll(getListContainingOneEpimId());
        epimIdList.add("2");
        return epimIdList;
    }

    private List<BuildingLocation> prepareBuildingLocation() {

        List<BuildingLocation> locations = new ArrayList<>();

        locations.add(BuildingLocation.builder()
                         .epimmsId("epimmsId")
                         .buildingLocationId("buildingLocationId")
                         .buildingLocationName("buildingLocationName")
                         .buildingLocationStatus(getBuildingLocationStatus())
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
        List<BuildingLocation> locations = new ArrayList<>();
        locations.addAll(prepareBuildingLocation());
        locations.add(BuildingLocation.builder()
                          .epimmsId("epimmsId222")
                          .buildingLocationId("buildingLocationId_2")
                          .buildingLocationName("buildingLocationName_2")
                          .buildingLocationStatus(getBuildingLocationStatus())
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

    private BuildingLocationStatus getBuildingLocationStatus() {
        BuildingLocationStatus status = new BuildingLocationStatus();
        status.setStatus("LIVE");
        status.setBuildingStatusId("1");
        return status;
    }



}
