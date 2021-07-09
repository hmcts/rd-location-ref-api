package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

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
