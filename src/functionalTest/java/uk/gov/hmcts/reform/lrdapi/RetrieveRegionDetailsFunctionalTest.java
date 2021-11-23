package uk.gov.hmcts.reform.lrdapi;

import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension.getToggledOffMessage;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
class RetrieveRegionDetailsFunctionalTest extends AuthorizationFunctionalTest {

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
    void getRegionDetailsByDescriptionWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionDescription(HttpStatus.OK, "London");

        assertNotNull(response);
        responseVerification(response, 1);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    void getRegionDetailsByDescriptionsWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionDescription(HttpStatus.OK, "London, Midlands");

        assertNotNull(response);
        responseVerification(response, 2);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    void getRegionDetailsByDescriptionWithTrailingSpacesWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionDescription(HttpStatus.OK, " London  ");

        assertNotNull(response);
        responseVerification(response, 1);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    void getRegionDetailsByIdWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionId(HttpStatus.OK, "2");

        assertNotNull(response);
        responseVerification(response, 1);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    void getRegionDetailsByIdsWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionId(HttpStatus.OK, "2,3");

        assertNotNull(response);
        responseVerification(response, 2);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    @SuppressWarnings("unchecked")
    void getRegionDetailsByIdAllWithStatusCode_200() {
        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.retrieveRegionInfoByRegionId(HttpStatus.OK, "ALL");

        assertNotNull(response);
        responseVerificationForAll(response, expectedListAll);
    }

    @Test
    @ExtendWith(FeatureToggleConditionExtension.class)
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    void should_retrieve_403_when_Api_toggled_off() {
        String exceptionMessage = getToggledOffMessage();
        validateErrorResponse(
            (ErrorResponse) lrdApiClient.retrieveRegionInfoByRegionDescription(HttpStatus.FORBIDDEN, ""),
            exceptionMessage, exceptionMessage
        );
    }

    private void responseVerification(List<LrdRegionResponse> response, int expectedRegions) {
        assertEquals(expectedRegions, response.size());
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertNull(response.get(0).getWelshDescription());
        if (expectedRegions == 2) {
            assertEquals("3", response.get(1).getRegionId());
            assertEquals("Midlands", response.get(1).getDescription());
            assertNull(response.get(1).getWelshDescription());
        }
    }

    private void responseVerificationForAll(List<LrdRegionResponse> actual, List<LrdRegionResponse> expected) {
        assertThat(actual).hasSize(expected.size()).usingRecursiveComparison().isEqualTo(expected);
    }
}
