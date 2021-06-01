package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildingLocationTest {

    @Test
    public void testBuildingLocationBuilder() {

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

        BuildingLocation buildingLocation =
            BuildingLocation.builder()
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
                .build();


        assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("buildingLocationId");
        assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId");
        assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName");
        assertThat(buildingLocation.getBuildingLocationStatus().getStatus()).isEqualTo("LIVE");
        assertThat(buildingLocation.getArea()).isEqualTo("area");
        assertThat(buildingLocation.getRegion().getRegionId()).isEqualTo("regionId");
        assertThat(buildingLocation.getRegion().getDescription()).isEqualTo("region");
        assertThat(buildingLocation.getCluster().getClusterId()).isEqualTo("clusterId");
        assertThat(buildingLocation.getCluster().getClusterName()).isEqualTo("cluster name");
        assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl");
        assertThat(buildingLocation.getPostcode()).isEqualTo("postcode");
        assertThat(buildingLocation.getAddress()).isEqualTo("address");
        assertThat(buildingLocation.getCreated()).isEqualTo(now);
        assertThat(buildingLocation.getLastUpdated()).isEqualTo(now);
    }

    @Test
    public void testBuildingLocationSetters() {

        BuildingLocationStatus status = new BuildingLocationStatus();
        status.setStatus("LIVE");
        status.setBuildingStatusId("1");

        Region region = new Region();
        region.setRegionId("regionId");
        region.setDescription("region");

        Cluster cluster = new Cluster();
        cluster.setClusterId("clusterId");
        cluster.setClusterName("cluster name");

        BuildingLocation buildingLocation = new BuildingLocation();

        buildingLocation.setBuildingLocationId("buildingLocationId");
        buildingLocation.setEpimmsId("epimmsId");
        buildingLocation.setBuildingLocationName("buildingLocationName");
        buildingLocation.setBuildingLocationStatus(status);
        buildingLocation.setArea("area");
        buildingLocation.setRegion(region);
        buildingLocation.setCluster(cluster);
        buildingLocation.setCourtFinderUrl("courtFinderUrl");
        buildingLocation.setPostcode("postcode");
        buildingLocation.setAddress("address");

        LocalDateTime now = LocalDateTime.now();
        buildingLocation.setCreated(now);
        buildingLocation.setLastUpdated(now);

        assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("buildingLocationId");
        assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId");
        assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName");
        assertThat(buildingLocation.getBuildingLocationStatus().getStatus()).isEqualTo("LIVE");
        assertThat(buildingLocation.getArea()).isEqualTo("area");
        assertThat(buildingLocation.getRegion().getRegionId()).isEqualTo("regionId");
        assertThat(buildingLocation.getRegion().getDescription()).isEqualTo("region");
        assertThat(buildingLocation.getCluster().getClusterId()).isEqualTo("clusterId");
        assertThat(buildingLocation.getCluster().getClusterName()).isEqualTo("cluster name");
        assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl");
        assertThat(buildingLocation.getPostcode()).isEqualTo("postcode");
        assertThat(buildingLocation.getAddress()).isEqualTo("address");
        assertThat(buildingLocation.getCreated()).isEqualTo(now);
        assertThat(buildingLocation.getLastUpdated()).isEqualTo(now);
    }
}
