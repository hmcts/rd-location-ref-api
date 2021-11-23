package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildingLocationTest {

    @Test
    void testBuildingLocationBuilder() {

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
                .buildingLocationId(1L)
                .buildingLocationName("buildingLocationName")
                .buildingLocationStatus("LIVE")
                .region(region)
                .address("address")
                .cluster(cluster)
                .courtFinderUrl("courtFinderUrl")
                .area("area")
                .postcode("postcode")
                .created(now)
                .lastUpdated(now)
                .build();


        assertEquals(Long.valueOf(1), buildingLocation.getBuildingLocationId());
        assertEquals("epimmsId", buildingLocation.getEpimmsId());
        assertEquals("buildingLocationName", buildingLocation.getBuildingLocationName());
        assertEquals("LIVE", buildingLocation.getBuildingLocationStatus());
        assertEquals("area", buildingLocation.getArea());
        assertEquals("regionId", buildingLocation.getRegion().get().getRegionId());
        assertEquals("region", buildingLocation.getRegion().get().getDescription());
        assertEquals("clusterId", buildingLocation.getCluster().get().getClusterId());
        assertEquals("cluster name", buildingLocation.getCluster().get().getClusterName());
        assertEquals("courtFinderUrl", buildingLocation.getCourtFinderUrl());
        assertEquals("postcode", buildingLocation.getPostcode());
        assertEquals("address", buildingLocation.getAddress());
        assertEquals(now, buildingLocation.getCreated());
        assertEquals(now, buildingLocation.getLastUpdated());
    }

    @Test
    void testBuildingLocationSetters() {

        Region region = new Region();
        region.setRegionId("regionId");
        region.setDescription("region");

        Cluster cluster = new Cluster();
        cluster.setClusterId("clusterId");
        cluster.setClusterName("cluster name");

        BuildingLocation buildingLocation = new BuildingLocation();

        buildingLocation.setBuildingLocationId(1L);
        buildingLocation.setEpimmsId("epimmsId");
        buildingLocation.setBuildingLocationName("buildingLocationName");
        buildingLocation.setBuildingLocationStatus("LIVE");
        buildingLocation.setArea("area");
        buildingLocation.setRegion(region);
        buildingLocation.setCluster(cluster);
        buildingLocation.setCourtFinderUrl("courtFinderUrl");
        buildingLocation.setPostcode("postcode");
        buildingLocation.setAddress("address");

        LocalDateTime now = LocalDateTime.now();
        buildingLocation.setCreated(now);
        buildingLocation.setLastUpdated(now);

        assertEquals(1L, buildingLocation.getBuildingLocationId());
        assertEquals("epimmsId", buildingLocation.getEpimmsId());
        assertEquals("buildingLocationName", buildingLocation.getBuildingLocationName());
        assertEquals("LIVE", buildingLocation.getBuildingLocationStatus());
        assertEquals("area", buildingLocation.getArea());
        assertEquals("regionId", buildingLocation.getRegion().get().getRegionId());
        assertEquals("region", buildingLocation.getRegion().get().getDescription());
        assertEquals("clusterId", buildingLocation.getCluster().get().getClusterId());
        assertEquals("cluster name", buildingLocation.getCluster().get().getClusterName());
        assertEquals("courtFinderUrl", buildingLocation.getCourtFinderUrl());
        assertEquals("postcode", buildingLocation.getPostcode());
        assertEquals("address", buildingLocation.getAddress());
        assertEquals(now, buildingLocation.getCreated());
        assertEquals(now, buildingLocation.getLastUpdated());
    }
}
