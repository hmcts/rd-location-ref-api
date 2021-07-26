package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LrdCourtVenueControllerTest {

    @InjectMocks
    LrdCourtVenueController lrdCourtVenueController;

    @Mock
    CourtVenueService courtVenueServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCourtVenues_ByServiceCode_Returns200() {
        ResponseEntity<Object> responseEntity =
            lrdCourtVenueController.retrieveCourtVenuesByServiceCode("BFA1");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(courtVenueServiceMock, times(1)).retrieveCourtVenuesByServiceCode("BFA1");
    }

    @Test
    public void testGetCourtVenues_ByBlankServiceCode_Returns400() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesByServiceCode(""));
    }

    @Test
    public void testGetCourtVenues_ByInvalidServiceCode_Returns400() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueController.retrieveCourtVenuesByServiceCode("@AB_C"));
    }

    @Test
    public void testGetCourtVenues_returns200() {
        ResponseEntity<Object> responseEntity =
            lrdCourtVenueController.retrieveCourtVenues("1234", null, null, null);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetails("1234",
                                                                          null, null, null);
    }
}
