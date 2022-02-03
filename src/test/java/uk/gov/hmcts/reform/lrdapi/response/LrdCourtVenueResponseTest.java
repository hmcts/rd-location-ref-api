package uk.gov.hmcts.reform.lrdapi.response;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LrdCourtVenueResponseTest {

    @Test
    void testCourtVenueResponse() {

        BuildingLocation buildingLocation = new BuildingLocation();
        buildingLocation.setEpimmsId("123456789");

        Region region = new Region();
        region.setDescription("Region XYZ");
        region.setRegionId("123");

        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("17");
        courtType.setTypeOfCourt("courtType");

        Cluster cluster = new Cluster();
        cluster.setClusterId("456");
        cluster.setClusterName("ClusterXYZ");

        LocalDateTime now = LocalDateTime.now();

        CourtVenue courtVenue = new CourtVenue();

        courtVenue.setCourtVenueId(1L);
        courtVenue.setSiteName("siteName");
        courtVenue.setRegion(region);
        courtVenue.setCourtType(courtType);
        courtVenue.setCluster(cluster);
        courtVenue.setOpenForPublic(Boolean.TRUE);
        courtVenue.setCourtAddress("courtAddress");
        courtVenue.setPostcode("AB EYZ");
        courtVenue.setPhoneNumber("122324234");
        courtVenue.setClosedDate(now);
        courtVenue.setCourtLocationCode("courtLocationCode");
        courtVenue.setDxAddress("dxAddress");
        courtVenue.setWelshSiteName("welshSiteName");
        courtVenue.setWelshCourtAddress("welshCourtAddress");
        courtVenue.setCourtStatus("Open");
        courtVenue.setCourtOpenDate(now);
        courtVenue.setCourtName("courtName");
        courtVenue.setCreatedTime(now);
        courtVenue.setUpdatedTime(now);
        courtVenue.setVenueName("venueName");
        courtVenue.setIsCaseManagementLocation("Y");
        courtVenue.setIsHearingLocation("Y");
        courtVenue.setWelshVenueName("");
        courtVenue.setIsTemporaryLocation("N");
        courtVenue.setIsNightingaleCourt("N");
        courtVenue.setLocationType("Court");
        courtVenue.setParentLocation("");

        LrdCourtVenueResponse courtVenueResponse = new LrdCourtVenueResponse(courtVenue);

        assertEquals("",courtVenueResponse.getWelshVenueName());
        assertEquals("N",courtVenueResponse.getIsTemporaryLocation());
        assertEquals("N",courtVenueResponse.getIsNightingaleCourt());
        assertEquals("Court",courtVenueResponse.getLocationType());
        assertEquals("",courtVenueResponse.getParentLocation());


    }
}
