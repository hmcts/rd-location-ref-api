package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lib.util.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.EMPTY_RESULT_DATA_ACCESS;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension.getToggledOffMessage;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
class RetrieveOrgServiceDetailsFunctionalTest extends AuthorizationFunctionalTest {

    public static final String mapKey = "LrdApiController.retrieveOrgServiceDetails";

    @SuppressWarnings("unchecked")
    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void returnsOrgServiceDetailsByServiceCodeWithStatusCode_200() throws JsonProcessingException {
        List<LrdOrgInfoServiceResponse> responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.retrieveOrgServiceInfo(HttpStatus.OK, "?serviceCode=AAA6");
        assertThat(responses.size()).isPositive();
        assertTrue(responses.stream().allMatch(service -> service.getServiceCode().equals("AAA6")));
    }

    @SuppressWarnings("unchecked")
    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void returnsOrgServiceDetailsByCcdCaseTypeWithStatusCode_200() throws JsonProcessingException {
        List<LrdOrgInfoServiceResponse> responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.retrieveOrgServiceInfo(
                HttpStatus.OK,
                "?ccdCaseType=MoneyClaimCase"
            );

        assertThat(responses.size()).isPositive();
        var ccdCaseTypes = responses
            .stream()
            .flatMap(service -> service.getCcdCaseTypes().stream())
            .collect(Collectors.toList());
        assertTrue(ccdCaseTypes.stream().anyMatch("MoneyClaimCase"::equalsIgnoreCase));
        responseVerification(responses);
    }

    @SuppressWarnings("unchecked")
    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void returnsOrgServiceDetailsByDefaultAll_200() throws JsonProcessingException {

        List<LrdOrgInfoServiceResponse> responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.retrieveOrgServiceInfo(HttpStatus.OK, "");

        assertThat(responses).isNotEmpty().hasSize(92);
    }

    @SuppressWarnings("unchecked")
    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void returnsOrgServiceDetailsByServiceNameStatusCode_200() throws JsonProcessingException {
        //ServiceName is generated from the ServiceToCcdCaseTypeAssoc
        List<LrdOrgInfoServiceResponse> responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.retrieveOrgServiceInfo(HttpStatus.OK, "?serviceCode=AAA6");
        assertThat(responses.size()).isPositive();
        assertTrue(responses.stream().allMatch(service -> service.getServiceCode().equals("AAA6")));
    }

    @Test
    void returnOrgServiceDetailsByUnknownCcdCaseTypeCode_404() throws JsonProcessingException {

        ErrorResponse errorResponse = (ErrorResponse)
            lrdApiClient.retrieveOrgServiceInfo(HttpStatus.NOT_FOUND,"?ccdCaseType=ccCaseType1");

        assertNotNull(errorResponse);
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), errorResponse.getErrorMessage());
    }

    @Test
    @ExtendWith(FeatureToggleConditionExtension.class)
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    void should_retrieve_403_when_Api_toggled_off() {
        String exceptionMessage = getToggledOffMessage();
        validateErrorResponse(
            (ErrorResponse) lrdApiClient.retrieveOrgServiceInfo(
                HttpStatus.FORBIDDEN, ""),
            exceptionMessage,
            exceptionMessage
        );
    }

    private void responseVerification(List<LrdOrgInfoServiceResponse> responses) throws JsonProcessingException {

        responses.forEach(response -> {
            assertThat(response.getServiceId()).isPositive();
            assertThat(response.getBusinessArea()).isEqualToIgnoringCase("Civil, Family and Tribunals");
            assertThat(response.getOrgUnit()).isEqualToIgnoringCase("HMCTS");
            assertNotNull(response.getCcdServiceName());
            assertNotNull(response.getServiceCode());
            assertThat(response.getJurisdiction()).isEqualToIgnoringCase("Civil");
            assertNotNull(response.getLastUpdate());
            assertNotNull(response.getServiceDescription());
            assertNotNull(response.getServiceShortDescription());
            assertThat(response.getSubBusinessArea()).isEqualToIgnoringCase("Civil and Family");
            assertThat(response.getCcdCaseTypes().size()).isPositive();

        });
    }
}
