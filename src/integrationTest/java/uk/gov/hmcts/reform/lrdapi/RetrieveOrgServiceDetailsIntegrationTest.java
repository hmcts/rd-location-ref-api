package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.EMPTY_RESULT_DATA_ACCESS;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
class RetrieveOrgServiceDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    public static final String HTTP_STATUS = "http_status";

    @Test
    void returnsOrgServiceDetailsByServiceCodeWithStatusCode200() throws JsonProcessingException {

        final var responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByServiceCode("AAA6", LrdOrgInfoServiceResponse[].class);

        assertThat(responses).isNotEmpty().hasSize(1);
        responseVerification(responses);
    }

    @Test
    void doNotReturnOrgServiceDetailsByUnknownServiceCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findOrgServiceDetailsByServiceCode("A1A7", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS,HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), errorResponse.getErrorMessage());
    }

    @Test
    void returnOrgServiceDetailsByCcdCaseTypeCode200() throws JsonProcessingException {

        final var responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByCcdCaseType("MONEYCLAIMCASE", LrdOrgInfoServiceResponse[].class);

        assertThat(responses).isNotEmpty().hasSize(1);
        responseVerification(responses);
    }

    @Test
    void returnOrgServiceDetailsByCcdServiceName200() throws JsonProcessingException {

        final var responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByCcdServiceName("CMC", LrdOrgInfoServiceResponse[].class);

        assertThat(responses).isNotEmpty().hasSize(1);
        responseVerification(responses);
    }

    @Test
    void returnOrgServiceDetailsByMultipleCcdServiceNames200() throws JsonProcessingException {

        final var responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByCcdServiceName("CMC,CCDSERVICENAME2",
                                                               LrdOrgInfoServiceResponse[].class);

        assertThat(responses).isNotEmpty().hasSize(2);
    }

    @Test
    void returnOrgServiceDetailsByCcdServiceNameAll200() throws JsonProcessingException {

        final var responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByCcdServiceName("ALL", LrdOrgInfoServiceResponse[].class);

        assertThat(responses).isNotEmpty().hasSize(4);
    }

    @Test
    void returnOrgServiceDetailsByCcdServiceNameNotFound404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap  = (Map<String, Object>)
            lrdApiClient.findOrgServiceDetailsByCcdServiceName("someRandomServiceName", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS,HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), errorResponse.getErrorMessage());
    }

    @Test
    void returnOrgServiceDetailsByCcdCaseTypeIgnoreCaseCode200() throws JsonProcessingException {

        final var responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByCcdCaseType(" moneyCLAIMCASE ",LrdOrgInfoServiceResponse[].class);

        assertThat(responses).isNotEmpty().hasSize(1);
        responseVerification(responses);
    }

    @Test
    void doNotReturnOrgServiceDetailsByUnknownCcdCaseTypeCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findOrgServiceDetailsByCcdCaseType("ccCaseType1", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS,HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), errorResponse.getErrorMessage());
    }

    @Test
    void returnsOrgServiceDetailsWithoutInputParams_200() throws JsonProcessingException {

        final var responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByDefaultAll(LrdOrgInfoServiceResponse[].class);
        assertThat(responses).isNotEmpty().hasSize(3);
    }

    @Test
    void returns_LaunchDarkly_Forbidden_when_delete_minimal_pending_organisation_with_invalid_flag()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdApiController.retrieveOrgServiceDetails",
            "lrd-disable-retrieve-org"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findOrgServiceDetailsByCcdCaseType("ccCaseType1", ErrorResponse.class);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd-disable-retrieve-org".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    private void responseVerification(List<LrdOrgInfoServiceResponse> responses) {

        responses.forEach(response -> {
            assertThat(response.getServiceId()).isEqualTo(3);
            assertThat(response.getBusinessArea()).isEqualToIgnoringCase("Civil, Family and Tribunals");
            assertThat(response.getOrgUnit()).isEqualToIgnoringCase("HMCTS");
            assertThat(response.getCcdServiceName()).isEqualToIgnoringCase("CMC");
            assertThat(response.getServiceCode()).isEqualTo("AAA6");
            assertThat(response.getJurisdiction()).isEqualToIgnoringCase("Civil");
            assertThat(response.getLastUpdate()).isNotNull();
            assertThat(response.getServiceDescription()).isEqualToIgnoringCase("Specified Money Claims");
            assertThat(response.getServiceShortDescription()).isEqualToIgnoringCase("Specified Money Claims");
            assertThat(response.getSubBusinessArea()).isEqualToIgnoringCase("Civil and Family");
            assertThat(response.getCcdCaseTypes().size()).isEqualTo(2);

        });

    }

    @Test
    void returnOrgServiceDetailsByMultiCcdSerNamesSmallAllSpace200() throws JsonProcessingException {

        List<LrdOrgInfoServiceResponse> responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByCcdServiceName(" aLL ", LrdOrgInfoServiceResponse[].class);

        assertThat(responses).isNotEmpty().hasSize(3);;
    }

    @Test
    void returnOrgServiceDetailsByMultiCcdSerNamesAllOther200() throws JsonProcessingException {

        List<LrdOrgInfoServiceResponse> responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.findOrgServiceDetailsByCcdServiceName("All, CMC",
                                                               LrdOrgInfoServiceResponse[].class);

        assertThat(responses).isNotEmpty().hasSize(3);
    }

    @Test
    void returnOrgServiceDetailsByMultiCcdSerNamesTwoCommaBadRequest400() throws JsonProcessingException {

        Map<String, Object> errorResponseMap  = (Map<String, Object>)
            lrdApiClient.findOrgServiceDetailsByCcdServiceName("CMC, ,Divorce", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS,HttpStatus.BAD_REQUEST);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals(EXCEPTION_MSG_SPCL_CHAR, errorResponse.getErrorDescription());
    }

    @Test
    void returnOrgServiceDetailsByMultiCcdSerNamesSpclChaBadRequest400() throws JsonProcessingException {

        Map<String, Object> errorResponseMap  = (Map<String, Object>)
            lrdApiClient.findOrgServiceDetailsByCcdServiceName(", &", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS,HttpStatus.BAD_REQUEST);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals(EXCEPTION_MSG_SPCL_CHAR, errorResponse.getErrorDescription());
    }
}
