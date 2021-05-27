package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildingLocationStatusTest {

    @Test
    public void testBuildingLocationStatusConstructor() {
        BuildingLocationStatus status = new BuildingLocationStatus("1", "LIVE");

        assertThat(status.getBuildingStatusId()).isEqualTo("1");
        assertThat(status.getStatus()).isEqualTo("LIVE");
    }

    @Test
    public void testBuildingLocationStatusSetter() {
        BuildingLocationStatus status = new BuildingLocationStatus();

        status.setBuildingStatusId("1");
        status.setStatus("LIVE");

        assertThat(status.getBuildingStatusId()).isEqualTo("1");
        assertThat(status.getStatus()).isEqualTo("LIVE");
    }
}
