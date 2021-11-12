package uk.gov.hmcts.reform.lrdapi;

import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.hmcts.reform.lrdapi.util.FeatureToggleConditionExtension.getToggledOffMessage;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
class RetrieveCourtVenuesByServiceCodeFunctionalTest extends AuthorizationFunctionalTest {

    public static final String mapKey = "LrdCourtVenueController.retrieveCourtVenuesByServiceCode";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    void getCourtVenuesByServiceCodeWithStatusCode_200() {
        LrdCourtVenuesByServiceCodeResponse response = (LrdCourtVenuesByServiceCodeResponse)
            lrdApiClient.retrieveCourtVenuesByServiceCode(HttpStatus.OK, "BFA1");

        assertNotNull(response);
        responseVerification(response);
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


    private void responseVerification(LrdCourtVenuesByServiceCodeResponse response) {
        assertEquals("BFA1", response.getServiceCode());
        assertEquals("23", response.getCourtTypeId());
        assertEquals("Immigration and Asylum Tribunal", response.getCourtType());
        assertNull(response.getWelshCourtType());
        assertNotNull(response.getCourtVenues());
    }

}
