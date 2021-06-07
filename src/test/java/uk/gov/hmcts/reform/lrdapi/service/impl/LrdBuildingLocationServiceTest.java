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
    public void test_RetrieveBuildingLocationsByEpimmsID() {

        when(buildingLocationRepository.findByEpimmsId(anyString())).thenReturn(prepareBuildingLocation());
        List<LrdBuildingLocationResponse> buildingLocations =
            lrdBuildingLocationService.retrieveBuildingLocationByEpimsId("1");

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

    @Test(expected = ResourceNotFoundException.class)
    public void test_RetrieveBuildingLocations_NoBuildLocationFound() {
        when(buildingLocationRepository.findByEpimmsId(anyString())).thenReturn(null);
        lrdBuildingLocationService.retrieveBuildingLocationByEpimsId("1");
        verify(buildingLocationRepository.findByEpimmsId(anyString()), times(1));
    }

    private List<BuildingLocation> prepareBuildingLocation() {

        BuildingLocationStatus status = new BuildingLocationStatus();
        status.setStatus("LIVE");
        status.setBuildingStatusId("1");

        Region region = new Region();
        region.setRegionId("regionId");
        region.setDescription("region");

        Cluster cluster = new Cluster();
        cluster.setClusterId("clusterId");
        cluster.setClusterName("cluster name");

        LocalDateTime now = LocalDateTime.now();

        List<BuildingLocation> locations = new ArrayList<>();

        locations.add(BuildingLocation.builder()
                         .epimmsId("epimmsId")
                         .buildingLocationId("buildingLocationId")
                         .buildingLocationName("buildingLocationName")
                         .buildingLocationStatus(status)
                         .region(region)
                         .address("address")
                         .cluster(cluster)
                         .courtFinderUrl("courtFinderUrl")
                         .area("area")
                         .postcode("postcode")
                         .created(now)
                         .lastUpdated(now)
                         .build());

        return locations;
    }

}
