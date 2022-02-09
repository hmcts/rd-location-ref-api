package uk.gov.hmcts.reform.lrdapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
class RetrieveCourtVenueDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {
    private static final String HTTP_STATUS_STR = "http_status";
    private static final String path = "/court-venues";

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400ForInvalidEpimmsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=-1111", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400ForNonStandardCharsEpimsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=!@£@$", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400ForAsteriskEpimsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=*", LrdCourtVenueResponse[].class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR,HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400ForOnlyTwoCommaGivenAsEpimsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=,,", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400ForTwoCommaWithValidEpimsId() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789,,", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturn400WhenRegionIdIsString() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?region_id=test_region", ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturn400WhenClusterIdIsString() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?cluster_id=test_cluster",
                                                         ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturn400WhenCourtTypeIdIsString() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?court_type_id=test_court_type",
                                                         ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }


    @Test
    void shouldReturn400WhenMultipleQueryParamsPassed() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveResponseForGivenRequest("?epimms_id=123456789&court_type_id=1"
                                                             + "&cluster_id=1",
                                                         ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturn200WhenParameterEpmIdsValueAllWithYPassed() throws
        JsonProcessingException {


        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=Y&is_case_management_location=Y&location_type=CTSC"
                    + "&is_temporary_location=Y",
                LrdCourtVenueResponse[].class,
                path
            );

        assertNotNull(response);
        assertThat(response.size()).isEqualTo(1);

    }



    @Test
    void shouldReturn200WhenParameterEpmIdsValueAllWithNAndLocationTypeNbcPassed() throws
        JsonProcessingException {


        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=N&is_case_management_location=N&location_type=NBC"
                    + "&is_temporary_location=N",
                LrdCourtVenueResponse[].class,
                path
            );

        assertNotNull(response);
        assertThat(response.size()).isEqualTo(1);

    }

    @Test
    void shouldReturn200WhenParameterEpmIdsValueAllWithHearingLocationYPassed() throws
        JsonProcessingException {


        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=Y",
                LrdCourtVenueResponse[].class,
                path
            );

        assertNotNull(response);
        assertThat(response.size()).isEqualTo(2);

    }

    @Test
    void shouldReturn200WhenParameterCourtTypeIdIsPassed() throws
        JsonProcessingException {


        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?court_type_id=17",
                LrdCourtVenueResponse[].class,
                path
            );

        assertNotNull(response);
        assertThat(response.get(0).getCourtTypeId()).isEqualTo("17");

    }

    @Test
    void shouldReturn200WhenParameterRegionIdIsPassed() throws
        JsonProcessingException {


        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?region_id=1",
                LrdCourtVenueResponse[].class,
                path
            );

        assertNotNull(response);
        assertThat(response.get(0).getRegionId()).isEqualTo("1");

    }

    @Test
    void shouldReturn200WhenParameterClusterIdIsPassed() throws
        JsonProcessingException {


        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?cluster_id=2",
                LrdCourtVenueResponse[].class,
                path
            );

        assertNotNull(response);
        assertThat(response.get(0).getClusterId()).isEqualTo("2");

    }

    @Test
    void shouldReturn200WhenParameterCourtVenueNameIsPassed() throws
        JsonProcessingException {
        //Its siteName or courtName

        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?court_venue_name=ABERDEEN TRIBUNAL HEARING CENTRE 10",
                LrdCourtVenueResponse[].class,
                path
            );

        assertNotNull(response);
        assertThat(response.get(0).getCourtName()).isEqualToIgnoringCase("ABERDEEN TRIBUNAL HEARING CENTRE 10");

    }

    @Test
    void shouldReturn400WhenCourtVenueResponseIsEmpty() throws
        JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(
                "?epimms_id=ALL"
                    + "&is_hearing_location=N&is_case_management_location=N&location_type=CCBC"
                    + "&is_temporary_Location=N",
                ErrorResponse.class,
                path
            );

        ErrorResponse errorResponse = (ErrorResponse) errorResponseMap.get("response_body");
        assertNotNull(errorResponseMap);

        assertThat(errorResponse.getErrorDescription()).contains("There are no court venues found");
    }

    @Test
    void shouldReturn200WhenNoQueryParameterIsPassed() throws
        JsonProcessingException {


        List<LrdCourtVenueResponse> response = (List<LrdCourtVenueResponse>)
            lrdApiClient.retrieveCourtVenueResponseForGivenRequest(null, LrdCourtVenueResponse[].class, path);

        assertNotNull(response);
        assertThat(response.size()).isEqualTo(11);

    }
}
