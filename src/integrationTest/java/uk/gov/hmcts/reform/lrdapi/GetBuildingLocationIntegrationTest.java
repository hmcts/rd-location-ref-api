package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;

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

@WithTags({@WithTag("testType:Integration")})
class GetBuildingLocationIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    private static final String ONE_STR = "ONE";
    private static final String TWO_STR = "TWO";
    private static final String ALL_OPEN_STR = "ALL_OPEN";
    public static final String HTTP_STATUS_STR = "http_status";
    private static final String path = "/building-locations";

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_ValidepimmsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789",
                                                         LrdBuildingLocationResponse[].class, path
            );

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_TwoValidepimmsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789,123456",
                                                         LrdBuildingLocationResponse[].class, path
            );

        assertNotNull(response);
        responseVerification(response, TWO_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_NoepimmsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=",
                                                         LrdBuildingLocationResponse[].class, path
            );

        assertNotNull(response);
        responseVerification(response, ALL_OPEN_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getBuildLocations_ValidepimmsIdGivenWithLeadingAndTrailingSpace_ReturnValidResponseAndStatusCode200()
        throws JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id= 123456789 ",
                                                         LrdBuildingLocationResponse[].class, path
            );

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_InvalidepimmsIdGiven_ShouldReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=-1111", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_NonStandardCharsepimmsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=!@£@$", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_AsteriskepimmsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=*", LrdBuildingLocationResponse[].class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_AsteriskAlongWithValidepimmsIdGiven_ReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789, *",
                                                         LrdBuildingLocationResponse[].class, path
            );

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_OnlyTwoCommaGivenAsepimmsId_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=,,", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_TwoCommaWithValidepimmsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789,,", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_LaunchDarklyFlagSetToOff_ShouldReturnErrorResponseWithStatusCode403()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdApiController.retrieveBuildingLocationDetails",
            "lrd_location_api"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789", ErrorResponse.class, path);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    @Test
    void retrieveBuildLocations_ValidBuildingLocationName_ShouldReturn200() throws
        JsonProcessingException {
        LrdBuildingLocationResponse expectedResponse = getBuildingLocationSampleResponse();

        LrdBuildingLocationResponse actualResponse =
            (LrdBuildingLocationResponse) lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?building_location_name=Building Location A",
                    LrdBuildingLocationResponse.class, path
                );

        assertNotNull(actualResponse);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_InValidBuildingLocationName_ShouldReturn404() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?building_location_name=Invalid",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_MultipleBuildingLocationNamePassed_ShouldReturn400() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?building_location_name=Building Location A, B",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_BuildingLocationName_LdFlagSetToOff_ShouldReturn403()
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
                .retrieveResponseForGivenRequest(
                    "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE", ErrorResponse.class, path);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_MultipleClusterIdPassed_ShouldReturn400() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?cluster_id=1,2",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_NonNumericClusterIdPassed_ShouldReturn400() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?cluster_id=Invalid",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_ClusterIdWithNoLocationsPassed_ShouldReturn404() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?cluster_id=25",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_NonExistingClusterId_ShouldReturn404() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?cluster_id=25000000000",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_ClusterId_LdFlagSetToOff_ShouldReturn403()
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
                .retrieveResponseForGivenRequest(
                    "?cluster_id=1", ErrorResponse.class, path);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_ValidRegionIdPassed_ShouldReturn200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> actualResponse =
            (List<LrdBuildingLocationResponse>) lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?region_id=1",
                    LrdBuildingLocationResponse[].class, path
                );

        assertNotNull(actualResponse);
        responseVerification(actualResponse, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_ValidRegionIdWithLeadingAndTrialingSpacePassed_ShouldReturn200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> actualResponse =
            (List<LrdBuildingLocationResponse>) lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?region_id= 1 ",
                    LrdBuildingLocationResponse[].class, path
                );

        assertNotNull(actualResponse);
        responseVerification(actualResponse, ONE_STR);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_MultipleRegionIdPassed_ShouldReturn400() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?region_id=1,2",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_NonNumericRegionIdPassed_ShouldReturn400() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?region_id=Invalid",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_RegionIdWithNoLocationsPassed_ShouldReturn404() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?region_id=25",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotRetrieveBuildLocations_NonExistingCRegionId_ShouldReturn404() throws
        JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest(
                    "?region_id=25000000000",
                    ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveBuildLocations_RegionId_LdFlagSetToOff_ShouldReturn403()
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
                .retrieveResponseForGivenRequest(
                    "?region_id=1", ErrorResponse.class, path);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }


    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400WhenMoreThanOneQueryParamsPassed() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient
                .retrieveResponseForGivenRequest("?building_location_name=test&epimms_id=123456789",
                                                 ErrorResponse.class, path
                );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnAllBuildingLocationsWithStatus200WhenAllQueryParamsAreEmpty() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient
                .retrieveResponseForGivenRequest("?building_location_name=&epimms_id=",
                                                 LrdBuildingLocationResponse[].class, path
                );

        assertNotNull(response);
        responseVerification(response, ALL_OPEN_STR);
    }

    private void responseVerification(List<LrdBuildingLocationResponse> response, String responseType) {
        if (ONE_STR.equalsIgnoreCase(responseType)) {
            assertIterableEquals(response, getSingleLocationResponse());
        } else if (TWO_STR.equalsIgnoreCase(responseType)) {
            assertThat(response).hasSize(2).hasSameElementsAs(getTwoLocationResponse());
        } else if (LocationRefConstants.ALL.equalsIgnoreCase(responseType)) {
            assertThat(response).hasSize(4).hasSameElementsAs(getAllLocationResponse());
        } else {
            assertThat(response).hasSize(3).hasSameElementsAs(getAllOpenLocationResponse());
        }
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

}
