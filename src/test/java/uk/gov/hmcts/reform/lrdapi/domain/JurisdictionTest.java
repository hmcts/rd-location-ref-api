package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JurisdictionTest {

    @Test
    void testCreateJurisdiction() {

        Jurisdiction jurisdiction = new Jurisdiction(1L, "jurisdiction");

        assertNotNull(jurisdiction);
        assertEquals("jurisdiction", jurisdiction.getDescription());
        assertEquals(1L, jurisdiction.getJurisdictionId());

        Jurisdiction jurisdictionOne = new Jurisdiction();
        jurisdictionOne.setDescription("jurisdiction");
        jurisdictionOne.setJurisdictionId(1L);
        assertNotNull(jurisdictionOne);
        assertEquals("jurisdiction", jurisdictionOne.getDescription());
        assertEquals(1L, jurisdictionOne.getJurisdictionId());
    }
}
