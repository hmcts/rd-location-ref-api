package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants;
import uk.gov.hmcts.reform.lrdapi.controllers.response.CourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
public class GetBuildingLocationIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    private static final String ONE_STR = "ONE";
    private static final String TWO_STR = "TWO";
    public static final String HTTP_STATUS_STR = "http_status";

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_ValidEpimsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=123456789",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_TwoValidEpimsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=123456789,123456",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, TWO_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_OneValidEpimsIdAndAllGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=123456789,ALL",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, LocationRefConstants.ALL);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_NoEpimsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, LocationRefConstants.ALL);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getBuildLocations_ValidEpimsIdGivenWithLeadingAndTrailingSpace_ReturnValidResponseAndStatusCode200()
        throws JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id= 123456789 ",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_InvalidEpimsIdGiven_ShouldReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=-1111", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_NonStandardCharsEpimsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=!@£@$", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_AsteriskEpimsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=*", LrdBuildingLocationResponse[].class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_AsteriskAlongWithValidEpimsIdGiven_ReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=123456789, *",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_OnlyTwoCommaGivenAsEpimsId_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=,,", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_TwoCommaWithValidEpimsIdGiven_ReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=123456789,,",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_LaunchDarklyFlagSetToOff_ShouldReturnErrorResponseWithStatusCode403()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdApiController.retrieveBuildingLocationDetails",
            "lrd_location_api"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByGivenQueryParam("?epimms_id=123456789", ErrorResponse.class);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    @Test
    public void retrieveBuildLocations_ValidBuildingLocationName_ShouldReturn200() throws
        JsonProcessingException {
        LrdBuildingLocationResponse expectedResponse = getBuildingLocationSampleResponse();

        LrdBuildingLocationResponse actualResponse =
            (LrdBuildingLocationResponse) lrdApiClient
                .findBuildingLocationByGivenQueryParam(
                    "?building_location_name=Building Location A",
                    LrdBuildingLocationResponse.class);

        assertNotNull(actualResponse);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotRetrieveBuildLocations_InValidBuildingLocationName_ShouldReturn404() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .findBuildingLocationByGivenQueryParam(
                    "?building_location_name=Invalid",
                    ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_BuildingLocationName_LdFlagSetToOff_ShouldReturn403()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdApiController.retrieveBuildingLocationDetails",
            "lrd_location_api"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .findBuildingLocationByGivenQueryParam(
                    "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE", ErrorResponse.class);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400WhenMoreThanOneQueryParamsPassed() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .findBuildingLocationByGivenQueryParam("?building_location_name=test&epimms_id=123456789",
                    ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnAllBuildingLocationsWithStatus200WhenAllQueryParamsAreEmpty() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient
                .findBuildingLocationByGivenQueryParam("?building_location_name=&epimms_id=",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, LocationRefConstants.ALL);
    }

    private void responseVerification(List<LrdBuildingLocationResponse> response, String responseType) {
        if (ONE_STR.equalsIgnoreCase(responseType)) {
            assertIterableEquals(response, getSingleLocationResponse());
        } else if (TWO_STR.equalsIgnoreCase(responseType)) {
            assertThat(response).hasSize(2).hasSameElementsAs(getTwoLocationResponse());
        } else {
            assertThat(response).hasSize(3).hasSameElementsAs(getAllLocationResponse());
        }
    }

    private List<LrdBuildingLocationResponse> getSingleLocationResponse() {

        var locationResponses = new ArrayList<LrdBuildingLocationResponse>();

        LrdBuildingLocationResponse response = getBuildingLocationSampleResponse();

        locationResponses.add(response);

        return locationResponses;
    }

    private LrdBuildingLocationResponse getBuildingLocationSampleResponse() {
        Set<CourtVenueResponse> courtVenueResponses = new HashSet<>();
        CourtVenueResponse response1 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 1")
            .openForPublic("YES")
            .epimsId("123456789")
            .regionId("1")
            .region("National")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 1")
            .courtStatus("Open")
            .postcode("AB11 6LT")
            .courtAddress("AB1, 48 HUNTLY STREET, ABERDEEN")
            .courtVenueId("1")
            .build();
        CourtVenueResponse response2 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 2")
            .openForPublic("YES")
            .epimsId("123456789")
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
        CourtVenueResponse response3 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 3")
            .openForPublic("YES")
            .epimsId("123456789")
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
                .regionId("8910")
                .region("Description A")
                .clusterId("0123")
                .clusterName("Cluster A")
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

    private List<LrdBuildingLocationResponse> getTwoLocationResponse() {
        Set<CourtVenueResponse> courtVenueResponses = new HashSet<>();
        CourtVenueResponse response1 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 8")
            .openForPublic("YES")
            .epimsId("123456")
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
        CourtVenueResponse response2 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 9")
            .openForPublic("YES")
            .epimsId("123456")
            .regionId("1")
            .region("National")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 9")
            .courtStatus("Open")
            .postcode("AB11 3RP")
            .courtAddress("AB9, 56 HUNTLY STREET, ABERDEEN")
            .courtVenueId("9")
            .build();
        CourtVenueResponse response3 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 10")
            .openForPublic("YES")
            .epimsId("123456")
            .regionId("1")
            .region("National")
            .clusterId("2")
            .clusterName("Bedfordshire, Cambridgeshire, Hertfordshire")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 10")
            .courtStatus("Open")
            .postcode("AB11 5QA")
            .courtAddress("AB10, 57 HUNTLY STREET, ABERDEEN")
            .courtVenueId("10")
            .build();

        courtVenueResponses.add(response1);
        courtVenueResponses.add(response2);
        courtVenueResponses.add(response3);

        List<LrdBuildingLocationResponse> locationResponses = getSingleLocationResponse();

        LrdBuildingLocationResponse locationResponses2 = LrdBuildingLocationResponse.builder()
            .buildingLocationId("22041996")
            .regionId("891011")
            .region("Description B")
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

    private List<LrdBuildingLocationResponse> getAllLocationResponse() {
        Set<CourtVenueResponse> courtVenueResponses = new HashSet<>();
        CourtVenueResponse response1 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 4")
            .openForPublic("YES")
            .epimsId("epimmsId1234")
            .regionId("1")
            .region("National")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 4")
            .courtStatus("Open")
            .postcode("AB11 4RT")
            .courtAddress("AB4, 51 HUNTLY STREET, ABERDEEN")
            .courtVenueId("4")
            .build();
        CourtVenueResponse response2 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 5")
            .openForPublic("YES")
            .epimsId("epimmsId1234")
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
        CourtVenueResponse response3 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 6")
            .openForPublic("YES")
            .epimsId("epimmsId1234")
            .regionId("2")
            .region("London")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 6")
            .courtStatus("Open")
            .postcode("AB11 7GQ")
            .courtAddress("AB6, 53 HUNTLY STREET, ABERDEEN")
            .courtVenueId("6")
            .build();
        CourtVenueResponse response4 = CourtVenueResponse.builder()
            .courtType("17")
            .siteName("Aberdeen Tribunal Hearing Centre 7")
            .openForPublic("YES")
            .epimsId("epimmsId1234")
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
            .regionId("891011")
            .region("Description B")
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

}
