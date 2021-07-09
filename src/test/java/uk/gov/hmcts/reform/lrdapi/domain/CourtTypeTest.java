package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class CourtTypeTest {

    @Test
    public void testCourtType() {

        LocalDateTime now = LocalDateTime.now();

        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("17");
        courtType.setCourtType("courtType");
        courtType.setWelshCourtType("welshCourtType");
        courtType.setCreated(now);
        courtType.setLastUpdated(now);

        assertEquals("17", courtType.getCourtTypeId());
        assertEquals("courtType", courtType.getCourtType());
        assertEquals("welshCourtType", courtType.getWelshCourtType());
        assertEquals(now, courtType.getCreated());
        assertEquals(now, courtType.getLastUpdated());

    }

}
