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
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.util.CustomSerenityRunner;
import uk.gov.hmcts.reform.lrdapi.util.ToggleEnable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
@SuppressWarnings("unchecked")
public class RetrieveBuildingLocationDetailsFunctionalTest extends AuthorizationFunctionalTest {

    private static final String mapKey = "LrdApiController.retrieveBuildingLocationDetails";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833",
                LrdBuildingLocationResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveAllBuildingLocations_NoEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.OK, null,
                                                                          LrdBuildingLocationResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_MultipleEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833"
                + ",219164",LrdBuildingLocationResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveAllBuildingLocations_AllEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdBuildingLocationResponse>)
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=ALL",
                                                                          LrdBuildingLocationResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocations_WithStatusCode_404() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.NOT_FOUND,
                                                                          "?epimms_id=aaazzzzzzzzzzzzz",
                                                                          LrdBuildingLocationResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocations_WhenToggleOff_WithStatusCode_403() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.FORBIDDEN, "1",
                                                                          LrdBuildingLocationResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_UnauthorizedDueToNoBearerToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam_NoBearerToken("1");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_UnauthorizedDueToNoS2SToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam_NoS2SToken("1");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocationsForValidBuildingName_WithStatusCode_200() {
        final var response = (LrdBuildingLocationResponse)
            lrdApiClient
                .retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.OK,
                               "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE",
                                                                          LrdBuildingLocationResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocationsForInValidBuildingName_WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.NOT_FOUND,
                                                                          "?building_location_name=Invalid",
                                                                          LrdBuildingLocationResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocationsForGivenBuildingName_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient
                .retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus.FORBIDDEN,
                             "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE",
                                                                          LrdBuildingLocationResponse.class);
        assertThat(response).isNotNull();
    }

}
