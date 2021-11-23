package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        assertEquals(1, responses.size());
        responseVerification(responses);
    }

    @SuppressWarnings("unchecked")
    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void returnsOrgServiceDetailsByCcdServiceNameWithStatusCode_200() {
        List<LrdOrgInfoServiceResponse> responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.retrieveOrgServiceInfo(
                HttpStatus.OK,
                "?ccdServiceNames=cMc"
            );
        assertEquals(1, responses.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void returnsOrgServiceDetailsByDefaultAll_200() throws JsonProcessingException {

        List<LrdOrgInfoServiceResponse> responses = (List<LrdOrgInfoServiceResponse>)
            lrdApiClient.retrieveOrgServiceInfo(HttpStatus.OK, "");

        assertThat(responses.size()).isPositive();
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
            assertEquals("AAA6", response.getServiceCode());
            assertThat(response.getJurisdiction()).isEqualToIgnoringCase("Civil");
            assertNotNull(response.getLastUpdate());
            assertThat(response.getServiceDescription()).isEqualToIgnoringCase("Specified Money Claims");
            assertThat(response.getServiceShortDescription()).isEqualToIgnoringCase("Specified Money Claims");
            assertThat(response.getSubBusinessArea()).isEqualToIgnoringCase("Civil and Family");
            assertThat(response.getCcdCaseTypes().size()).isPositive();

        });
    }
}
