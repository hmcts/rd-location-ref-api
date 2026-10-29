package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CourtVenueTest {

    @Test
    void testCourtVenue() {

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
        courtVenue.setServiceCode("AAA2");

        assertEquals("1", courtVenue.getCourtVenueId().toString());
        assertEquals("siteName", courtVenue.getSiteName());
        assertEquals(region, courtVenue.getRegion().get());
        assertEquals(courtType, courtVenue.getCourtType());
        assertEquals(cluster, courtVenue.getCluster().get());
        assertTrue(courtVenue.getOpenForPublic());
        assertEquals("courtAddress",courtVenue.getCourtAddress());
        assertEquals("AB EYZ",courtVenue.getPostcode());
        assertEquals("122324234",courtVenue.getPhoneNumber());
        assertEquals("courtLocationCode",courtVenue.getCourtLocationCode());
        assertEquals("dxAddress",courtVenue.getDxAddress());
        assertEquals("welshSiteName",courtVenue.getWelshSiteName());
        assertEquals("welshCourtAddress",courtVenue.getWelshCourtAddress());
        assertEquals("Open",courtVenue.getCourtStatus());
        assertEquals("courtName",courtVenue.getCourtName());
        assertEquals("venueName",courtVenue.getVenueName());
        assertEquals("Y",courtVenue.getIsCaseManagementLocation());
        assertEquals("Y",courtVenue.getIsHearingLocation());
        assertEquals("",courtVenue.getWelshVenueName());
        assertEquals("N",courtVenue.getIsTemporaryLocation());
        assertEquals(now,courtVenue.getClosedDate().get());
        assertEquals(now,courtVenue.getCourtOpenDate().get());
        assertEquals(now, courtVenue.getCreatedTime());
        assertEquals(now, courtVenue.getUpdatedTime());
        assertEquals("AAA2",courtVenue.getServiceCode());

    }

    @Test
    void testCourtVenueRequestParam() {
        CourtVenueRequestParam courtVenueRequestParam =
            new CourtVenueRequestParam();

        courtVenueRequestParam.setIsCaseManagementLocation("Y");
        courtVenueRequestParam.setIsHearingLocation("Y");
        courtVenueRequestParam.setIsTemporaryLocation("N");
        courtVenueRequestParam.setLocationType("CTSC");

        assertEquals("Y", courtVenueRequestParam.getIsCaseManagementLocation());
        assertEquals("Y", courtVenueRequestParam.getIsHearingLocation());
        assertEquals("N", courtVenueRequestParam.getIsTemporaryLocation());
        assertEquals("CTSC", courtVenueRequestParam.getLocationType());

    }

    @Test
    void testCourtVenueBuilderWithServiceCode() {
        Region region = new Region();
        region.setDescription("regionDescription");
        region.setRegionId("789");

        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("20");
        courtType.setTypeOfCourt("courtType2");

        Cluster cluster = new Cluster();
        cluster.setClusterId("789");
        cluster.setClusterName("clusterName2");

        LocalDateTime now = LocalDateTime.now();

        CourtVenue courtVenue = CourtVenue.builder()
            .courtVenueId(2L)
            .siteName("siteName2")
            .region(region)
            .courtType(courtType)
            .cluster(cluster)
            .openForPublic(Boolean.FALSE)
            .courtAddress("courtAddress2")
            .postcode("AB CDE")
            .phoneNumber("987654321")
            .courtLocationCode("courtLocationCode2")
            .dxAddress("dxAddress2")
            .welshSiteName("welshSiteName2")
            .welshCourtAddress("welshCourtAddress2")
            .courtStatus("Open")
            .courtOpenDate(now)
            .courtName("courtName2")
            .createdTime(now)
            .updatedTime(now)
            .venueName("venueName2")
            .isCaseManagementLocation("N")
            .isHearingLocation("Y")
            .welshVenueName("welshVenueName2")
            .isTemporaryLocation("Y")
            .isNightingaleCourt("N")
            .locationType("Court")
            .parentLocation("100")
            .serviceCode("AAA3")
            .build();

        assertEquals(2L, courtVenue.getCourtVenueId());
        assertEquals("siteName2", courtVenue.getSiteName());
        assertEquals("789", courtVenue.getRegion().get().getRegionId());
        assertEquals("20", courtVenue.getCourtType().getCourtTypeId());
        assertEquals("789", courtVenue.getCluster().get().getClusterId());
        assertTrue(!courtVenue.getOpenForPublic());
        assertEquals("courtAddress2", courtVenue.getCourtAddress());
        assertEquals("AB CDE", courtVenue.getPostcode());
        assertEquals("987654321", courtVenue.getPhoneNumber());
        assertEquals("courtLocationCode2", courtVenue.getCourtLocationCode());
        assertEquals("dxAddress2", courtVenue.getDxAddress());
        assertEquals("welshSiteName2", courtVenue.getWelshSiteName());
        assertEquals("welshCourtAddress2", courtVenue.getWelshCourtAddress());
        assertEquals("Open", courtVenue.getCourtStatus());
        assertEquals("courtName2", courtVenue.getCourtName());
        assertEquals("venueName2", courtVenue.getVenueName());
        assertEquals("N", courtVenue.getIsCaseManagementLocation());
        assertEquals("Y", courtVenue.getIsHearingLocation());
        assertEquals("welshVenueName2", courtVenue.getWelshVenueName());
        assertEquals("Y", courtVenue.getIsTemporaryLocation());
        assertEquals("N", courtVenue.getIsNightingaleCourt());
        assertEquals("Court", courtVenue.getLocationType());
        assertEquals("100", courtVenue.getParentLocation());
        assertEquals("AAA3", courtVenue.getServiceCode());
    }

    @Test
    void testCourtVenueServiceCodeValidation() {
        CourtVenue courtVenue1 = new CourtVenue();
        courtVenue1.setServiceCode("AAA2");
        assertEquals("AAA2", courtVenue1.getServiceCode());

        CourtVenue courtVenue2 = CourtVenue.builder()
            .courtVenueId(1L)
            .serviceCode("AAA6")
            .build();
        assertEquals("AAA6", courtVenue2.getServiceCode());

        CourtVenue courtVenue3 = CourtVenue.builder()
            .courtVenueId(2L)
            .serviceCode("ABA4")
            .build();
        assertEquals("ABA4", courtVenue3.getServiceCode());
    }

}
