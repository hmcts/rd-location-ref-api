package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.junit5.SerenityTest;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SerenityTest
@WithTags({@WithTag("testType:Integration")})
class RetrieveCourtVenuesBySearchStringIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {
    private static final String path = "/court-venues/venue-search";
    private static final String HTTP_STATUS_STR = "http_status";

    @Test
    void shouldRetrieveCourtVenues_For_SearchString_WithStatusCode_200()
        throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe",
                LrdCourtVenueResponse[].class,
                path
            );
        assertThat(response).isNotEmpty();
        var courtVenueResponse = new ArrayList<>(Arrays.asList(response));
        var courtNameVerified = courtVenueResponse
            .stream()
            .filter(venue -> venue.getCourtName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getSiteName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getCourtAddress().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getPostcode().strip().toLowerCase().contains("Abe".toLowerCase()))
            .collect(Collectors.toList());
        courtVenueResponse.removeAll(courtNameVerified);

        assertTrue(courtVenueResponse.isEmpty());
    }

    @Test
    void shouldRetrieveCourtVenues_For_SearchString_And_CourtTypeId_WithStatusCode_200()
        throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&court-type-id=17,10,23",
                LrdCourtVenueResponse[].class,
                path
            );
        assertThat(response).isNotEmpty();
        var courtVenueResponse = new ArrayList<>(Arrays.asList(response));
        var courtNameVerified = courtVenueResponse
            .stream()
            .filter(venue -> venue.getCourtName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getSiteName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getCourtAddress().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getPostcode().strip().toLowerCase().contains("Abe".toLowerCase()))
            .collect(Collectors.toList());
        courtVenueResponse.removeAll(courtNameVerified);

        assertTrue(courtVenueResponse.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenSearchStringLessThan3Char() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString("?search-string=zz&court-type-id=1000",
                                                       ErrorResponse.class, path
            );
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenSearchStringContainSpecialChar() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=AB$&court-type-id=1000",
                ErrorResponse.class,
                path
            );
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenCourtTypeIdContainContainSpecialChar() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=AB$&court-type-id=1,2,$,4",
                ErrorResponse.class,
                path
            );
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnEmptyList_WhenCourtTypeIdIsInvalid() throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000",
                LrdCourtVenueResponse[].class,
                path
            );
        assertEquals(0,response.length);
    }
}
