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
public class GetBuildingLocationIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    @Test
    public void retrieveBuildLocations_ValidEpimmsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        LrdBuildingLocationResponse response = (LrdBuildingLocationResponse)
            lrdApiClient.findBuildingLocationByEpimmId("123456789", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response);
    }

    @Test
    public void getBuildLocations_ValidEpimmsIdGivenWithLeadingAndTrailingSpace_ReturnValidResponseAndStatusCode200()
        throws JsonProcessingException {

        LrdBuildingLocationResponse response = (LrdBuildingLocationResponse)
            lrdApiClient.findBuildingLocationByEpimmId(" 123456789 ", LrdBuildingLocationResponse.class);

        assertNotNull(response);
        responseVerification(response);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_StringEpimmsIdGiven_ShouldReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("QWERTY", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveBuildLocations_NonExistingEpimmsIdGiven_ShouldReturnErrorResponseAndStatusCode400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findBuildingLocationByEpimmId("-1111", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status",HttpStatus.BAD_REQUEST);
    }

    private void responseVerification(LrdBuildingLocationResponse response) {
        assertThat(response.getBuildingLocationId()).isEqualTo("22041995");
        assertThat(response.getRegionId()).isEqualTo("8910");
        assertThat(response.getClusterId()).isEqualTo("0123");
        assertThat(response.getBuildingLocationStatus()).isEqualTo("Status A");
        assertThat(response.getEpimmsId()).isEqualTo("123456789");
        assertThat(response.getBuildingLocationName()).isEqualTo("Building Location A");
        assertThat(response.getArea()).isEqualTo("Area A");
        assertThat(response.getPostcode()).isEqualTo("WX67 2YZ");
        assertThat(response.getAddress()).isEqualTo("1 Street, London");
        assertThat(response.getCourtFinderUrl()).isEqualTo("Court Finder URL");
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

}
