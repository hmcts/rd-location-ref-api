package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RegionTest {

    @Test
    public void testRegionConstructor() {
        Region region = new Region("ID", "Description", "Welsh Description");

        assertThat(region.getRegionId()).isEqualTo("ID");
        assertThat(region.getDescription()).isEqualTo("Description");
        assertThat(region.getWelshDescription()).isEqualTo("Welsh Description");
    }

    @Test
    public void testRegionSetter() {
        Region region = new Region();

        region.setRegionId("ID");
        region.setDescription("Description");
        region.setWelshDescription("Welsh Description");

        assertThat(region.getRegionId()).isEqualTo("ID");
        assertThat(region.getDescription()).isEqualTo("Description");
        assertThat(region.getWelshDescription()).isEqualTo("Welsh Description");
    }

}
