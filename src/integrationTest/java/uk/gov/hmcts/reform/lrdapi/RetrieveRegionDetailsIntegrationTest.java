package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.EMPTY_RESULT_DATA_ACCESS;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_REGION_DESCRIPTION_PASSED;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
class RetrieveRegionDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    public static final String HTTP_STATUS = "http_status";

    List<LrdRegionResponse> expectedListAll = List.of(
        new LrdRegionResponse(new Region("1", "London", null)),
        new LrdRegionResponse(new Region("2", "Midlands", null)),
        new LrdRegionResponse(new Region("3", "North East", null)),
        new LrdRegionResponse(new Region("4", "North West", null)),
        new LrdRegionResponse(new Region("5", "South East", null)),
        new LrdRegionResponse(new Region("6", "South West", null)),
        new LrdRegionResponse(new Region("7", "Wales", null)),
        new LrdRegionResponse(new Region("11", "Scotland", null)),
        new LrdRegionResponse(new Region("12", "National", null))
    );

    @ParameterizedTest
    @CsvSource({"London, 1", " London , 1", "'London, Midlands', 2", "LoNdOn, 1"})
    void returnsRegionDetailsByDescriptionWithStatusCode_200(String description, int expectedRegions)
        throws JsonProcessingException {

        final var response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription(description, LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerification(response, expectedRegions);
    }

    @ParameterizedTest
    @CsvSource({"1, 1", "'1, 2', 2"})
    void returnsRegionDetailsByIdWithStatusCode_200(String regionId, int expectedRegions)
        throws JsonProcessingException {

        final var response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById(regionId, LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerification(response, expectedRegions);
    }

    @Test
    void returnsRegionDetailsByIdAllWithStatusCode_200() throws JsonProcessingException, JSONException {

        final var response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("ALL", LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerificationForAll(response, expectedListAll);
    }

    @Test
    void returnsRegionDetailsById1AndAllWithStatusCode_200() throws JsonProcessingException {

        final var response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("1, ALL", LrdRegionResponse[].class);
        List<LrdRegionResponse> sortedList = response.stream()
            .sorted(Comparator.comparing(LrdRegionResponse::getRegionId))
            .collect(Collectors.toList());

        assertNotNull(response);
        assertEquals(9, sortedList.size());
        assertEquals("1", sortedList.get(0).getRegionId());
        assertEquals("London", sortedList.get(0).getDescription());
        assertNull(sortedList.get(0).getWelshDescription());

    }

    @Test
    void returnsRegionDetailsNoParamWithStatusCode_200() throws JsonProcessingException {

        final var response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("", LrdRegionResponse[].class);

        assertNotNull(response);
        assertEquals(8, response.size());
    }

    @Test
    void doesNotReturnRegionDetailsByInvalidDescriptionWithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("Invalid Description", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals("No Region(s) found with the given Region Description(s): [Invalid Description]",
                     errorResponse.getErrorDescription());
    }

    @Test
    void doesNotReturnRegionByInvalidDescriptionSpecialCharWithStatusCode_400() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("Lo£nd*on", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals(String.format(EXCEPTION_MSG_NO_VALID_REGION_DESCRIPTION_PASSED, "[Lo£nd*on]"),
                     errorResponse.getErrorDescription());
    }

    @Test
    void doesNotReturnRegionByInvalidIdnSpecialCharWithStatusCode_400() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("1*", ErrorResponse.class);


        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals(String.format(EXCEPTION_MSG_NO_VALID_REGION_DESCRIPTION_PASSED, "[1*]"),
                     errorResponse.getErrorDescription());
    }

    @Test
    void returnsRegionDetails_LdFlagOff_WithStatusCode_403()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdApiController.retrieveRegionDetails",
            "lrd_location_api"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("London", ErrorResponse.class);
        assertThat(errorResponseMap).containsEntry("http_status", HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    private void responseVerification(List<LrdRegionResponse> response, int expectedRegions) {
        assertThat(response.size()).isEqualTo(expectedRegions);
        assertThat(response.get(0).getRegionId()).isEqualTo("1");
        assertThat(response.get(0).getDescription()).isEqualTo("London");
        assertThat(response.get(0).getWelshDescription()).isNull();
        if (expectedRegions == 2) {
            assertThat(response.get(1).getRegionId()).isEqualTo("2");
            assertThat(response.get(1).getDescription()).isEqualTo("Midlands");
            assertThat(response.get(1).getWelshDescription()).isNull();
        }
    }

    private void responseVerificationForAll(List<LrdRegionResponse> actualData, List<LrdRegionResponse> expectedData) throws JsonProcessingException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        String actual = objectMapper.writeValueAsString(actualData);
        String expected = objectMapper.writeValueAsString(expectedData);
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

}
