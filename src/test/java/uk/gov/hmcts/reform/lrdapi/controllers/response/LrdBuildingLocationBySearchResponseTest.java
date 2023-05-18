package uk.gov.hmcts.reform.lrdapi.controllers.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LrdBuildingLocationBySearchResponseTest {

    @Test
    void testBuildingLocationResponseBuilder() {
        LrdBuildingLocationBySearchResponse lrdBuildingLocationBySearchResponse =
            LrdBuildingLocationBySearchResponse.builder()
                .epimmsId("epimmsId")
                .buildingLocationName("buildingLocationName")
                .buildingLocationStatus("LIVE")
                .region("region")
                .regionId("regionId")
                .address("address")
                .clusterId("clusterId")
                .clusterName("clisterName")
                .courtFinderUrl("courtFinderUrl")
                .area("area")
                .postcode("postcode")
                .build();


        assertEquals("epimmsId", lrdBuildingLocationBySearchResponse.getEpimmsId());
        assertEquals("buildingLocationName", lrdBuildingLocationBySearchResponse.getBuildingLocationName());
        assertEquals("LIVE", lrdBuildingLocationBySearchResponse.getBuildingLocationStatus());
        assertEquals("area", lrdBuildingLocationBySearchResponse.getArea());
        assertEquals("regionId", lrdBuildingLocationBySearchResponse.getRegionId());
        assertEquals("region", lrdBuildingLocationBySearchResponse.getRegion());
        assertEquals("clusterId", lrdBuildingLocationBySearchResponse.getClusterId());
        assertEquals("clisterName", lrdBuildingLocationBySearchResponse.getClusterName());
        assertEquals("courtFinderUrl", lrdBuildingLocationBySearchResponse.getCourtFinderUrl());
        assertEquals("postcode", lrdBuildingLocationBySearchResponse.getPostcode());
        assertEquals("address", lrdBuildingLocationBySearchResponse.getAddress());

    }
}

