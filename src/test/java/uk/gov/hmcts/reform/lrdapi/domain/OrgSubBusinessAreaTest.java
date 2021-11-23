package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgSubBusinessAreaTest {

    @Test
    void testOrgSubBusinessArea() {

        OrgSubBusinessArea orgSubBusinessArea = new OrgSubBusinessArea(1L, "orgSubBusArea");
        assertNotNull(orgSubBusinessArea);
        assertEquals(1L, orgSubBusinessArea.getSubBusinessAreaId());
        assertEquals("orgSubBusArea", orgSubBusinessArea.getDescription());

        OrgSubBusinessArea orgSubBusinessAreaOne = new OrgSubBusinessArea();
        orgSubBusinessAreaOne.setSubBusinessAreaId(1L);
        orgSubBusinessAreaOne.setDescription("orgSubBusArea");
        assertNotNull(orgSubBusinessAreaOne);
        assertEquals(1L, orgSubBusinessAreaOne.getSubBusinessAreaId());
        assertEquals("orgSubBusArea", orgSubBusinessAreaOne.getDescription());
    }
}
