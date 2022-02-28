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
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id="+ epimmsId, ErrorResponse.class, path);

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
        assertTrue(response.stream().allMatch(v -> v.getCourtTypeId().equals(id.trim())));
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
        assertTrue(response.stream().allMatch(v -> v.getRegionId().equals(id.trim())));
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
        assertTrue(response.stream().allMatch(v -> v.getClusterId().equals(id.trim())));
    }

    @Test
    @SuppressWarnings("unchecked")
    void retrieveCourtVenues_WithCourtVenueNameGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?court_venue_name=ABERDEEN TRIBUNAL HEARING CENTRE 1", LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty().hasSize(1);
        assertTrue(response.stream().allMatch(v -> v.getCourtName().equals("ABERDEEN TRIBUNAL HEARING CENTRE 1")));
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

}
