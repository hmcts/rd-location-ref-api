package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildingLocationTest {

    @Test
    public void testBuildingLocationConstructor() {
        BuildingLocation buildingLocation =
            new BuildingLocation("buildingLocationId", "epimmsId",
                                 "buildingLocationName",
                                 "buildingLocationStatusId", "area", "regionId",
                                 "clusterId", "courtFinderUrl",
                                 "postcode", "address"
            );

        assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("buildingLocationId");
        assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId");
        assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName");
        assertThat(buildingLocation.getBuildingLocationStatusId()).isEqualTo("buildingLocationStatusId");
        assertThat(buildingLocation.getArea()).isEqualTo("area");
        assertThat(buildingLocation.getRegionId()).isEqualTo("regionId");
        assertThat(buildingLocation.getClusterId()).isEqualTo("clusterId");
        assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl");
        assertThat(buildingLocation.getPostcode()).isEqualTo("postcode");
        assertThat(buildingLocation.getAddress()).isEqualTo("address");
    }

    @Test
    public void testBuildingLocationSetters() {
        BuildingLocation buildingLocation = new BuildingLocation();

        buildingLocation.setBuildingLocationId("buildingLocationId");
        buildingLocation.setEpimmsId("epimmsId");
        buildingLocation.setBuildingLocationName("buildingLocationName");
        buildingLocation.setBuildingLocationStatusId("buildingLocationStatusId");
        buildingLocation.setArea("area");
        buildingLocation.setRegionId("regionId");
        buildingLocation.setClusterId("clusterId");
        buildingLocation.setCourtFinderUrl("courtFinderUrl");
        buildingLocation.setPostcode("postcode");
        buildingLocation.setAddress("address");

        assertThat(buildingLocation.getBuildingLocationId()).isEqualTo("buildingLocationId");
        assertThat(buildingLocation.getEpimmsId()).isEqualTo("epimmsId");
        assertThat(buildingLocation.getBuildingLocationName()).isEqualTo("buildingLocationName");
        assertThat(buildingLocation.getBuildingLocationStatusId()).isEqualTo("buildingLocationStatusId");
        assertThat(buildingLocation.getArea()).isEqualTo("area");
        assertThat(buildingLocation.getRegionId()).isEqualTo("regionId");
        assertThat(buildingLocation.getClusterId()).isEqualTo("clusterId");
        assertThat(buildingLocation.getCourtFinderUrl()).isEqualTo("courtFinderUrl");
        assertThat(buildingLocation.getPostcode()).isEqualTo("postcode");
        assertThat(buildingLocation.getAddress()).isEqualTo("address");
    }
}
