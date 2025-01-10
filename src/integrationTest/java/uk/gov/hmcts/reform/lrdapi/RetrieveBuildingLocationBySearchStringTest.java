package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationBySearchResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@WithTags({@WithTag("testType:Integration")})
class RetrieveBuildingLocationBySearchStringTest extends LrdAuthorizationEnabledIntegrationTest {
    private static final String path = "/building-locations/search";
    private static final String HTTP_STATUS_STR = "http_status";


    @ParameterizedTest
    @ValueSource(strings = {"ing","loc","bui"})
    @SuppressWarnings("unchecked")
    void shouldRetrieveBuildingLocation_For_SearchString_WithStatusCode_200(String searchString)
        throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationBySearchResponse>)
            lrdApiClient.findBuildingLocationBySearchString(
                "?search=" + searchString,
                LrdBuildingLocationBySearchResponse[].class,
                path
            );
        assertThat(response).isNotEmpty().hasSize(5);
        assertTrue(response.stream().allMatch(location -> location.getBuildingLocationName().toLowerCase()
                .contains(searchString.toLowerCase())));
        assertTrue(response.stream().allMatch(location -> location.getBuildingLocationStatus()
                .equalsIgnoreCase("Open")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"INg","LOC","BUI"})
    @SuppressWarnings("unchecked")
    void shouldRetrieveBuildinLocation_For_SearchStringWithCaseInsencitive_WithStatusCode_200(String searchString)
        throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationBySearchResponse>)
            lrdApiClient.findBuildingLocationBySearchString(
                "?search=" + searchString,
                LrdBuildingLocationBySearchResponse[].class,
                path
            );
        assertThat(response).isNotEmpty().hasSize(5);
        assertTrue(response.stream().allMatch(location -> location.getBuildingLocationName().toLowerCase()
                .contains(searchString.toLowerCase())));
        assertTrue(response.stream().allMatch(location -> location.getBuildingLocationStatus()
                .equalsIgnoreCase("Open")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Building Location B"})
    @SuppressWarnings("unchecked")
    void shouldRetrieveBuildingLocation_For_SearchString_WithFullBuildingName_WithStatusCode_200(String searchString)
        throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationBySearchResponse>)
            lrdApiClient.findBuildingLocationBySearchString(
                "?search=" + searchString,
                LrdBuildingLocationBySearchResponse[].class,
                path
            );
        assertThat(response).isNotEmpty().hasSize(1);
        assertTrue(response.stream().allMatch(location -> location.getBuildingLocationName().toLowerCase()
            .contains(searchString.toLowerCase())));
        assertTrue(response.stream().allMatch(location -> location.getBuildingLocationStatus()
            .equalsIgnoreCase("Open")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Building Location 4"})
    @SuppressWarnings("unchecked")
    void shouldRetrieveBuildingLocation_For_SearchString_ClosedLocation_WithStatusCode_200(String searchString)
        throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationBySearchResponse>)
            lrdApiClient.findBuildingLocationBySearchString(
                "?search=" + searchString,
                LrdBuildingLocationBySearchResponse[].class,
                path
            );
        assertNotNull(response);
        assertEquals(2,response.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnEmptyResponse_For_NoDetailsFoundInDb_WithStatusCode_200()
        throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationBySearchResponse>)
            lrdApiClient.findBuildingLocationBySearchString(
                "?search=Nodata",
                LrdBuildingLocationBySearchResponse[].class,
                path
            );
        assertNotNull(response);
        assertEquals(0,response.size());
    }


    @ParameterizedTest
    @ValueSource(strings = {"?sdf", "kdjdn*",
        "123.","12"})
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenInvalidParamsPassed(String parameter) throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationBySearchString("?search=" + parameter, ErrorResponse.class, path);
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }


    @Test
    @SuppressWarnings("unchecked")
    void searchBuildLocations_LaunchDarklyFlagSetToOff_ShouldReturnErrorResponseWithStatusCode403()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdApiController.retrieveBuildingLocationDetailsBySearchString",
            "lrd_location_search_api"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationBySearchString("?search=loc", ErrorResponse.class, path);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_search_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

}
