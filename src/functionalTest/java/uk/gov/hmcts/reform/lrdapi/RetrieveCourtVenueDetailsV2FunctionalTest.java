package uk.gov.hmcts.reform.lrdapi;

import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lib.util.serenity5.SerenityTest;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
class RetrieveCourtVenueDetailsV2FunctionalTest extends AuthorizationFunctionalTest {

    private static final String path = "/v2/court-venues";

    @Test
    void shouldRetrieveCourtVenuesV2_WithStatusCode_200() {
        final var response = (LrdCourtVenueV2Response[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, null,
                                                         LrdCourtVenueV2Response[].class,
                                                         path);

        assertThat(response).isNotEmpty();
        assertThat(Arrays.stream(response)
                       .map(LrdCourtVenueV2Response::getMrdVenueId))
            .allMatch(mrdVenueId -> mrdVenueId != null && !mrdVenueId.isBlank());
        assertNotNull(response[0].getCourtUse());
        assertNotNull(response[0].getNames());
    }
}
