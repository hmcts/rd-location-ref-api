package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class CourtTypeServiceAssocTest {

    @Test
    public void testCourtTypeServiceAssoc() {

        Service service = new Service();
        service.setServiceCode("serviceCode");

        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("17");

        LocalDateTime now = LocalDateTime.now();

        CourtTypeServiceAssoc courtTypeServiceAssoc = new CourtTypeServiceAssoc();
        courtTypeServiceAssoc.setCourtTypeServiceAssocId("courtTypeServiceAssocId");
        courtTypeServiceAssoc.setService(service);
        courtTypeServiceAssoc.setCourtType(courtType);
        courtTypeServiceAssoc.setCreated(now);
        courtTypeServiceAssoc.setLastUpdated(now);

        assertEquals("courtTypeServiceAssocId", courtTypeServiceAssoc.getCourtTypeServiceAssocId());
        assertEquals("serviceCode", courtTypeServiceAssoc.getService().getServiceCode());
        assertEquals("17", courtTypeServiceAssoc.getCourtType().getCourtTypeId());
        assertEquals(now, courtTypeServiceAssoc.getCreated());
        assertEquals(now, courtTypeServiceAssoc.getLastUpdated());

    }

}
