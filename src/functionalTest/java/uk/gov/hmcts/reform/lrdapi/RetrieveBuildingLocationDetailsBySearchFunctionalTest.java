package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lib.util.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationBySearchResponse;
import uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.SEARCH_STRING_VALUE_ERROR_MESSAGE;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
class RetrieveBuildingLocationDetailsBySearchFunctionalTest extends AuthorizationFunctionalTest {

    private static final String mapKey = "LrdApiController.retrieveBuildingLocationDetailsBySearchString";
    private static final String path = "/building-locations/search";

    private static final String flag = "lrd_location_search_api";


    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocationsBySearch_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationBySearchResponse[]) lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.OK,
                "?search=loc",
                LrdBuildingLocationBySearchResponse[].class,
                path
        );
        //if data not found in higher env
        if (response.length > 0) {
            assertTrue(Arrays.stream(response).allMatch(location -> location.getBuildingLocationName().toLowerCase()
                .contains("loc")));
            assertTrue(Arrays.stream(response).allMatch(location -> location.getBuildingLocationStatus()
                .equalsIgnoreCase("Open")));
        } else {
            assertEquals(0, response.length);
        }
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocationsBySearchNoDataFound_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationBySearchResponse[]) lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.OK,
                "?search=lssnodatsc",
                LrdBuildingLocationBySearchResponse[].class,
                path
        );

        assertEquals(0, response.length);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocationsBySearch_NoSearchString_WithStatusCode_400() throws JsonProcessingException {

        ErrorResponse response = (ErrorResponse) lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.BAD_REQUEST,
                "?search=",
                LrdBuildingLocationBySearchResponse[].class,
                path
        );
        assertThat(response).isNotNull();
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), response.getErrorMessage());
        assertEquals(String.format(SEARCH_STRING_VALUE_ERROR_MESSAGE, ""), response.getErrorDescription());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocationsBySearch_InvalidSearchString_WithStatusCode_400() throws JsonProcessingException {

        ErrorResponse response = (ErrorResponse) lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.BAD_REQUEST,
                "?search=sdfd*+",
                LrdBuildingLocationBySearchResponse[].class,
                path
        );
        assertThat(response).isNotNull();
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), response.getErrorMessage());
        assertEquals(String.format(SEARCH_STRING_VALUE_ERROR_MESSAGE, ""), response.getErrorDescription());
    }

    @Test
    @ExtendWith(FeatureToggleConditionExtension.class)
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    void shouldNotRetrieveBuildingLocations_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse) lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.FORBIDDEN,
                "?search=lssnodatsc",
                LrdBuildingLocationBySearchResponse[].class,
                path
        );

        assertNotNull(response);
        assertEquals(flag.concat(SPACE).concat(
                     FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD), response.getErrorMessage());
        assertEquals(flag.concat(SPACE).concat(
            FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD), response.getErrorDescription());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocations_UnauthorizedDueToNoBearerToken_ShouldReturnStatusCode401() {
        Response response = lrdApiClient.retrieveResponseForGivenRequest_NoBearerToken("1", path);

        assertNotNull(response);
        assertThat(response.getHeader("UnAuthorized-Token-Error")).contains("Authentication Exception");
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocations_UnauthorizedDueToNoS2SToken_ShouldReturnStatusCode401() {
        Response response = lrdApiClient.retrieveResponseForGivenRequest_NoS2SToken("1", path);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

}
