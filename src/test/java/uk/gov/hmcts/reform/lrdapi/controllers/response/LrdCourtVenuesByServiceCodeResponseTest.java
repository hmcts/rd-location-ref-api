package uk.gov.hmcts.reform.lrdapi.controllers.response;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LrdCourtVenuesByServiceCodeResponseTest {

    @Test
    void testCourtVenuesByServiceCodeResponse() {

        Region region = new Region();
        region.setDescription("Region XYZ");
        region.setRegionId("123");

        BuildingLocation buildingLocation = new BuildingLocation();
        buildingLocation.setEpimmsId("123456789");

        CourtType courtType = new CourtType();
        courtType.setTypeOfCourt("CourtTypeXYZ");
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
            .venueName("venueName")
            .isCaseManagementLocation("Y")
            .isHearingLocation("Y")
            .serviceCode("ABC1")
            .build();

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
}
