package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegionTest {

    @Test
    void testRegionConstructor() {
        Region region = new Region("ID", "Description", "Welsh Description");

        assertEquals("ID", region.getRegionId());
        assertEquals("Description", region.getDescription());
        assertEquals("Welsh Description", region.getWelshDescription());
    }

    @Test
    void testRegionSetter() {
        Region region = new Region();

        region.setRegionId("ID");
        region.setDescription("Description");
        region.setWelshDescription("Welsh Description");

        assertEquals("ID", region.getRegionId());
        assertEquals("Description", region.getDescription());
        assertEquals("Welsh Description", region.getWelshDescription());
    }

}
