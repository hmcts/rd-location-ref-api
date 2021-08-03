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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
public class RetrieveBuildingLocationDetailsFunctionalTest extends AuthorizationFunctionalTest {

    private static final String mapKey = "LrdApiController.retrieveBuildingLocationDetails";
    private static final String path = "/building-locations";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, "?epimms_id=815833",
                LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildLocations_OneValidepimmsIdAndAllGiven_ShouldReturnValidResponseAndStatusCode200() throws
        JsonProcessingException {

        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK,"?epimms_id=123456789,ALL",
                 LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveAllBuildingLocations_NoEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, null,
                                                                          LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_MultipleEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, "?epimms_id=815833"
                + ",219164",LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveAllBuildingLocations_AllEpimmsIdPassed_WithStatusCode_200() throws JsonProcessingException {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.OK, "?epimms_id=ALL",
                                                                          LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocations_WithStatusCode_404() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND,
                                                                          "?epimms_id=no_epimms_id",
                                                                          LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocations_WhenToggleOff_WithStatusCode_403() throws JsonProcessingException {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.FORBIDDEN, "1",
                                                                          LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_UnauthorizedDueToNoBearerToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveResponseForGivenRequest_NoBearerToken("1", path);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocations_UnauthorizedDueToNoS2SToken_ShouldReturnStatusCode401() {
        Response response =
            lrdApiClient.retrieveResponseForGivenRequest_NoS2SToken("1", path);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocationsForValidBuildingName_WithStatusCode_200() {
        final var response = (LrdBuildingLocationResponse)
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.OK,
                               "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE",
                                                                          LrdBuildingLocationResponse.class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocationsForInValidBuildingName_WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND,
                                                                          "?building_location_name=Invalid",
                                                                          LrdBuildingLocationResponse.class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocationsForGivenBuildingName_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.FORBIDDEN,
                             "?building_location_name=ABERDEEN TRIBUNAL HEARING CENTRE",
                                                                          LrdBuildingLocationResponse.class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocationsForValidRegionId_WithStatusCode_200() {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.OK,
                                                                  "?region_id=3",
                                                                  LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocationsForNonExistentRegionId_WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND,
                                                                          "?region_id=100000000",
                                                                          LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocationsForGivenRegionId_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.FORBIDDEN,
                                                                  "?region_id=1",
                                                                  LrdBuildingLocationResponse.class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void retrieveBuildingLocationsForValidClusterId_WithStatusCode_200() {
        final var response = (LrdBuildingLocationResponse[])
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.OK,
                                                                  "?cluster_id=9",
                                                                  LrdBuildingLocationResponse[].class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldNotRetrieveBuildingLocationsForNonExistentClusterId_WithStatusCode_404() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient.retrieveResponseForGivenRequest(HttpStatus.NOT_FOUND,
                                                                          "?cluster_id=10000000",
                                                                          LrdBuildingLocationResponse.class, path);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveBuildingLocationsForGivenClusterId_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient
                .retrieveResponseForGivenRequest(HttpStatus.FORBIDDEN,
                                                                  "?cluster_id=1",
                                                                  LrdBuildingLocationResponse.class, path);
        assertThat(response).isNotNull();
    }

}
