package uk.gov.hmcts.reform.lrdapi;

import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lib.util.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.EMPTY_RESULT_DATA_ACCESS;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
class RetrieveCourtVenuesByServiceCodeV2FunctionalTest extends AuthorizationFunctionalTest {

    private static final String PATH = "/v2/court-venues/services";

    @Test
    void getCourtVenuesByServiceCodeV2WithStatusCode_200() {
        LrdCourtVenueV2Response[] response = (LrdCourtVenueV2Response[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, "?service_code=AAA6",
                                                         LrdCourtVenueV2Response[].class,
                                                         PATH);
        assertNotNull(response);
        assertThat(response).isNotEmpty();
        assertThat(response).extracting(LrdCourtVenueV2Response::getServiceCode).contains("AAA6");
        assertThat(response).extracting(LrdCourtVenueV2Response::getMrdVenueId).doesNotContainNull();
    }

    @Test
    void getCourtVenuesByServiceCodeV2WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND, "?service_code=53453",
                                                         ErrorResponse.class,
                                                         PATH);
        assertNotNull(response);
        assertEquals(EMPTY_RESULT_DATA_ACCESS.getErrorMessage(), response.getErrorMessage());
        assertEquals("No court venues found for the given service code 53453",
                     response.getErrorDescription());
    }
}
