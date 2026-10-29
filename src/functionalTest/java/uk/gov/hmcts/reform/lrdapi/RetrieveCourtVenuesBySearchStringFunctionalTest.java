package uk.gov.hmcts.reform.lrdapi;

import io.restassured.response.Response;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lib.util.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_INVALID_SERVICE_CODE;
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
            .filter(venue -> venue.getCourtName().strip().toLowerCase()
                .contains("Abe".toLowerCase())
                || venue.getSiteName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getCourtAddress().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getPostcode().strip().toLowerCase().contains("Abe".toLowerCase()))
            .collect(Collectors.toList());

        assertTrue(courtNameVerified
                       .stream()
                       .allMatch(venue -> venue.getCourtStatus().equals("Open"))
        );
        assertThat(courtNameVerified.size()).isPositive();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldRetrieveCourtVenues_By_SearchStringWithHyphen_WithStatusCode_200() {
        final var response = (LrdCourtVenueResponse[]) lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK,
                          "?search-string=Stoke-on", LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotNull().isNotEmpty();
        assertThat(response.length).isPositive();
        assertThat(response[0].getCourtStatus()).isEqualTo("Open");

    }



    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldRetrieveCourtVenues_By_CourtTypeIdAndSearchString_WithStatusCode_200() {
        var searchString = "Abe";
        var matchingVenue = retrieveMatchingVenue(
            searchString,
            venue -> isNotBlank(venue.getCourtTypeId())
        );

        final var response = (LrdCourtVenueResponse[]) lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK,
                        "?court-type-id=" + matchingVenue.getCourtTypeId() + "&search-string=" + searchString,
                        LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty();

        var courtVenueResponse = new ArrayList<>(Arrays.asList(response));
        assertTrue(courtVenueResponse.stream().allMatch(venue ->
                                                            venue.getCourtTypeId()
                                                                .equals(matchingVenue.getCourtTypeId())));
        assertTrue(courtVenueResponse.stream().allMatch(venue -> venueContains(venue, searchString)));
        assertTrue(courtVenueResponse.stream().allMatch(venue ->
                                                            venue.getCourtStatus().equals("Open")));
        assertThat(courtVenueResponse.size()).isPositive();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldRetrieveCourtVenues_By_MultipleParamsAndSearchString_WithStatusCode_200() {
        var searchString = "Abe";
        var matchingVenue = retrieveMatchingVenue(
            searchString,
            venue -> isNotBlank(venue.getCourtTypeId())
                && isNotBlank(venue.getIsCaseManagementLocation())
                && isNotBlank(venue.getLocationType())
        );

        final var response = (LrdCourtVenueResponse[]) lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK,
                "?court-type-id=" + matchingVenue.getCourtTypeId()
                    + "&is_case_management_location=" + matchingVenue.getIsCaseManagementLocation()
                    + "&location_type=" + encode(matchingVenue.getLocationType())
                    + "&search-string=" + searchString,
                LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotNull();

        var courtVenueResponse = new ArrayList<>(Arrays.asList(response));
        assertTrue(courtVenueResponse.stream().allMatch(venue ->
                                                            venue.getCourtTypeId()
                                                                .equals(matchingVenue.getCourtTypeId())));
        assertTrue(courtVenueResponse.stream().allMatch(venue -> venueContains(venue, searchString)));
        assertTrue(courtVenueResponse.stream()
                       .allMatch(venue -> venue.getIsCaseManagementLocation()
                           .equalsIgnoreCase(matchingVenue.getIsCaseManagementLocation())));
        assertTrue(courtVenueResponse.stream().allMatch(venue ->
                                                            venue.getLocationType()
                                                                .equalsIgnoreCase(matchingVenue.getLocationType())));
        assertTrue(courtVenueResponse.stream().allMatch(venue ->
                                                            venue.getCourtStatus().equals("Open")));
        assertThat(courtVenueResponse.size()).isPositive();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void shouldReturnEmptyList_WhenServiceCodeAndSearchStringHaveNoMatchingData_WithStatusCode_200() {
        final var filteredResponse = (LrdCourtVenueResponse[]) lrdApiClient.retrieveResponseForGivenRequest(
            HttpStatus.OK,
            "?search-string=Abe&service_code=BFA1",
            LrdCourtVenueResponse[].class,
            path
        );

        assertThat(filteredResponse).isNotNull().isEmpty();
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
    void shouldReturn400_WhenServiceCodeContainsSpecialCharacters() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.BAD_REQUEST,
                "?search-string=Abe&service_code=AB$",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotNull();
        assertEquals(INVALID_REQUEST_EXCEPTION.getErrorMessage(), response.getErrorMessage());
        assertEquals(String.format(EXCEPTION_MSG_INVALID_SERVICE_CODE, "AB$"), response.getErrorDescription());
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

    private LrdCourtVenueResponse retrieveMatchingVenue(String searchString, Predicate<LrdCourtVenueResponse> filter) {
        final var searchResponse = (LrdCourtVenueResponse[]) lrdApiClient.retrieveResponseForGivenRequest(
            HttpStatus.OK,
            "?search-string=" + searchString,
            LrdCourtVenueResponse[].class,
            path
        );

        return Arrays.stream(searchResponse)
            .filter(filter)
            .findFirst()
            .orElseThrow(() -> new AssertionError("No court venue test data found for search string " + searchString));
    }

    private boolean venueContains(LrdCourtVenueResponse venue, String searchString) {
        return containsIgnoreCase(venue.getCourtName(), searchString)
            || containsIgnoreCase(venue.getSiteName(), searchString)
            || containsIgnoreCase(venue.getCourtAddress(), searchString)
            || containsIgnoreCase(venue.getPostcode(), searchString);
    }

    private boolean containsIgnoreCase(String value, String searchString) {
        return value != null && value.strip().toLowerCase(Locale.ROOT).contains(searchString.toLowerCase(Locale.ROOT));
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
