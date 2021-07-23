package uk.gov.hmcts.reform.lrdapi.controllers.response;

import org.junit.Test;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LrdCourtVenueResponseTest {

    @Test
    public void testCourtVenueTransformationToResponse_ClusterAndDatesAbsent() {

        Region region = new Region();
        region.setCreatedTime(LocalDateTime.now());
        region.setDescription("Region XYZ");
        region.setRegionId("123");
        region.setUpdatedTime(LocalDateTime.now());
        region.setWelshDescription("Region ABC");

        CourtType courtType = new CourtType();
        courtType.setCourtType("CourtTypeXYZ");
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
            .build();

        LrdCourtVenueResponse courtVenueResponse = new LrdCourtVenueResponse(courtVenue);

        assertEquals("YES", courtVenueResponse.getOpenForPublic());
        assertNull(courtVenueResponse.getClusterId());
        assertNull(courtVenueResponse.getClusterName());
        assertEquals(region.getRegionId(), courtVenueResponse.getRegionId());
        assertEquals(region.getDescription(), courtVenueResponse.getRegion());
        assertNull(courtVenueResponse.getClosedDate());
        assertNull(courtVenueResponse.getCourtOpenDate());
    }

    @Test
    public void testCourtVenueTransformationToResponse_ClusterAndDatesPresent() {

        Cluster cluster = new Cluster();
        cluster.setClusterId("456");
        cluster.setClusterName("ClusterXYZ");
        cluster.setWelshClusterName("ClusterABC");
        cluster.setCreatedTime(LocalDateTime.now());
        cluster.setUpdatedTime(LocalDateTime.now());

        CourtType courtType = new CourtType();
        courtType.setCourtType("CourtTypeXYZ");
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
            .build();

        LrdCourtVenueResponse courtVenueResponse = new LrdCourtVenueResponse(courtVenue);

        assertEquals("NO", courtVenueResponse.getOpenForPublic());
        assertEquals(cluster.getClusterId(), courtVenueResponse.getClusterId());
        assertEquals(cluster.getClusterName(), courtVenueResponse.getClusterName());
        assertNull(courtVenueResponse.getRegion());
        assertNull(courtVenueResponse.getRegionId());
        assertEquals(now.toString(), courtVenueResponse.getClosedDate());
        assertEquals(now.toString(), courtVenueResponse.getCourtOpenDate());
    }

}
