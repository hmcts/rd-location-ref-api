package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
class RetrieveCourtVenuesByServiceCodeIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    public static final String HTTP_STATUS = "http_status";

    @Test
    void returnsCourtVenuesByServiceCodeWithStatusCode_200() throws JsonProcessingException {

        LrdCourtVenuesByServiceCodeResponse response = (LrdCourtVenuesByServiceCodeResponse)
            lrdApiClient.findCourtVenuesByServiceCode("AAA3", LrdCourtVenuesByServiceCodeResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response);
    }

    @Test
    void returnsCourtVenuesByServiceCodeCaseInsensitive_WithStatusCode_200() throws JsonProcessingException {

        LrdCourtVenuesByServiceCodeResponse response = (LrdCourtVenuesByServiceCodeResponse)
            lrdApiClient.findCourtVenuesByServiceCode("aaa3", LrdCourtVenuesByServiceCodeResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response);
    }

    @Test
    void returnsCourtVenuesByUnknownServiceCodeWithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesByServiceCode("ABCD1", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
    }

    @Test
    void returnsCourtVenuesByInvalidServiceCodeWithStatusCode_400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesByServiceCode("@$ABC", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry("http_status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void returnsCourtVenuesByServiceCode_LdFlagOff_WithStatusCode_403()
        throws Exception {
        Map<String, String> launchDarklyMap = new HashMap<>();
        launchDarklyMap.put(
            "LrdCourtVenueController.retrieveCourtVenuesByServiceCode",
            "lrd_location_api"
        );
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(false);
        when(featureToggleService.getLaunchDarklyMap()).thenReturn(launchDarklyMap);
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesByServiceCode("AAA3", ErrorResponse.class);
        assertThat(errorResponseMap).containsEntry("http_status", HttpStatus.FORBIDDEN);
        assertThat(((ErrorResponse) errorResponseMap.get("response_body")).getErrorMessage())
            .contains("lrd_location_api".concat(" ").concat(FORBIDDEN_EXCEPTION_LD));
    }

    private void responseVerification(LrdCourtVenuesByServiceCodeResponse response) {
        List<LrdCourtVenueResponse> expectedCourtVenueResponses = buildCourtVenueResponses();

        assertThat(response.getServiceCode()).isEqualTo("AAA3");
        assertThat(response.getCourtTypeId()).isEqualTo("10");
        assertThat(response.getCourtType()).isEqualTo("County Court");
        assertThat(response.getWelshCourtType()).isNull();
        assertThat(response.getCourtVenues()).isEqualTo(expectedCourtVenueResponses);
    }

    private List<LrdCourtVenueResponse> buildCourtVenueResponses() {
        List<LrdCourtVenueResponse> expectedCourtVenueResponses = new ArrayList<>();
        LrdCourtVenueResponse response1 = LrdCourtVenueResponse.builder()
            .courtVenueId("11")
            .siteName("Aberdeen Tribunal Hearing Centre 11")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 11")
            .epimmsId("123456")
            .openForPublic("YES")
            .courtTypeId("10")
            .courtType("County Court")
            .regionId("1")
            .region("National")
            .clusterId("2")
            .clusterName("Bedfordshire, Cambridgeshire, Hertfordshire")
            .courtStatus("Open")
            .postcode("AB11 5QA")
            .courtAddress("AB10, 57 HUNTLY STREET, ABERDEEN")
            .isCaseManagementLocation("N")
            .isHearingLocation("N")
            .locationType("NBC")
            .isTemporaryLocation("N")
            .build();

        expectedCourtVenueResponses.add(response1);

        return expectedCourtVenueResponses;
    }

}
