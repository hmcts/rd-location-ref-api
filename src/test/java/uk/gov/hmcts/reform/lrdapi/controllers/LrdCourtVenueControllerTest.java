package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LrdCourtVenueControllerTest {

    @InjectMocks
    LrdCourtVenueController lrdCourtVenueController;

    @Mock
    CourtVenueService courtVenueServiceMock;


    private CourtVenueRequestParam courtVenueRequestParam;

   @BeforeEach
    public void setup() {
       courtVenueRequestParam = mock(CourtVenueRequestParam.class);
       /*courtVenueRequestParam =
           new CourtVenueRequestParam();
       courtVenueRequestParam.setIsCaseManagementLocation("Y");
       courtVenueRequestParam.setIsHearingLocation("Y");
       courtVenueRequestParam.setIsTemporaryLocation("N");
       courtVenueRequestParam.setLocationType("CTSC");*/
    }

    @Test
    void testGetCourtVenues_ByServiceCode_Returns200() {
        ResponseEntity<Object> responseEntity =
            lrdCourtVenueController.retrieveCourtVenuesByServiceCode("BFA1");

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
                "1234", null, null, null, null,"Y",
                "Y","CTSC","Y");



        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        //TODO reformat
//        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetails(
//            "1234",
//            null,  null,  null, null,
//            courtVenueRequestParam);
    }

    @Test
    void testGetCourtVenuesBySearchString() {
        ResponseEntity<List<LrdCourtVenueResponse>> responseEntity =
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("MAN", null);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(courtVenueServiceMock, times(1)).retrieveCourtVenuesBySearchString(
            "MAN",
            null
        );
    }

    @Test
    void testGetCourtVenuesBySearchStringWithInvalidString() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("$AB_C", null));
    }

    @Test
    void testGetCourtVenuesBySearchStringWithStringLessThan3Char() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("AB", null));
    }

    @Test
    void testGetCourtVenuesBySearchStringWithInvalidCourtTypeId() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("ABC", "1,2,*"));
    }

    @Test
    void testGetCourtVenuesBySearchStringWithInvalidCourtTypeIdWithComma() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesBySearchString("ABC", ",1,2,"));
    }
}
