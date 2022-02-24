package uk.gov.hmcts.reform.lrdapi.controllers.response;


import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LrdCourtVenueResponseTest {

    @Test
    void testCourtVenueTransformationToResponse_ClusterAndDatesAbsent() {

        Region region = new Region();
        region.setCreatedTime(LocalDateTime.now());
        region.setDescription("Region XYZ");
        region.setRegionId("123");
        region.setUpdatedTime(LocalDateTime.now());
        region.setWelshDescription("Region ABC");

        CourtType courtType = new CourtType();
        courtType.setTypeOfCourt("CourtTypeXYZ");
        courtType.setCourtTypeId("17");

        BuildingLocation buildingLocation = new BuildingLocation();
        buildingLocation.setEpimmsId("123456789");

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
            .welshVenueName("testVenue")
            .isTemporaryLocation("N")
            .isNightingaleCourt("N")
            .locationType("Court")
            .parentLocation("366559")
            .build();

        LrdCourtVenueResponse courtVenueResponse = new LrdCourtVenueResponse(courtVenue);

        assertEquals("YES", courtVenueResponse.getOpenForPublic());
        assertNull(courtVenueResponse.getClusterId());
        assertNull(courtVenueResponse.getClusterName());
        assertEquals(region.getRegionId(), courtVenueResponse.getRegionId());
        assertEquals(region.getDescription(), courtVenueResponse.getRegion());
        assertNull(courtVenueResponse.getClosedDate());
        assertNull(courtVenueResponse.getCourtOpenDate());
        assertNotNull(courtVenueResponse.getVenueName());
        assertNotNull(courtVenueResponse.getIsCaseManagementLocation());
        assertNotNull(courtVenueResponse.getIsHearingLocation());
        assertNotNull(courtVenueResponse.getWelshVenueName());
        assertNotNull(courtVenueResponse.getIsTemporaryLocation());
        assertNotNull(courtVenueResponse.getIsNightingaleCourt());
        assertNotNull(courtVenueResponse.getLocationType());
        assertNotNull(courtVenueResponse.getParentLocation());
    }

    @Test
    void testCourtVenueTransformationToResponse_ClusterAndDatesPresent() {

        Cluster cluster = new Cluster();
        cluster.setClusterId("456");
        cluster.setClusterName("ClusterXYZ");
        cluster.setWelshClusterName("ClusterABC");
        cluster.setCreatedTime(LocalDateTime.now());
        cluster.setUpdatedTime(LocalDateTime.now());

        CourtType courtType = new CourtType();
        courtType.setTypeOfCourt("CourtTypeXYZ");
        courtType.setCourtTypeId("17");

        BuildingLocation buildingLocation = new BuildingLocation();
        buildingLocation.setEpimmsId("123456789");

        LocalDateTime now = LocalDateTime.now();

        CourtVenue courtVenue = CourtVenue.builder()
            .courtType(courtType)
            .siteName("Aberdeen Tribunal Hearing Centre 1")
            .openForPublic(Boolean.FALSE)
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 1")
            .courtStatus("Open")
            .cluster(cluster)
            .postcode("AB11 6LT")
            .courtAddress("AB1, 48 HUNTLY STREET, ABERDEEN")
            .courtVenueId(1L)
            .courtOpenDate(now)
            .closedDate(now)
            .venueName("venueName")
            .isCaseManagementLocation("Y")
            .isHearingLocation("Y")
            .welshVenueName("testVenue")
            .isTemporaryLocation("N")
            .isNightingaleCourt("N")
            .locationType("Court")
            .parentLocation("366559")
            .build();

        LrdCourtVenueResponse courtVenueResponse = new LrdCourtVenueResponse(courtVenue);

        assertEquals("NO", courtVenueResponse.getOpenForPublic());
        assertEquals(cluster.getClusterId(), courtVenueResponse.getClusterId());
        assertEquals(cluster.getClusterName(), courtVenueResponse.getClusterName());
        assertNull(courtVenueResponse.getRegion());
        assertNull(courtVenueResponse.getRegionId());
        assertNotNull(courtVenueResponse.getVenueName());
        assertNotNull(courtVenueResponse.getIsCaseManagementLocation());
        assertNotNull(courtVenueResponse.getIsHearingLocation());
        assertEquals(now.toString(), courtVenueResponse.getClosedDate());
        assertEquals(now.toString(), courtVenueResponse.getCourtOpenDate());
        assertNotNull(courtVenueResponse.getWelshVenueName());
        assertNotNull(courtVenueResponse.getIsTemporaryLocation());
        assertNotNull(courtVenueResponse.getIsNightingaleCourt());
        assertNotNull(courtVenueResponse.getLocationType());
        assertNotNull(courtVenueResponse.getParentLocation());
    }

}
