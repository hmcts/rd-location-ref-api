package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourtTypeTest {

    @Test
    void testCourtType() {

        LocalDateTime now = LocalDateTime.now();

        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("17");
        courtType.setTypeOfCourt("courtType");
        courtType.setWelshCourtType("welshCourtType");
        courtType.setCreated(now);
        courtType.setLastUpdated(now);

        assertEquals("17", courtType.getCourtTypeId());
        assertEquals("courtType", courtType.getTypeOfCourt());
        assertEquals("welshCourtType", courtType.getWelshCourtType());
        assertEquals(now, courtType.getCreated());
        assertEquals(now, courtType.getLastUpdated());

    }

}
