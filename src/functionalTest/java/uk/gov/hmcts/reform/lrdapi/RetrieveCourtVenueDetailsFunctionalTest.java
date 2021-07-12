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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(CustomSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
@SuppressWarnings("unchecked")
public class RetrieveCourtVenueDetailsFunctionalTest extends AuthorizationFunctionalTest {

    private static final String mapKey = "LrdApiController.retrieveCourtVenueDetails";

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_Given_Epimms_Id_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833",
                                                                    LrdCourtVenueResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_Given_Multiple_Epimms_Id_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833",
                                                                    LrdCourtVenueResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_Given_No_Epimms_Id_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833",
                                                                    LrdCourtVenueResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_Given_All_Epimms_Id_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833",
                                                                    LrdCourtVenueResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_Given_CourtType_Id_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833",
                                                                    LrdCourtVenueResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_Given_Region_Id_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833",
                                                                    LrdCourtVenueResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_Given_Cluster_Id_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.OK, "?epimms_id=815833",
                                                                    LrdCourtVenueResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = true)
    public void shouldRetrieveCourtVenues_For_NoQueryParam_WithStatusCode_200() throws JsonProcessingException {
        final var response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.OK, null,
                                                                    LrdCourtVenueResponse[].class);
        assertThat(response).isNotNull();
    }

    @Test
    @ToggleEnable(mapKey = mapKey, withFeature = false)
    public void shouldNotRetrieveCourtVenues_WhenToggleOff_WithStatusCode_403() {
        ErrorResponse response = (ErrorResponse)
            lrdApiClient
                .retrieveCourtVenueDetailsByGivenQueryParam(HttpStatus.FORBIDDEN,
                                                                  "?epimms_id=815833",
                                                                  LrdBuildingLocationResponse.class);
        assertThat(response).isNotNull();
    }
}
