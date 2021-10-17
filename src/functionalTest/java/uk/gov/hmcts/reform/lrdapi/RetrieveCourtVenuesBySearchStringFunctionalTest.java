package uk.gov.hmcts.reform.lrdapi;

import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.util.CustomSerenityRunner;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(CustomSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")

public class RetrieveCourtVenuesBySearchStringFunctionalTest extends AuthorizationFunctionalTest {

    public static final String mapKey = "LrdCourtVenueController.retrieveCourtVenuesBySearchString";
    private static final String path = "/court-venues/venue-search";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_SearchString_WithStatusCode_200() {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.OK,
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
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_SearchString_And_CourtTypeId_WithStatusCode_200() {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.OK,
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
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldReturnEmptyList_WhenNoDataFound() {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(
                HttpStatus.OK,
                "?search-string=zzz&court-type-id=1000",
                LrdCourtVenueResponse[].class,
                path
            );
        assertEquals(0, response.length);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveCourtVenues_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.FORBIDDEN,
                                                 "?search-string=zzz&court-type-id=1000",
                                                 LrdCourtVenueResponse.class, path
                );
        assertThat(response).isNotNull();
    }
}
