package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
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
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.NOT_FOUND, "?epimms_id=1");
        //POST API to be used to pre-insert the test data once available. Tech Debt for now.
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveAllBuildingLocations_NoEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.NOT_FOUND, null);
        //POST API to be used to pre-insert the test data once available. Tech Debt for now.
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_MultipleEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.NOT_FOUND, "?epimms_id=1,2");
        //POST API to be used to pre-insert the test data once available. Tech Debt for now.
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveAllBuildingLocations_AllEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.NOT_FOUND, "?epimms_id=ALL");
        //POST API to be used to pre-insert the test data once available. Tech Debt for now.
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocations_WithStatusCode_404() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.NOT_FOUND, "?epimms_id=aaazzzzzzzzzzzzz");
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocations_WhenToggleOff_WithStatusCode_403() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.FORBIDDEN, "1");
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_UnauthorizedDueToNoBearerToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId_NoBearerToken("1");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_UnauthorizedDueToNoS2SToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId_NoS2SToken("1");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocationsForValidBuildingName_WithStatusCode_200() {
        //Currently we get 404 as the Api to insert data into database is yet to be implemented
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByBuildingName(HttpStatus.NOT_FOUND,
                                         "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE");
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocationsForInValidBuildingName_WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByEpimsId(HttpStatus.NOT_FOUND,
                                     "?building_location_name=Invalid");
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocationsForGivenBuildingName_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByBuildingName(HttpStatus.FORBIDDEN,
                                          "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE");
        assertThat(response).isNotNull();
    }

}
