package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.EMPTY_RESULT_DATA_ACCESS;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.UNKNOWN_EXCEPTION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureConditionEvaluation.FORBIDDEN_EXCEPTION_LD;

@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
class RetrieveCourtVenuesByServiceCodeIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    public static final String HTTP_STATUS = "http_status";
    private static final String V2_PATH = "/v2/court-venues/services";
    private static final String MISSING_SERVICE_CODE_MESSAGE =
        "Required request parameter 'service_code' for method parameter type String is not present";

    @ParameterizedTest
    @ValueSource(strings = {"AAA3","aaa3"})
    void returnsCourtVenuesByServiceCodeWithStatusCode_200(String serviceCode)
        throws JsonProcessingException, JSONException {

        final var response = (LrdCourtVenuesByServiceCodeResponse)
            lrdApiClient.findCourtVenuesByServiceCode(serviceCode, LrdCourtVenuesByServiceCodeResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response);
    }

    @Test
    void returnsCourtVenuesByUnknownServiceCodeWithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesByServiceCode("ABCD1", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals("No court venues found for the given service code ABCD1",
                     errorResponse.getErrorDescription());
    }

    @Test
    void returnsCourtVenuesByInvalidServiceCodeWithStatusCode_400() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesByServiceCode("@$ABC", ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals(EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR, errorResponse.getErrorDescription());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void returnsCourtVenuesByBlankServiceCodeWithStatusCode_500(String serviceCode) throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesByServiceCode(serviceCode, ErrorResponse.class);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(UNKNOWN_EXCEPTION.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals("400 BAD_REQUEST \"Validation failure\"", errorResponse.getErrorDescription());
    }

    @ParameterizedTest
    @ValueSource(strings = {"AAA6","aaa6"})
    void returnsCourtVenuesByServiceCodeV2WithStatusCode_200(String serviceCode)
        throws JsonProcessingException {

        final var response = (List<LrdCourtVenueV2Response>)
            lrdApiClient.retrieveCourtVenueV2ResponseForGivenRequest("?service_code=" + serviceCode,
                                                                     LrdCourtVenueV2Response[].class, V2_PATH);
        assertThat(response).isNotEmpty();
        assertThat(response).extracting(LrdCourtVenueV2Response::getServiceCode).contains("AAA6");
        assertThat(response).extracting(LrdCourtVenueV2Response::getMrdVenueId)
            .contains("MRD-3", "MRD-7", "MRD-11", "MRD-123456789");

        final var enrichedVenue = response.stream()
            .filter(venue -> "MRD-123456789".equals(venue.getMrdVenueId()))
            .findFirst()
            .orElseThrow();

        assertThat(enrichedVenue.getEpimsId()).isEqualTo("123463");
        assertThat(enrichedVenue.getMrdBuildingId()).isEqualTo("MRD-BLD-385");
        assertThat(enrichedVenue.getCourtStatus()).isEqualTo("OPEN");
        assertThat(enrichedVenue.getLocationType()).isEqualTo("CTSC");
        assertThat(enrichedVenue.getRegion()).isEqualTo("4");
        assertThat(enrichedVenue.getCluster()).isEqualTo("1");
        assertThat(enrichedVenue.getVenueOuCode()).isEqualTo("VOU123456");
        assertThat(enrichedVenue.getServiceUrl()).isEqualTo("https://service.example.gov.uk");
        assertThat(enrichedVenue.getFactUrl()).isEqualTo("https://fact.example.gov.uk");

        assertThat(enrichedVenue.getCourtUse().isHearingLocation()).isTrue();
        assertThat(enrichedVenue.getCourtUse().isCaseManagementLocation()).isTrue();
        assertThat(enrichedVenue.getCourtUse().isTemporaryLocation()).isFalse();
        assertThat(enrichedVenue.getCourtUse().isNightingaleCourt()).isFalse();
        assertThat(enrichedVenue.getCourtUse().isDistrictRegistry()).isFalse();
        assertThat(enrichedVenue.getCourtUse().isAppealCentre()).isFalse();

        assertThat(enrichedVenue.getNames()).hasSize(3);
        assertThat(enrichedVenue.getNames()).extracting(
            LrdCourtVenueV2Response.Name::getType,
            LrdCourtVenueV2Response.Name::getLanguage,
            LrdCourtVenueV2Response.Name::getName
        ).containsExactlyInAnyOrder(
            tuple("SITE", "EN", "Aberdeen Tribunal Hearing Centre 7"),
            tuple("COURT", "EN", "ABERDEEN TRIBUNAL HEARING CENTRE 23"),
            tuple("COURT", "CY", "Llys Sirol Caerdydd")
        );

        assertThat(enrichedVenue.getAddresses()).hasSize(1);
        assertThat(enrichedVenue.getAddresses().get(0).getType()).isEqualTo("MAILING");
        assertThat(enrichedVenue.getAddresses().get(0).getAddress()).isEqualTo("AB7, 54 HUNTLY STREET, ABERDEEN");
        assertThat(enrichedVenue.getAddresses().get(0).getPostCode()).isEqualTo("AB11 1TY");
        assertThat(enrichedVenue.getAddresses().get(0).getUprn()).isEqualTo("100070123456");
        assertThat(enrichedVenue.getAddresses().get(0).getLanguage()).isEqualTo("EN");

        assertThat(enrichedVenue.getContacts()).hasSize(2);
        assertThat(enrichedVenue.getContacts()).extracting(
            LrdCourtVenueV2Response.Contact::getMethod,
            LrdCourtVenueV2Response.Contact::getType,
            LrdCourtVenueV2Response.Contact::getValue
        ).containsExactlyInAnyOrder(
            tuple("EMAIL", "CONTACT_SERVICE", "contact@example.gov.uk"),
            tuple("EMAIL", "BREATHING_SPACE", "breathing.space@example.gov.uk")
        );
    }

    @Test
    void returnsCourtVenuesByUnknownServiceCodeV2WithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveCourtVenueV2ResponseForGivenRequest("?service_code=53453",
                                                                     ErrorResponse.class, V2_PATH);
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals("No court venues found for the given service code 53453",
                     errorResponse.getErrorDescription());
    }

    @Test
    void returnsCourtVenuesByNullLiteralServiceCodeV2WithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveCourtVenueV2ResponseForGivenRequest("?service_code=null",
                                                                     ErrorResponse.class, V2_PATH);
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals("No court venues found for the given service code null",
                     errorResponse.getErrorDescription());
    }

    @Test
    void returnsCourtVenuesBySpecialCharacterServiceCodeV2WithStatusCode_400() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveCourtVenueV2ResponseForGivenRequest("?service_code=@$ABC",
                                                                     ErrorResponse.class, V2_PATH);
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(MISSING_SERVICE_CODE_MESSAGE, errorResponse.getErrorMessage());
        assertEquals(MISSING_SERVICE_CODE_MESSAGE, errorResponse.getErrorDescription());
    }

    @Test
    void returnsCourtVenuesByMissingServiceCodeV2WithStatusCode_400() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveCourtVenueV2ResponseForGivenRequest("", ErrorResponse.class, V2_PATH);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
    }

    @Test
    void returnsCourtVenuesByBlankServiceCodeV2WithStatusCode_500() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveCourtVenueV2ResponseForGivenRequest("?service_code=",
                                                                     ErrorResponse.class, V2_PATH);
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertEquals(UNKNOWN_EXCEPTION.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals("400 BAD_REQUEST \"Validation failure\"", errorResponse.getErrorDescription());
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

    private void responseVerification(LrdCourtVenuesByServiceCodeResponse response)
        throws JSONException, JsonProcessingException {
        List<LrdCourtVenueResponse> expectedCourtVenueResponses = buildCourtVenueResponses();

        assertThat(response.getServiceCode()).isEqualTo("AAA3");
        assertThat(response.getCourtTypeId()).isEqualTo("17");
        assertThat(response.getCourtType()).isEqualTo("Employment Tribunal");
        assertThat(response.getWelshCourtType()).isNull();
        ObjectMapper objectMapper = new ObjectMapper();
        String actual = objectMapper.writeValueAsString(response.getCourtVenues());
        String expected = objectMapper.writeValueAsString(expectedCourtVenueResponses);
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    private List<LrdCourtVenueResponse> buildCourtVenueResponses() {
        List<LrdCourtVenueResponse> expectedCourtVenueResponses = new ArrayList<>();
        LrdCourtVenueResponse response1 = LrdCourtVenueResponse.builder()
            .courtVenueId("2")
            .siteName("Aberdeen Tribunal Hearing Centre 2")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 2")
            .epimmsId("123456788")
            .openForPublic("YES")
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .regionId("1")
            .region("London")
            .clusterId("1")
            .clusterName("Avon, Somerset and Gloucestershire")
            .courtStatus("Open")
            .postcode("AB11 7KT")
            .courtAddress("AB2, 49 HUNTLY STREET, ABERDEEN")
            .mrdVenueId("MRD-2")
            .serviceCode("AAA3")
            .build();

        LrdCourtVenueResponse response2 = LrdCourtVenueResponse.builder()
            .courtVenueId("6")
            .siteName("Aberdeen Tribunal Hearing Centre 6")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 6")
            .epimmsId("epimmsId1236")
            .openForPublic("YES")
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .regionId("2")
            .region("Midlands")
            .courtStatus("Open")
            .postcode("AB11 7GQ")
            .courtAddress("AB6, 53 HUNTLY STREET, ABERDEEN")
            .mrdVenueId("MRD-6")
            .serviceCode("AAA3")
            .build();

        LrdCourtVenueResponse response3 = LrdCourtVenueResponse.builder()
            .courtVenueId("10")
            .siteName("Aberdeen Tribunal Hearing Centre 10")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 10")
            .epimmsId("123458")
            .openForPublic("YES")
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .regionId("1")
            .region("London")
            .clusterId("2")
            .clusterName("Bedfordshire, Cambridgeshire, Hertfordshire")
            .courtStatus("Open")
            .postcode("AB11 5QA")
            .courtAddress("AB10, 57 HUNTLY STREET, ABERDEEN")
            .isCaseManagementLocation("Y")
            .isHearingLocation("Y")
            .locationType("CTSC")
            .isTemporaryLocation("Y")
            .mrdVenueId("MRD-10")
            .serviceCode("AAA3")
            .build();

        LrdCourtVenueResponse response4 = LrdCourtVenueResponse.builder()
            .courtVenueId("14")
            .siteName("Aberdeen Tribunal Hearing Centre 7")
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 22")
            .epimmsId("123462")
            .openForPublic("YES")
            .courtTypeId("17")
            .courtType("Employment Tribunal")
            .regionId("3")
            .region("North East")
            .clusterId("1")
            .clusterName("Avon, Somerset and Gloucestershire")
            .courtStatus("Open")
            .postcode("AB11 1TY")
            .courtAddress("AB7, 54 HUNTLY STREET, ABERDEEN")
            .serviceCode("AAA3")
            .externalShortName("Aberdeen Tribunal External")
            .welshExternalShortName("Welsh External Short Name")
            .mrdVenueId("MRD-14")
            .build();

        expectedCourtVenueResponses.add(response1);
        expectedCourtVenueResponses.add(response2);
        expectedCourtVenueResponses.add(response3);
        expectedCourtVenueResponses.add(response4);

        return expectedCourtVenueResponses;
    }

}
