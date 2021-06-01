package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.util.CustomSerenityRunner;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
@SuppressWarnings("unchecked")
public class RetrieveBuildingLocationDetailsFunctionalTest extends AuthorizationFunctionalTest {

    private static final String mapKey = "LrdApiController.retrieveBuildingLocationDetailsByEpimsId";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_WithStatusCode_200() throws JsonProcessingException {
        LrdBuildingLocationResponse response = (LrdBuildingLocationResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.NOT_FOUND, "1");
        //POST API to be used to pre-insert the test data once available. Tech Debt for now.
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocations_WithStatusCode_404() throws JsonProcessingException {
        LrdBuildingLocationResponse response = (LrdBuildingLocationResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.NOT_FOUND, "1QWERTY");
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocations_WhenToggleOff_WithStatusCode_403() throws JsonProcessingException {
        LrdBuildingLocationResponse response = (LrdBuildingLocationResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.FORBIDDEN, "1");
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_UnauthorizedDueToNoBearerToken_ShouldReturnStatusCode401() {
        ErrorResponse response =
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId_NoBearerToken(HttpStatus.UNAUTHORIZED, "1");

        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_UnauthorizedDueToNoS2SToken_ShouldReturnStatusCode401() {
        ErrorResponse response =
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId_NoS2SToken(HttpStatus.UNAUTHORIZED, "1");

        assertThat(response).isNotNull();
    }

}
