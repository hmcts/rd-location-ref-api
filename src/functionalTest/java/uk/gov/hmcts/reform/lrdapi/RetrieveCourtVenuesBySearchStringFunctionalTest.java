package uk.gov.hmcts.reform.lrdapi;

import io.restassured.response.Response;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.SEARCH_STRING_VALUE_ERROR_MESSAGE;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")

class RetrieveCourtVenuesBySearchStringFunctionalTest extends AuthorizationFunctionalTest {

    public static final String mapKey = "LrdCourtVenueController.retrieveCourtVenuesBySearchString";
    private static final String path = "/court-venues/venue-search";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldRetrieveCourtVenues_By_SearchString_WithStatusCode_200() {
        final var response = (LrdCourtVenueResponse[]) lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK,
                "?search-string=Abe", LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty();

        var courtVenueResponse = new ArrayList<>(Arrays.asList(response));
        var courtNameVerified = courtVenueResponse
            .stream()
            .filter(venue -> venue.getCourtName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getSiteName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getCourtAddress().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getPostcode().strip().toLowerCase().contains("Abe".toLowerCase()))
            .collect(Collectors.toList());

        assertTrue(courtNameVerified
                       .stream()
                       .allMatch(venue -> venue.getCourtStatus().equals("Open"))
        );
        assertThat(courtNameVerified).hasSize(3);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldRetrieveCourtVenues_By_CourtTypeIdAndSearchString_WithStatusCode_200() {
        final var response = (LrdCourtVenueResponse[]) lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK,
                        "?court-type-id=1&search-string=Abe", LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty();

        var courtVenueResponse = new ArrayList<>(Arrays.asList(response));
        assertTrue(courtVenueResponse.stream().allMatch(venue -> venue.getCourtTypeId().equals("1")));
        assertTrue(courtVenueResponse.stream().allMatch(venue ->
               venue.getCourtName().strip().toLowerCase().contains("Abe".toLowerCase())
            || venue.getSiteName().strip().toLowerCase().contains("Abe".toLowerCase())
            || venue.getCourtAddress().strip().toLowerCase().contains("Abe".toLowerCase())
            || venue.getPostcode().strip().toLowerCase().contains("Abe".toLowerCase())));
        assertTrue(courtVenueResponse.stream().allMatch(venue -> venue.getCourtStatus().equals("Open")));
        assertThat(courtVenueResponse.size()).isEqualTo(2);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldRetrieveCourtVenues_By_MultipleParamsAndSearchString_WithStatusCode_200() {
        final var response = (LrdCourtVenueResponse[]) lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK,
                "?court-type-id=1&is_case_management_location=n&location_type=CTSC&search-string=Aber",
                        LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotNull();

        var courtVenueResponse = new ArrayList<>(Arrays.asList(response));
        assertTrue(courtVenueResponse.stream().allMatch(venue -> venue.getCourtTypeId().equals("1")));
        assertTrue(courtVenueResponse.stream().allMatch(venue ->
               venue.getCourtName().strip().toLowerCase().contains("Aber".toLowerCase())
                   || venue.getSiteName().strip().toLowerCase().contains("Aber".toLowerCase())
                   || venue.getCourtAddress().strip().toLowerCase().contains("Aber".toLowerCase())
                   || venue.getPostcode().strip().toLowerCase().contains("Aber".toLowerCase())));
        assertTrue(courtVenueResponse.stream()
                       .allMatch(venue -> venue.getIsCaseManagementLocation().equalsIgnoreCase("n")));
        assertTrue(courtVenueResponse.stream().allMatch(venue -> venue.getLocationType().equals("CTSC")));
        assertTrue(courtVenueResponse.stream().allMatch(venue -> venue.getCourtStatus().equals("Open")));
        assertThat(courtVenueResponse.size()).isEqualTo(1);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldRetrieveCourtVenues_By_NoSearchString_WithStatusCode_400() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.BAD_REQUEST, "?search-string",
                                                         LrdCourtVenueResponse[].class, path
        );
        assertThat(response).isNotNull();
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), response.getErrorMessage());
        assertEquals(String.format(SEARCH_STRING_VALUE_ERROR_MESSAGE, ""), response.getErrorDescription());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldReturnEmptyList_WhenNoDataFound() {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.OK,
                "?search-string=abe&court-type-id=8",
                LrdCourtVenueResponse[].class,
                path
            );
        assertEquals(0, response.length);
    }

    @Test
    @ExtendWith(FeatureToggleConditionExtension.class)
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    void shouldNotRetrieveCourtVenues_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.FORBIDDEN,
                                                 "?search-string=zzz&court-type-id=1000",
                                                 LrdCourtVenueResponse.class, path
                );
        assertNotNull(response);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocations_UnauthorizedDueToNoBearerToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveResponseForGivenRequest_NoBearerToken("1", path);

        assertNotNull(response);
        assertThat(response.getHeader("UnAuthorized-Token-Error")).contains("Authentication Exception");
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void retrieveBuildingLocations_UnauthorizedDueToNoS2SToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveResponseForGivenRequest_NoS2SToken("1", path);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }
}
