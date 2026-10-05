package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LrdCourtVenueV2ControllerTest {

    @InjectMocks
    LrdCourtVenueV2Controller lrdCourtVenueV2Controller;

    @Mock
    CourtVenueService courtVenueServiceMock;

    @Test
    void testGetCourtVenuesWithCourtStatusReturns200() {
        ResponseEntity<List<LrdCourtVenueV2Response>> responseEntity =
            lrdCourtVenueV2Controller.retrieveCourtVenues(
                null, null, null, null, null, null, null,
                null, null, null, null, "MRD-BLD-385", "Closed"
            );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ArgumentCaptor<CourtVenueRequestParam> courtVenueRequestParamCaptor =
            ArgumentCaptor.forClass(CourtVenueRequestParam.class);

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetailsV2(
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            ArgumentCaptor.forClass(Boolean.class).capture(),
            courtVenueRequestParamCaptor.capture()
        );
        assertEquals("MRD-BLD-385", courtVenueRequestParamCaptor.getValue().getMrdBuildingId());
        assertEquals("Closed", courtVenueRequestParamCaptor.getValue().getCourtStatus());
    }
}
