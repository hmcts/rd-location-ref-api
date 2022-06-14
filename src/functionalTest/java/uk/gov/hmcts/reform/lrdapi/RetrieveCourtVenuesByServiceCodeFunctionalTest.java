package uk.gov.hmcts.reform.lrdapi;

import io.restassured.response.Response;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lib.util.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.EMPTY_RESULT_DATA_ACCESS;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension.getToggledOffMessage;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
class RetrieveCourtVenuesByServiceCodeFunctionalTest extends AuthorizationFunctionalTest {

    public static final String mapKey = "LrdCourtVenueController.retrieveCourtVenuesByServiceCode";
    private static final String path = "/court-venues/services";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void getCourtVenuesByServiceCodeWithStatusCode_200() {
        LrdCourtVenuesByServiceCodeResponse response = (LrdCourtVenuesByServiceCodeResponse)
            lrdApiClient.retrieveCourtVenuesByServiceCode(HttpStatus.OK, "BFA1");

        assertNotNull(response);
        responseVerification(response);
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void getCourtVenuesByServiceCodeWithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveCourtVenuesByServiceCode(HttpStatus.NOT_FOUND, "BFA2");

        assertNotNull(response);
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), response.getErrorMessage());
        assertEquals("No court types found for the given service code BFA2",
                     response.getErrorDescription());
    }


    @Test
    @ExtendWith(FeatureToggleConditionExtension.class)
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    void should_retrieve_403_when_Api_toggled_off() {
        String exceptionMessage = getToggledOffMessage();
        validateErrorResponse(
            (ErrorResponse) lrdApiClient.retrieveCourtVenuesByServiceCode(
                HttpStatus.FORBIDDEN, ""),
            exceptionMessage,
            exceptionMessage
        );
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


    private void responseVerification(LrdCourtVenuesByServiceCodeResponse response) {
        assertEquals("BFA1", response.getServiceCode());
        assertEquals("23", response.getCourtTypeId());
        assertEquals("Immigration and Asylum Tribunal", response.getCourtType());
        assertNull(response.getWelshCourtType());
        assertThat(response.getCourtVenues().size()).isPositive();
    }

}
