package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RetrieveBuildingLocationIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    private static final String path = "/building-locations";

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_OneValidepimmsIdAndAllGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789,ALL",
                                                         LrdBuildingLocationResponse[].class, path
            );

        assertNotNull(response);
        responseVerification(response, LocationRefConstants.ALL);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_ValidClusterIdPassed_ShouldReturn200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> actualResponse =
            (List<LrdBuildingLocationResponse>) lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?cluster_id=01234",
                    LrdBuildingLocationResponse[].class, path
                );

        assertNotNull(actualResponse);
        responseVerification(actualResponse, LocationRefConstants.ALL);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_ValidClusterIdWithLeadingAndTrailingSpacePassed_ShouldReturn200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> actualResponse =
            (List<LrdBuildingLocationResponse>) lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?cluster_id= 01234 ",
                    LrdBuildingLocationResponse[].class, path
                );

        assertNotNull(actualResponse);
        responseVerification(actualResponse, LocationRefConstants.ALL);
    }

    private void responseVerification(List<LrdBuildingLocationResponse> response, String responseType) {
        if (LocationRefConstants.ALL.equalsIgnoreCase(responseType)) {
            assertThat(response).hasSize(4).hasSameElementsAs(getAllLocationResponse());
        }
    }

    private List<LrdBuildingLocationResponse> getAllLocationResponse() {
        List<LrdBuildingLocationResponse> locationResponses = getAllOpenLocationResponse();
        LrdBuildingLocationResponse locationResponses4 = LrdBuildingLocationResponse.builder()
            .buildingLocationId("22041998")
            .regionId("2")
            .region("London")
            .clusterId("01234")
            .clusterName("Cluster B")
            .buildingLocationStatus("Closed")
            .epimmsId("123EpimmsId456")
            .buildingLocationName("Building Location 4")
            .area("Area D")
            .postcode("EC2A 3AQ")
            .address("4 Street, London")
            .courtFinderUrl("Court Finder URL 4")
            .build();

        locationResponses.add(locationResponses4);
        return locationResponses;
    }

    private List<LrdBuildingLocationResponse> getAllOpenLocationResponse() {
        Set<LrdCourtVenueResponse> courtVenueResponses = new HashSet<>();
        LrdCourtVenueResponse response1 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 4")
            .openForPublic("YES")
            .epimmsId("epimmsId1234")
            .regionId("1")
            .region("National")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 4")
            .courtStatus("Open")
            .postcode("AB11 4RT")
            .courtAddress("AB4, 51 HUNTLY STREET, ABERDEEN")
            .courtVenueId("4")
            .build();
        LrdCourtVenueResponse response2 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 5")
            .openForPublic("YES")
            .epimmsId("epimmsId1234")
            .regionId("1")
            .region("National")
            .clusterId("1")
            .clusterName("Avon, Somerset and Gloucestershire")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 5")
            .courtStatus("Open")
            .postcode("AB11 4EQ")
            .courtAddress("AB5, 52 HUNTLY STREET, ABERDEEN")
            .courtVenueId("5")
            .build();
        LrdCourtVenueResponse response3 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 6")
            .openForPublic("YES")
            .epimmsId("epimmsId1234")
            .regionId("2")
            .region("London")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 6")
            .courtStatus("Open")
            .postcode("AB11 7GQ")
            .courtAddress("AB6, 53 HUNTLY STREET, ABERDEEN")
            .courtVenueId("6")
            .build();
        LrdCourtVenueResponse response4 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 7")
            .openForPublic("YES")
            .epimmsId("epimmsId1234")
            .regionId("2")
            .region("London")
            .clusterId("2")
            .clusterName("Bedfordshire, Cambridgeshire, Hertfordshire")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 7")
            .courtStatus("Open")
            .postcode("AB11 1TY")
            .courtAddress("AB7, 54 HUNTLY STREET, ABERDEEN")
            .courtVenueId("7")
            .build();

        courtVenueResponses.add(response1);
        courtVenueResponses.add(response2);
        courtVenueResponses.add(response3);
        courtVenueResponses.add(response4);

        List<LrdBuildingLocationResponse> locationResponses = getTwoLocationResponse();

        LrdBuildingLocationResponse locationResponses3 = LrdBuildingLocationResponse.builder()
            .buildingLocationId("22041997")
            .regionId("2")
            .region("London")
            .clusterId("01234")
            .clusterName("Cluster B")
            .buildingLocationStatus("Open")
            .epimmsId("epimmsId1234")
            .buildingLocationName("Building Location C")
            .area("Area C")
            .postcode("EC2A 2YZ")
            .address("3 Street, London")
            .courtFinderUrl("Court Finder URL 3")
            .courtVenues(courtVenueResponses)
            .build();

        locationResponses.add(locationResponses3);

        return locationResponses;
    }

    private List<LrdBuildingLocationResponse> getTwoLocationResponse() {
        Set<LrdCourtVenueResponse> courtVenueResponses = new HashSet<>();
        LrdCourtVenueResponse response1 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 8")
            .openForPublic("YES")
            .epimmsId("123456")
            .regionId("2")
            .region("London")
            .clusterId("2")
            .clusterName("Bedfordshire, Cambridgeshire, Hertfordshire")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 8")
            .courtStatus("Open")
            .postcode("AB11 2HT")
            .courtAddress("AB8, 55 HUNTLY STREET, ABERDEEN")
            .courtVenueId("8")
            .build();
        LrdCourtVenueResponse response2 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 9")
            .openForPublic("YES")
            .epimmsId("123456")
            .regionId("1")
            .region("National")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 9")
            .courtStatus("Open")
            .postcode("AB11 3RP")
            .courtAddress("AB9, 56 HUNTLY STREET, ABERDEEN")
            .courtVenueId("9")
            .build();
        LrdCourtVenueResponse response3 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 10")
            .openForPublic("YES")
            .epimmsId("123456")
            .regionId("1")
            .region("National")
            .clusterId("2")
            .clusterName("Bedfordshire, Cambridgeshire, Hertfordshire")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 10")
            .courtStatus("Open")
            .postcode("AB11 5QA")
            .courtAddress("AB10, 57 HUNTLY STREET, ABERDEEN")
            .isCaseManagementLocation("Y")
            .isHearingLocation("Y")
            .locationType("CTSC")
            .isTemporaryLocation("Y")
            .courtVenueId("10")
            .build();
        LrdCourtVenueResponse response4 = LrdCourtVenueResponse.builder()
            .courtTypeId("10")
            .courtType("County Court")
            .siteName("Aberdeen Tribunal Hearing Centre 11")
            .openForPublic("YES")
            .epimmsId("123456")
            .regionId("1")
            .region("National")
            .clusterId("2")
            .clusterName("Bedfordshire, Cambridgeshire, Hertfordshire")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 11")
            .courtStatus("Open")
            .postcode("AB11 5QA")
            .courtAddress("AB10, 57 HUNTLY STREET, ABERDEEN")
            .isCaseManagementLocation("N")
            .isHearingLocation("N")
            .locationType("NBC")
            .isTemporaryLocation("N")
            .courtVenueId("11")
            .build();

        courtVenueResponses.add(response1);
        courtVenueResponses.add(response2);
        courtVenueResponses.add(response3);
        courtVenueResponses.add(response4);

        List<LrdBuildingLocationResponse> locationResponses = getSingleLocationResponse();

        LrdBuildingLocationResponse locationResponses2 = LrdBuildingLocationResponse.builder()
            .buildingLocationId("22041996")
            .regionId("2")
            .region("London")
            .clusterId("01234")
            .clusterName("Cluster B")
            .buildingLocationStatus("Open")
            .epimmsId("123456")
            .buildingLocationName("Building Location B")
            .area("Area B")
            .postcode("SW19 2YZ")
            .address("2 Street, London")
            .courtFinderUrl("Court Finder URL 2")
            .courtVenues(courtVenueResponses)
            .build();

        locationResponses.add(locationResponses2);

        return locationResponses;
    }

    private List<LrdBuildingLocationResponse> getSingleLocationResponse() {

        var locationResponses = new ArrayList<LrdBuildingLocationResponse>();

        LrdBuildingLocationResponse response = getBuildingLocationSampleResponse();

        locationResponses.add(response);

        return locationResponses;
    }

    private LrdBuildingLocationResponse getBuildingLocationSampleResponse() {
        Set<LrdCourtVenueResponse> courtVenueResponses = new HashSet<>();
        LrdCourtVenueResponse response1 = LrdCourtVenueResponse.builder()
            .courtTypeId("23")
            .courtType("Immigration and Asylum Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 1")
            .openForPublic("YES")
            .epimmsId("123456789")
            .regionId("1")
            .region("National")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 1")
            .courtStatus("Open")
            .postcode("AB11 6LT")
            .courtAddress("AB1, 48 HUNTLY STREET, ABERDEEN")
            .courtVenueId("1")
            .venueName("venueName1")
            .isCaseManagementLocation("Y")
            .isHearingLocation("Y")
            .welshVenueName("testVenue")
            .isTemporaryLocation("N")
            .isNightingaleCourt("N")
            .locationType("Court")
            .parentLocation("366559")
            .build();
        LrdCourtVenueResponse response2 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 2")
            .openForPublic("YES")
            .epimmsId("123456789")
            .regionId("1")
            .region("National")
            .clusterId("1")
            .clusterName("Avon, Somerset and Gloucestershire")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 2")
            .courtStatus("Open")
            .postcode("AB11 7KT")
            .courtAddress("AB2, 49 HUNTLY STREET, ABERDEEN")
            .courtVenueId("2")
            .build();
        LrdCourtVenueResponse response3 = LrdCourtVenueResponse.builder()
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .siteName("Aberdeen Tribunal Hearing Centre 3")
            .openForPublic("YES")
            .epimmsId("123456789")
            .regionId("2")
            .region("London")
            .clusterId("1")
            .clusterName("Avon, Somerset and Gloucestershire")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 3")
            .courtStatus("Open")
            .postcode("AB11 8IP")
            .courtAddress("AB3, 50 HUNTLY STREET, ABERDEEN")
            .courtVenueId("3")
            .build();

        courtVenueResponses.add(response1);
        courtVenueResponses.add(response2);
        courtVenueResponses.add(response3);

        return LrdBuildingLocationResponse.builder()
            .buildingLocationId("22041995")
            .regionId("1")
            .region("National")
            .clusterId("01234")
            .clusterName("Cluster B")
            .buildingLocationStatus("Open")
            .epimmsId("123456789")
            .buildingLocationName("Building Location A")
            .area("Area A")
            .postcode("WX67 2YZ")
            .address("1 Street, London")
            .courtFinderUrl("Court Finder URL")
            .courtVenues(courtVenueResponses)
            .build();
    }

}
