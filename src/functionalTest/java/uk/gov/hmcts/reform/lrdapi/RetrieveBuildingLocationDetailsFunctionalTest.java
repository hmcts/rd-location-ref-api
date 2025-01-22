package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lib.util.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.EMPTY_RESULT_DATA_ACCESS;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATIONS_FOR_CLUSTER_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATIONS_FOR_EPIMMS_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATIONS_FOR_REGION_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATION_FOR_BUILDING_LOCATION_NAME;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension.getToggledOffMessage;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
class RetrieveBuildingLocationDetailsFunctionalTest extends AuthorizationFunctionalTest {

    private static final String mapKey = "LrdApiController.retrieveBuildingLocationDetails";
    private static final String path = "/building-locations";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocations_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, "?epimms_id=815833",
                LrdBuildingLocationResponse[].class, path);

        assertThat(response).isNotEmpty();
        boolean isEachIdMatched = Arrays
            .stream(response)
            .map(LrdBuildingLocationResponse::getEpimmsId)
            .allMatch("815833"::equals);
        assertTrue(isEachIdMatched);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildLocations_OneValidepimmsIdAndAllGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK,"?epimms_id=123456,ALL",
                 LrdBuildingLocationResponse[].class, path);

        assertThat(response).isNotEmpty();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveAllBuildingLocations_NoEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, null,
                                                                          LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotEmpty();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocations_MultipleEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, "?epimms_id=815833,219164",
                                                         LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotEmpty().hasSize(2);
        boolean allIdMatched = Arrays
            .stream(response)
            .map(LrdBuildingLocationResponse::getEpimmsId)
            .allMatch(Set.of("815833","219164")::contains);
        assertTrue(allIdMatched);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveAllBuildingLocations_AllEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, "?epimms_id=ALL",
                                                                          LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotEmpty();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldNotRetrieveBuildingLocations_WithStatusCode_404() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND, "?epimms_id=123",
                                                                          LrdBuildingLocationResponse[].class, path);
        assertNotNull(response);
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), response.getErrorMessage());
        assertEquals(String.format(NO_BUILDING_LOCATIONS_FOR_EPIMMS_ID, "[123]"), response.getErrorDescription());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocations_UnauthorizedDueToNoBearerToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveResponseForGivenRequest_NoBearerToken("1", path);

        assertNotNull(response);
        assertThat(response.getHeader("UnAuthorized-Token-Error")).contains("Authentication Exception");
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocations_UnauthorizedDueToNoS2SToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveResponseForGivenRequest_NoS2SToken("1", path);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocationsForValidBuildingName_WithStatusCode_200() {
        final var response = (LrdBuildingLocationResponse)
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.OK,
                               "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE",
                                                                          LrdBuildingLocationResponse.class, path);
        assertNotNull(response);
        assertThat(response.getBuildingLocationName()).isEqualToIgnoringCase("Aberdeen Tribunal Hearing Centre");
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldNotRetrieveBuildingLocationsForInValidBuildingName_WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND, "?building_location_name=Invalid",
                                                                          LrdBuildingLocationResponse.class, path);
        assertNotNull(response);
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), response.getErrorMessage());
        assertEquals(String.format(NO_BUILDING_LOCATION_FOR_BUILDING_LOCATION_NAME, "Invalid"),
                     response.getErrorDescription());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocationsForValidRegionId_WithStatusCode_200() {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.OK, "?region_id=3",
                                                                  LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotEmpty();
        boolean isIdMatched = Arrays
            .stream(response)
            .map(LrdBuildingLocationResponse::getRegionId)
            .allMatch("3"::equals);
        assertTrue(isIdMatched);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldNotRetrieveBuildingLocationsForNonExistentRegionId_WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND, "?region_id=100000000",
                                                                          LrdBuildingLocationResponse[].class, path);
        assertNotNull(response);
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), response.getErrorMessage());
        assertEquals(String.format(NO_BUILDING_LOCATIONS_FOR_REGION_ID, "100000000"), response.getErrorDescription());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocationsForValidClusterId_WithStatusCode_200() {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.OK,"?cluster_id=9",
                                                                  LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotEmpty();
        boolean isIdMatched = Arrays
            .stream(response)
            .map(LrdBuildingLocationResponse::getClusterId)
            .allMatch("9"::equals);
        assertTrue(isIdMatched);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldNotRetrieveBuildingLocationsForNonExistentClusterId_WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND, "?cluster_id=10000000",
                                                                          LrdBuildingLocationResponse.class, path);
        assertNotNull(response);
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), response.getErrorMessage());
        assertEquals(String.format(NO_BUILDING_LOCATIONS_FOR_CLUSTER_ID, "10000000"), response.getErrorDescription());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE", "?region_id=1",
        "?cluster_id=1" })
    @ExtendWith(FeatureToggleConditionExtension.class)
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    void shouldNotRetrieveBuildingLocations_WhenToggledOff_WithStatusCode_403(String input) {
        String exceptionMessage = getToggledOffMessage();
        validateErrorResponse(
            (ErrorResponse) lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.FORBIDDEN, input,
                                                                         LrdBuildingLocationResponse.class, path),
            exceptionMessage,
            exceptionMessage
        );
    }
}
