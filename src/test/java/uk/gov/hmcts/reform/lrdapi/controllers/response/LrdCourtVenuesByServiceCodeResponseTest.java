package uk.gov.hmcts.reform.lrdapi.controllers.response;

import org.junit.Test;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LrdCourtVenuesByServiceCodeResponseTest {

    @Test
    public void testCourtVenuesByServiceCodeResponse() {

        Region region = new Region();
        region.setDescription("Region XYZ");
        region.setRegionId("123");

        BuildingLocation buildingLocation = new BuildingLocation();
        buildingLocation.setEpimmsId("123456789");

        CourtType courtType = new CourtType();
        courtType.setCourtType("CourtTypeXYZ");
        courtType.setCourtTypeId("17");

        CourtVenue courtVenue = CourtVenue.builder()
            .courtType(courtType)
            .siteName("Aberdeen Tribunal Hearing Centre 1")
            .openForPublic(Boolean.TRUE)
            .region(region)
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 1")
            .courtStatus("Open")
            .postcode("AB11 6LT")
            .courtAddress("AB1, 48 HUNTLY STREET, ABERDEEN")
            .courtVenueId(1L)
            .build();

        courtType.setCourtVenues(Collections.singletonList(courtVenue));
        String serviceCode = "ABC1";

        LrdCourtVenuesByServiceCodeResponse courtVenuesByServiceCodeResponse =
            new LrdCourtVenuesByServiceCodeResponse(courtType, serviceCode);

        assertEquals(serviceCode, courtVenuesByServiceCodeResponse.getServiceCode());
        assertEquals(courtType.getCourtTypeId(), courtVenuesByServiceCodeResponse.getCourtTypeId());
        assertEquals(courtType.getCourtType(), courtVenuesByServiceCodeResponse.getCourtType());
        assertNull(courtVenuesByServiceCodeResponse.getWelshCourtType());
        assertNotNull(courtVenuesByServiceCodeResponse.getCourtVenues());

    }
}
