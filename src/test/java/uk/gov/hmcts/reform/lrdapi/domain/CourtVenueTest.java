package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CourtVenueTest {

    @Test
    public void testCourtVenue() {

        BuildingLocation buildingLocation = new BuildingLocation();
        buildingLocation.setEpimmsId("123456789");

        Region region = new Region();
        region.setDescription("Region XYZ");
        region.setRegionId("123");

        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("17");
        courtType.setCourtType("courtType");

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
        assertEquals(now,courtVenue.getClosedDate().get());
        assertEquals(now,courtVenue.getCourtOpenDate().get());
        assertEquals(now, courtVenue.getCreatedTime());
        assertEquals(now, courtVenue.getUpdatedTime());

    }

}
