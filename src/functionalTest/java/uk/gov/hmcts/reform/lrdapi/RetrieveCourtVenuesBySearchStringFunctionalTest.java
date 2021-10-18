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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(CustomSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")

public class RetrieveCourtVenuesBySearchStringFunctionalTest extends AuthorizationFunctionalTest {

    public static final String mapKey = "LrdCourtVenueController.retrieveCourtVenuesBySearchString";
    private static final String path = "/court-venues/venue-search";

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
