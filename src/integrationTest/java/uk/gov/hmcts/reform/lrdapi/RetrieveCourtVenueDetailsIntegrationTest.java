package uk.gov.hmcts.reform.lrdapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.EMPTY_RESULT_DATA_ACCESS;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.INVALID_REQUEST_EXCEPTION;

@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
class RetrieveCourtVenueDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {
    private static final String HTTP_STATUS_STR = "http_status";
    private static final String path = "/court-venues";

    @ParameterizedTest
    @ValueSource(strings = {"123456789", " 123456789 ", "123456789, *"})
    @SuppressWarnings("unchecked")
    void retrieveCourtVenues_WithEpimmsIdGiven_ShouldReturnValidResponseAndStatusCode200(String id) throws
        JsonProcessingException {

        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest("?epimms_id=" + id,
                                                         LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty().hasSize(3);
        assertTrue(response.stream().allMatch(venue -> venue.getEpimmsId().equals("123456789")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ALL", "All", " All ", "all", "aLL", "AlL", "alL", "aLl", "ALL,123456"})
    @SuppressWarnings("unchecked")
    void retrieveCourtVenues_AllepimmsIdGiven_ShouldReturnValidResponseAndStatusCode200(String parameter) throws
        JsonProcessingException {

        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest("?epimms_id=" + parameter,
                                                         LrdCourtVenueResponse[].class, path
            );

        assertThat(response).isNotEmpty().hasSize(11);
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveCourtVenues_NoEpimmsIdGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest("?epimms_id=",
                                                         LrdCourtVenueResponse[].class, path
            );

        assertThat(response).isNotEmpty().hasSize(11);;
        assertTrue(response.stream().allMatch(venue -> venue.getCourtStatus().equalsIgnoreCase("Open")));
    }


    @ParameterizedTest
    @ValueSource(strings = {"-1111", "!@£@$", "*", ",,", "123456789,,"})
    @SuppressWarnings("unchecked")
    void shouldReturn400ForInvalidEpimmsId(String epimmsId) throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=" + epimmsId, ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
        ErrorResponse errorResponse = ((ErrorResponse) errorResponseMap.get("response_body"));
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), errorResponse.getErrorMessage());
        assertThat(errorResponse.getErrorDescription()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"17", " 17 "})
    @SuppressWarnings("unchecked")
    void retrieveCourtVenues_WithCourtTypeIdGiven_ShouldReturnValidResponseAndStatusCode200(String id) throws
        JsonProcessingException {

        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest("?court_type_id=" + id,
                                                                   LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty().hasSize(9);
        assertTrue(response.stream().allMatch(venue -> venue.getCourtTypeId().equals(id.trim())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", " 1 "})
    @SuppressWarnings("unchecked")
    void retrieveCourtVenues_WithRegionIdGiven_ShouldReturnValidResponseAndStatusCode200(String id) throws
        JsonProcessingException {

        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest("?region_id=" + id,
                                                                   LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty().hasSize(7);
        assertTrue(response.stream().allMatch(venue -> venue.getRegionId().equals(id.trim())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2", " 2 "})
    @SuppressWarnings("unchecked")
    void retrieveCourtVenues_WithClusterIdGiven_ShouldReturnValidResponseAndStatusCode200(String id) throws
        JsonProcessingException {

        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest("?cluster_id=" + id,
                                                                   LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty().hasSize(4);
        assertTrue(response.stream().allMatch(venue -> venue.getClusterId().equals(id.trim())));
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveCourtVenues_WithCourtVenueNameGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?court_venue_name=ABERDEEN TRIBUNAL HEARING CENTRE 1", LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty().hasSize(1);
        assertTrue(response.stream().allMatch(venue -> venue.getCourtName()
            .equals("ABERDEEN TRIBUNAL HEARING CENTRE 1")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"?region_id=12x!", "?cluster_id=@£$", "?court_type_id=@_court_type",
         "?epimms_id=123456789&court_type_id=1&cluster_id=1"})
    void shouldReturn400WhenInvalidParamsPassed(String parameter) throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest(parameter, ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturn200WhenParameterEpmIdsValueAllWithYPassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=Y&is_case_management_location=Y&location_type=CTSC"
                    + "&is_temporary_location=Y",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(1);
        assertTrue(response.stream().allMatch(venue ->
            venue.getIsHearingLocation().equalsIgnoreCase("Y")
            && venue.getIsCaseManagementLocation().equalsIgnoreCase("Y")
            && venue.getLocationType().equalsIgnoreCase("CTSC")
            && venue.getIsTemporaryLocation().equalsIgnoreCase("Y")));
    }

    @Test
    void shouldReturn200WhenParameterEpmIdsValueAllWithLowerCaseValuesPassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=y&is_case_management_location=y&location_type=CTSC"
                    + "&is_temporary_location=y",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(1);
        assertTrue(response.stream().allMatch(venue ->
            venue.getIsHearingLocation().equalsIgnoreCase("Y")
            && venue.getIsCaseManagementLocation().equalsIgnoreCase("Y")
            && venue.getLocationType().equalsIgnoreCase("CTSC")
            && venue.getIsTemporaryLocation().equalsIgnoreCase("Y")));
    }

    @Test
    void shouldReturn200WhenParameterEpmIdsValueAllWithNAndLocationTypeNbcPassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=N&is_case_management_location=N&location_type=NBC"
                    + "&is_temporary_location=N",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(1);
        assertTrue(response.stream().allMatch(venue ->
            venue.getIsHearingLocation().equalsIgnoreCase("N")
            && venue.getIsCaseManagementLocation().equalsIgnoreCase("N")
            && venue.getLocationType().equalsIgnoreCase("NBC")
            && venue.getIsTemporaryLocation().equalsIgnoreCase("N")));
    }

    @Test
    void shouldReturn200WhenParameterEpmIdsValueAllWithHearingLocationYPassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=Y",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(2);
        assertTrue(response.stream().allMatch(venue ->
             venue.getIsHearingLocation().equalsIgnoreCase("Y")));
    }

    @Test
    void shouldReturn200WhenParameterCourtTypeIdIsPassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?court_type_id=17",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(9);
        assertTrue(response.stream().allMatch(venue -> venue.getCourtTypeId().equals("17")));
    }

    @Test
    void shouldReturn200WhenParameterRegionIdIsPassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?region_id=1",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(7);
        assertTrue(response.stream().allMatch(venue -> venue.getRegionId().equals("1")));
    }

    @Test
    void shouldReturn200WhenParameterClusterIdIsPassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?cluster_id=2",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(4);
        assertTrue(response.stream().allMatch(venue -> venue.getClusterId().equals("2")));
    }

    @Test
    void shouldReturn200WhenParameterCourtVenueNameIsPassed() throws
        JsonProcessingException {
        //Its siteName or courtName

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?court_venue_name=ABERDEEN TRIBUNAL HEARING CENTRE 10",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(1);
        assertTrue(response.stream().allMatch(venue -> venue.getCourtName()
            .equals("ABERDEEN TRIBUNAL HEARING CENTRE 10")));
    }

    @Test
    void shouldReturn404WhenCourtVenueResponseIsEmpty() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=N&is_case_management_location=N&location_type=CCBC"
                    + "&is_temporary_Location=N",
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.NOT_FOUND);
        ErrorResponse errorResponse = ((ErrorResponse) errorResponseMap.get("response_body"));
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), errorResponse.getErrorMessage());
        assertEquals("There are no court venues found", errorResponse.getErrorDescription());
    }

    @Test
    void shouldReturn200WhenNoQueryParameterIsPassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(null, LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty().hasSize(11);

    }

    @Test
    void shouldReturn200WhenParameterEpmIdsValueAllWithYAndSpacePassed() throws
        JsonProcessingException {

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=    Y&is_case_management_location=Y    "
                    + "&location_type=    CTSC    "
                    + "&is_temporary_location=Y    ",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(1);
    }
}
