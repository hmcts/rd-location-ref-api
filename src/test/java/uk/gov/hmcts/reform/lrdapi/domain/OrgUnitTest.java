package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgUnitTest {

    @Test
    void testOrgUnit() {

        OrgUnit orgUnit = new OrgUnit(1L,"OrgUnit");
        assertNotNull(orgUnit);
        assertEquals(1L, orgUnit.getOrgUnitId());
        assertEquals("OrgUnit", orgUnit.getDescription());

        OrgUnit orgUnitOne = new OrgUnit();
        orgUnitOne.setOrgUnitId(1L);
        orgUnitOne.setDescription("OrgUnit");
        assertNotNull(orgUnitOne);
        assertEquals(1L, orgUnitOne.getOrgUnitId());
        assertEquals("OrgUnit", orgUnitOne.getDescription());
    }
}
