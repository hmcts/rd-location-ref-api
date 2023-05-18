package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LrdCourtVenueControllerTest {

    @InjectMocks
    LrdCourtVenueController lrdCourtVenueController;

    @Mock
    CourtVenueService courtVenueServiceMock;





    @Test
    void testGetCourtVenues_ByServiceCode_Returns200() {
        ResponseEntity<Object> responseEntity =
            lrdCourtVenueController.retrieveCourtVenuesByServiceCode("BFA1");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(courtVenueServiceMock, times(1)).retrieveCourtVenuesByServiceCode("BFA1");
    }

    @Test
    void testGetCourtVenues_ByServiceCodeWithSpace_Returns200() {
        ResponseEntity<Object> responseEntity =
            lrdCourtVenueController.retrieveCourtVenuesByServiceCode(" BFA1 ");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(courtVenueServiceMock, times(1)).retrieveCourtVenuesByServiceCode("BFA1");
    }

    @Test
    void testGetCourtVenues_ByBlankServiceCode_Returns400() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesByServiceCode(""));
    }

    @Test
    void testGetCourtVenues_ByInvalidServiceCode_Returns400() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesByServiceCode("@AB_C"));
    }

    @Test
    void testGetCourtVenues_returns200() {
        ResponseEntity<List<LrdCourtVenueResponse>> responseEntity =
            lrdCourtVenueController.retrieveCourtVenues(
                "1234", null, null, null, null, "Y",
                "Y", "CTSC", "Y"
            );


        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ArgumentCaptor<CourtVenueRequestParam> courtVenueRequestParamCaptr =
            ArgumentCaptor.forClass(CourtVenueRequestParam.class);

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetails(
            ArgumentCaptor.forClass(String.class).capture(),
            ArgumentCaptor.forClass(Integer.class).capture(),
            ArgumentCaptor.forClass(Integer.class).capture(),
            ArgumentCaptor.forClass(Integer.class).capture(),
            ArgumentCaptor.forClass(String.class).capture(),
            ArgumentCaptor.forClass(Boolean.class).capture(),
            courtVenueRequestParamCaptr.capture()
        );
        assertNotNull(courtVenueRequestParamCaptr.getValue());
        CourtVenueRequestParam result = courtVenueRequestParamCaptr.getValue();
        assertEquals("Y", result.getIsHearingLocation());
        assertEquals("Y", result.getIsCaseManagementLocation());
        assertEquals("CTSC", result.getLocationType());
        assertEquals("Y", result.getIsTemporaryLocation());
    }

    @Test
    void testGetCourtVenueswitEpimmsIdAndCourtType_returns200() {
        ResponseEntity<List<LrdCourtVenueResponse>> responseEntity =
            lrdCourtVenueController.retrieveCourtVenues(
                "1234", 13, null, null, null, "Y",
                "Y", "CTSC", "Y"
            );


        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ArgumentCaptor<CourtVenueRequestParam> courtVenueRequestParamCaptr =
            ArgumentCaptor.forClass(CourtVenueRequestParam.class);

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetails(
            ArgumentCaptor.forClass(String.class).capture(),
            ArgumentCaptor.forClass(Integer.class).capture(),
            ArgumentCaptor.forClass(Integer.class).capture(),
            ArgumentCaptor.forClass(Integer.class).capture(),
            ArgumentCaptor.forClass(String.class).capture(),
            ArgumentCaptor.forClass(Boolean.class).capture(),
            courtVenueRequestParamCaptr.capture()
        );
        assertNotNull(courtVenueRequestParamCaptr.getValue());
        CourtVenueRequestParam result = courtVenueRequestParamCaptr.getValue();
        assertEquals("Y", result.getIsHearingLocation());
        assertEquals("Y", result.getIsCaseManagementLocation());
        assertEquals("CTSC", result.getLocationType());
        assertEquals("Y", result.getIsTemporaryLocation());
    }

    @Test
    void testGetCourtVenues_WithMultipleParams_Returns400() {
        Exception exception = assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenues("12345", null, 12, null,
            null, null, null, null, null));

        assertNotNull(exception);
        assertEquals("Please provide only 1 of 4 values of params: (1.epimms_id and court_type_id),"
                + " (2.region_id), (3.cluster_id), (4.court_venue_name).",
                     exception.getMessage());
    }

    @Test
    void testGetCourtVenuesBySearchString() {
        var param = new CourtVenueRequestParam();
        ResponseEntity<List<LrdCourtVenueResponse>> responseEntity =
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("MAN", null,
                                                                      param.getIsHearingLocation(),
                                                                      param.getIsCaseManagementLocation(),
                                                                      param.getLocationType(),
                                                                      param.getIsTemporaryLocation());
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenuesBySearchString(
            anyString(),
            isNull(),
            any(CourtVenueRequestParam.class)
        );
    }

    @Test
    void testGetCourtVenuesBySearchStringWithHyphen() {
        var param = new CourtVenueRequestParam();
        ResponseEntity<List<LrdCourtVenueResponse>> responseEntity =
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("Stoke-", null,
                                                                      param.getIsHearingLocation(),
                                                                      param.getIsCaseManagementLocation(),
                                                                      param.getLocationType(),
                                                                      param.getIsTemporaryLocation());
        assertNotNull(responseEntity);
        assertThat(responseEntity.getBody()).isNotNull().isEmpty();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenuesBySearchString(
            anyString(),
            isNull(),
            any(CourtVenueRequestParam.class)
        );
    }

    @Test
    void testGetCourtVenuesBySearchStringWithInvalidString() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("$AB_C", null, null, null, null, null));
    }

    @Test
    void testGetCourtVenuesBySearchStringWithStringLessThan3Char() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("AB", null, null, null, null, null));
    }

    @Test
    void testGetCourtVenuesBySearchStringWithInvalidCourtTypeId() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("ABC", "1,2,*", null, null, null, null));
    }

    @Test
    void testGetCourtVenuesBySearchStringWithInvalidCourtTypeIdWithComma() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("ABC", ",1,2,", null, null, null, null));
    }

    @Test
    void testGetCourtVenuesBySearchStringWithEmptyString() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("", null, null, null, null, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=abc--", "?search-string=ab__c", "?search-string=___c",
        "?search-string=___", "?search-string=@@@", "?search-string=---", "?search-string='''",
        "?search-string=&&&", "?search-string=...", "?search-string=,,,", "?search-string=(((",
        "?search-string=)))"})
    void testGetCourtVenuesBySearchStringWithConsecutiveSpecialCharacters(String param) {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString(param, ",1,2,", null, null, null, null));
    }
}
