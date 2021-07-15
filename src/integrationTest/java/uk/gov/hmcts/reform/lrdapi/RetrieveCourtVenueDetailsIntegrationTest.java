package uk.gov.hmcts.reform.lrdapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
public class RetrieveCourtVenueDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {
    private static final String HTTP_STATUS_STR = "http_status";
    private static final String path = "/court-venues";

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400ForInvalidEpimmsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=-1111", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400ForNonStandardCharsEpimsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=!@£@$", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400ForAsteriskEpimsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=*", LrdCourtVenueResponse[].class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

//    @Test
//    @SuppressWarnings("unchecked")
//    public void retrieveBuildLocations_AsteriskAlongWithValidEpimsIdGiven_ReturnValidResponseAndStatusCode200() throws
//        JsonProcessingException {
//
//        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
//            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789, *",
//                                                         LrdCourtVenueResponse[].class, path);
//
//        assertNotNull(response);
//        responseVerification(response, ONE_STR);
//    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400ForOnlyTwoCommaGivenAsEpimsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=,,", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400ForTwoCommaWithValidEpimsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789,,", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

//    @Test
//    public void shouldReturn400WhenRegionIdIsString() throws
//        JsonProcessingException {
//
//        List<LrdCourtVenuenResponse> response = (List<LrdCourtVenuenResponse>)
//            lrdApiClient.findCourtVenuesByGivenQueryParam("?region_id=123456789",
//                                                               LrdBuildingLocationResponse[].class);
//
//        assertNotNull(response);
//        responseVerification(response, ONE_STR);
//    }
//
//    @Test
//    public void shouldReturn400WhenClusterIdIsString() throws
//        JsonProcessingException {
//
//        List<LrdCourtVenuenResponse> response = (List<LrdCourtVenuenResponse>)
//            lrdApiClient.findCourtVenuesByGivenQueryParam("?cluster_id=123456789",
//                                                          LrdBuildingLocationResponse[].class);
//
//        assertNotNull(response);
//        responseVerification(response, ONE_STR);
//    }
//
//    @Test
//    public void shouldReturn400WhenCourtTypeIdIsString() throws
//        JsonProcessingException {
//
//        List<LrdCourtVenuenResponse> response = (List<LrdCourtVenuenResponse>)
//            lrdApiClient.findCourtVenuesByGivenQueryParam("?cluster_id=123456789",
//                                                          LrdBuildingLocationResponse[].class);
//
//        assertNotNull(response);
//        responseVerification(response, ONE_STR);
//    }
//
//
//    @Test
//    public void shouldReturn400WhenMultipleQueryParamsPassed() throws
//        JsonProcessingException {
//
//        List<LrdCourtVenuenResponse> response = (List<LrdCourtVenuenResponse>)
//            lrdApiClient.findCourtVenuesByGivenQueryParam("?cluster_id=123456789",
//                                                          LrdBuildingLocationResponse[].class);
//
//        assertNotNull(response);
//        responseVerification(response, ONE_STR);
//    }
}
