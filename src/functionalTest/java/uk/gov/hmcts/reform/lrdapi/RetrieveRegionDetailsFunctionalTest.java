package uk.gov.hmcts.reform.lrdapi;

import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.util.CustomSerenityRunner;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.lrdapi.util.CustomSerenityRunner.getFeatureFlagName;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@RunWith(CustomSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
public class RetrieveRegionDetailsFunctionalTest extends AuthorizationFunctionalTest {

    public static final String mapKey = "LrdApiController.retrieveRegionDetails";

    List<LrdRegionResponse> expectedListAll = List.of(
        new LrdRegionResponse(new Region("1", "National", null)),
        new LrdRegionResponse(new Region("2", "London", null)),
        new LrdRegionResponse(new Region("3", "Midlands", null)),
        new LrdRegionResponse(new Region("4", "North East", null)),
        new LrdRegionResponse(new Region("5", "North West", null)),
        new LrdRegionResponse(new Region("6", "South East", null)),
        new LrdRegionResponse(new Region("7", "South West", null)),
        new LrdRegionResponse(new Region("8", "Wales", null)),
        new LrdRegionResponse(new Region("9", "Scotland", null))
    );

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    public void getRegionDetailsByDescriptionWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionDescription(HttpStatus.OK, "London");

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    public void getRegionDetailsByDescriptionsWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionDescription(HttpStatus.OK, "London, Midlands");

        assertThat(response).isNotNull();
        responseVerification(response, 2);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    public void getRegionDetailsByDescriptionWithTrailingSpacesWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionDescription(HttpStatus.OK, " London  ");

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    public void getRegionDetailsByIdWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionId(HttpStatus.OK, "2");

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    public void getRegionDetailsByIdsWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionId(HttpStatus.OK, "2,3");

        assertThat(response).isNotNull();
        responseVerification(response, 2);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    public void getRegionDetailsByIdAllWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionId(HttpStatus.OK, "ALL");

        assertThat(response).isNotNull();
        responseVerificationForAll(response, expectedListAll);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void should_retrieve_403_when_Api_toggled_off() {
        String exceptionMessage = getFeatureFlagName().concat(" ").concat(FORBIDDEN_EXCEPTION_LD);
        validateErrorResponse(
            (ErrorResponse) lrdApiClient.retrieveRegionInfoByRegionDescription(HttpStatus.FORBIDDEN, ""),
            exceptionMessage, exceptionMessage
        );
    }

    private void responseVerification(List<LrdRegionResponse> response, int expectedRegions) {
        assertThat(response.size()).isEqualTo(expectedRegions);
        assertThat(response.get(0).getRegionId()).isEqualTo("2");
        assertThat(response.get(0).getDescription()).isEqualTo("London");
        assertThat(response.get(0).getWelshDescription()).isNull();
        if (expectedRegions == 2) {
            assertThat(response.get(1).getRegionId()).isEqualTo("3");
            assertThat(response.get(1).getDescription()).isEqualTo("Midlands");
            assertThat(response.get(1).getWelshDescription()).isNull();
        }
    }

    private void responseVerificationForAll(List<LrdRegionResponse> actual, List<LrdRegionResponse> expected) {
        assertThat(actual).hasSize(expected.size()).usingRecursiveComparison().isEqualTo(expected);
    }
}
