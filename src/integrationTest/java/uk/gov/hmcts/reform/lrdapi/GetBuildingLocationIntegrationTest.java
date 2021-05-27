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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
public class GetBuildingLocationIntegrationTest extends LrdAuthorizationEnabledIntegrationTest{

    @Test
    public void retrieveBuildLocations_ValidEPIMMSIDGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        LrdBuildingLocationResponse response = (LrdBuildingLocationResponse)
            lrdApiClient.findBuildingLocationByEPIMMId("815833", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response);
    }

    @Test
    public void retrieveBuildLocations_ValidEPIMMSIDGivenWithLeadingAndTrailingSpace_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        LrdBuildingLocationResponse response = (LrdBuildingLocationResponse)
            lrdApiClient.findBuildingLocationByEPIMMId(" 815833 ", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response);
    }

    @Test
    public void retrieveBuildLocations_NoEPIMMSIDGiven_ShouldReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        LrdBuildingLocationResponse response = (LrdBuildingLocationResponse)
            lrdApiClient.findBuildingLocationByEPIMMId("", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_StringEPIMMSIDGiven_ShouldReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEPIMMId("QWERTY", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_NonExistingEPIMMSIDGiven_ShouldReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEPIMMId("-1111", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    private void responseVerification(LrdBuildingLocationResponse response) {

    }

    @Test
    public void retrieveBuildLocations_LaunchDarklyFlagSetToOff_ShouldReturnErrorResponseWithStatusCode403()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdApiController.retrieveBuildingLocationDetailsByEpimsId",
            "lrd-disable-retrieve-org"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findOrgServiceDetailsByCcdCaseType("ccCaseType1", ErrorResponse.class);
        assertThat(errorResponseMap).containsEntry("http_status", HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd-disable-retrieve-org".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

}
