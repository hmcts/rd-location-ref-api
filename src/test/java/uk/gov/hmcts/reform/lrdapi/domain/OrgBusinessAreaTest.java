package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgBusinessAreaTest {

    @Test
    void testOrgBusinessArea() {

        OrgBusinessArea orgBusinessArea = new OrgBusinessArea(1L, "businessArea");
        assertNotNull(orgBusinessArea);
        assertEquals(1L, orgBusinessArea.getBusinessAreaId());
        assertEquals("businessArea", orgBusinessArea.getDescription());

        OrgBusinessArea orgBusinessAreaOne = new OrgBusinessArea();
        orgBusinessAreaOne.setBusinessAreaId(1L);
        orgBusinessAreaOne.setDescription("businessArea");
        assertNotNull(orgBusinessAreaOne);
        assertEquals(1L, orgBusinessAreaOne.getBusinessAreaId());
        assertEquals("businessArea", orgBusinessAreaOne.getDescription());
    }
}
