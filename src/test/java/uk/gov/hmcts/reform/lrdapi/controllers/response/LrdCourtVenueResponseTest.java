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
            .welshCourtName("welshCourtName1")
            .uprn("1234")
            .venueOuCode("87675")
            .mrdBuildingLocationId("8686")
            .mrdVenueId("765")
            .serviceUrl("https://serviceurl.com")
            .factUrl("https://facturl.com")
            .externalShortName("External Short Court")
            .welshExternalShortName("Welsh External Court")
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
        assertNotNull(courtVenueResponse.getWelshCourtName());
        assertNotNull(courtVenueResponse.getUprn());
        assertNotNull(courtVenueResponse.getVenueOuCode());
        assertNotNull(courtVenueResponse.getMrdBuildingLocationId());
        assertNotNull(courtVenueResponse.getMrdVenueId());
        assertNotNull(courtVenueResponse.getServiceUrl());
        assertNotNull(courtVenueResponse.getFactUrl());
        assertNotNull(courtVenueResponse.getExternalShortName());
        assertNotNull(courtVenueResponse.getWelshExternalShortName());
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
            .welshCourtName("welshCourtName1")
            .uprn("1234")
            .venueOuCode("87675")
            .mrdBuildingLocationId("8686")
            .mrdVenueId("765")
            .serviceUrl("https://serviceurl.com")
            .factUrl("https://facturl.com")
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
        assertNotNull(courtVenueResponse.getWelshCourtName());
        assertNotNull(courtVenueResponse.getUprn());
        assertNotNull(courtVenueResponse.getVenueOuCode());
        assertNotNull(courtVenueResponse.getMrdBuildingLocationId());
        assertNotNull(courtVenueResponse.getMrdVenueId());
        assertNotNull(courtVenueResponse.getServiceUrl());
        assertNotNull(courtVenueResponse.getFactUrl());
    }

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
        courtVenue.setWelshCourtName("welshCourtName1");
        courtVenue.setUprn("1234");
        courtVenue.setVenueOuCode("87675");
        courtVenue.setMrdBuildingLocationId("8686");
        courtVenue.setMrdVenueId("765");
        courtVenue.setServiceUrl("https://serviceurl.com");
        courtVenue.setFactUrl("https://facturl.com");

        LrdCourtVenueResponse courtVenueResponse = new LrdCourtVenueResponse(courtVenue);

        assertEquals("",courtVenueResponse.getWelshVenueName());
        assertEquals("N",courtVenueResponse.getIsTemporaryLocation());
        assertEquals("N",courtVenueResponse.getIsNightingaleCourt());
        assertEquals("Court",courtVenueResponse.getLocationType());
        assertEquals("",courtVenueResponse.getParentLocation());
        assertEquals("welshCourtName1", courtVenueResponse.getWelshCourtName());
        assertEquals("1234", courtVenueResponse.getUprn());
        assertEquals("87675", courtVenueResponse.getVenueOuCode());
        assertEquals("8686", courtVenueResponse.getMrdBuildingLocationId());
        assertEquals("765", courtVenueResponse.getMrdVenueId());
        assertEquals("https://serviceurl.com", courtVenueResponse.getServiceUrl());
        assertEquals("https://facturl.com", courtVenueResponse.getFactUrl());
    }

}
