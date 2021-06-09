package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
public class GetBuildingLocationIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_ValidEpimsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=123456789", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response, "ONE");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_TwoValidEpimsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=123456789,123456", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response, "TWO");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_OneValidEpimsIdAndAllGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=123456789,ALL", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response, "ALL");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_NoEpimsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response, "ALL");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getBuildLocations_ValidEpimsIdGivenWithLeadingAndTrailingSpace_ReturnValidResponseAndStatusCode200()
        throws JsonProcessingException {

        List<LrdBuildingLocationResponse> response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id= 123456789 ", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response, "ONE");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_InvalidEpimsIdGiven_ShouldReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=-1111", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_NonStandardCharsEpimsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=!@£@$", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_AsteriskEpimsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=*", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_AsteriskAlongWithValidEpimsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=123456789, *", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_TwoCommaOnlyGivenAsEpimsId_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=,,", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_TwoCommaWithValidEpimsIdGiven_ReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("?epimms_id=123456789,,", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_LaunchDarklyFlagSetToOff_ShouldReturnErrorResponseWithStatusCode403()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdApiController.retrieveBuildingLocationDetailsByEpimsId",
            "lrd_location_api"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("123456789", ErrorResponse.class);
        assertThat(errorResponseMap).containsEntry("http_status", HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    private void responseVerification(List<LrdBuildingLocationResponse> response, String responseType) {
        if(responseType.equalsIgnoreCase("ONE")) {
            assertIterableEquals(response, getSingleLocationResponse());
        } else if (responseType.equalsIgnoreCase("TWO")) {
            assertIterableEquals(response, getTwoLocationResponse());
        } else {
            assertIterableEquals(response, getAllLocationResponse());
        }
    }

    private List<LrdBuildingLocationResponse> getSingleLocationResponse() {

        List<LrdBuildingLocationResponse> locationResponses = new ArrayList<>();

        LrdBuildingLocationResponse response = LrdBuildingLocationResponse.builder()
            .buildingLocationId("22041995")
            .regionId("8910")
            .region("Description A")
            .clusterId("0123")
            .clusterName("Cluster A")
            .buildingLocationStatus("Status A")
            .epimmsId("123456789")
            .buildingLocationName("Building Location A")
            .area("Area A")
            .postcode("WX67 2YZ")
            .address("1 Street, London")
            .courtFinderUrl("Court Finder URL")
            .build();

        locationResponses.add(response);

        return locationResponses;
    }

    private List<LrdBuildingLocationResponse> getTwoLocationResponse() {

        List<LrdBuildingLocationResponse> locationResponses = getSingleLocationResponse();

        LrdBuildingLocationResponse response2 = LrdBuildingLocationResponse.builder()
            .buildingLocationId("22041996")
            .regionId("891011")
            .region("Description B")
            .clusterId("01234")
            .clusterName("Cluster B")
            .buildingLocationStatus("Status B")
            .epimmsId("123456")
            .buildingLocationName("Building Location B")
            .area("Area B")
            .postcode("SW19 2YZ")
            .address("2 Street, London")
            .courtFinderUrl("Court Finder URL 2")
            .build();

        locationResponses.add(response2);

        return locationResponses;
    }

    private List<LrdBuildingLocationResponse> getAllLocationResponse() {

        List<LrdBuildingLocationResponse> locationResponses = getTwoLocationResponse();

        LrdBuildingLocationResponse response3 = LrdBuildingLocationResponse.builder()
            .buildingLocationId("22041997")
            .regionId("891011")
            .region("Description B")
            .clusterId("01234")
            .clusterName("Cluster B")
            .buildingLocationStatus("Status B")
            .epimmsId("epimmsId1234")
            .buildingLocationName("Building Location C")
            .area("Area C")
            .postcode("EC2A 2YZ")
            .address("3 Street, London")
            .courtFinderUrl("Court Finder URL 3")
            .build();

        locationResponses.add(response3);

        return locationResponses;
    }

}
