package uk.gov.hmcts.reform.lrdapi.controllers.response;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LrdCourtVenuesByServiceCodeResponseTest {

    @Test
    void testCourtVenuesByServiceCodeResponse() {
        CourtType courtType = buildCourtType();
        CourtVenue courtVenue = buildCourtVenue(courtType);

        courtType.setCourtVenues(Collections.singletonList(courtVenue));
        String serviceCode = "ABC1";

        LrdCourtVenuesByServiceCodeResponse courtVenuesByServiceCodeResponse =
            new LrdCourtVenuesByServiceCodeResponse(courtType, serviceCode);

        assertEquals(serviceCode, courtVenuesByServiceCodeResponse.getServiceCode());
        assertEquals(courtType.getCourtTypeId(), courtVenuesByServiceCodeResponse.getCourtTypeId());
        assertEquals(courtType.getTypeOfCourt(), courtVenuesByServiceCodeResponse.getCourtType());
        assertNull(courtVenuesByServiceCodeResponse.getWelshCourtType());
        assertNotNull(courtVenuesByServiceCodeResponse.getCourtVenues());
        assertNotNull(courtVenuesByServiceCodeResponse.getCourtVenues().get(0).getVenueName());
        assertNotNull(courtVenuesByServiceCodeResponse.getCourtVenues().get(0).getIsCaseManagementLocation());
        assertNotNull(courtVenuesByServiceCodeResponse.getCourtVenues().get(0).getIsHearingLocation());
        assertEquals("ABC1", courtVenuesByServiceCodeResponse.getCourtVenues().get(0).getServiceCode());
    }

    @Test
    void testCourtVenuesByServiceCodeResponseConstructorWithAssociatedCourtType() {
        CourtType courtType = buildCourtType();
        List<LrdCourtVenueResponse> courtVenues = List.of(LrdCourtVenueResponse.builder()
                                                             .venueName("venueName")
                                                             .serviceCode("ABC1")
                                                             .build());

        LrdCourtVenuesByServiceCodeResponse response =
            new LrdCourtVenuesByServiceCodeResponse(courtType, courtVenues, "ABC1");

        assertEquals("ABC1", response.getServiceCode());
        assertEquals(courtType.getCourtTypeId(), response.getCourtTypeId());
        assertEquals(courtType.getTypeOfCourt(), response.getCourtType());
        assertNull(response.getWelshCourtType());
        assertEquals(courtVenues, response.getCourtVenues());
    }

    @Test
    void testCourtVenuesByServiceCodeResponseConstructorWithoutAssociatedCourtType() {
        List<LrdCourtVenueResponse> courtVenues = List.of(LrdCourtVenueResponse.builder()
                                                             .venueName("venueName")
                                                             .serviceCode("ABC1")
                                                             .build());

        LrdCourtVenuesByServiceCodeResponse response =
            new LrdCourtVenuesByServiceCodeResponse(null, courtVenues, "ABC1");

        assertEquals("ABC1", response.getServiceCode());
        assertNull(response.getCourtTypeId());
        assertNull(response.getCourtType());
        assertNull(response.getWelshCourtType());
        assertEquals(courtVenues, response.getCourtVenues());
    }

    private CourtType buildCourtType() {
        CourtType courtType = new CourtType();
        courtType.setTypeOfCourt("CourtTypeXYZ");
        courtType.setCourtTypeId("17");
        return courtType;
    }

    private CourtVenue buildCourtVenue(CourtType courtType) {
        Region region = new Region();
        region.setDescription("Region XYZ");
        region.setRegionId("123");

        return CourtVenue.builder()
            .courtType(courtType)
            .siteName("Aberdeen Tribunal Hearing Centre 1")
            .openForPublic(Boolean.TRUE)
            .region(region)
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 1")
            .courtStatus("Open")
            .postcode("AB11 6LT")
            .courtAddress("AB1, 48 HUNTLY STREET, ABERDEEN")
            .courtVenueId(1L)
            .venueName("venueName")
            .isCaseManagementLocation("Y")
            .isHearingLocation("Y")
            .serviceCode("ABC1")
            .build();

    }
}
