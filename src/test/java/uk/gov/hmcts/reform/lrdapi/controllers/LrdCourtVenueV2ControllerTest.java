package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LrdCourtVenueV2ControllerTest {

    @InjectMocks
    LrdCourtVenueV2Controller lrdCourtVenueV2Controller;

    @Mock
    CourtVenueService courtVenueServiceMock;

    @Test
    void testGetCourtVenuesByServiceCodeV2Returns200() {
        LrdCourtVenueV2Response courtVenueResponse = LrdCourtVenueV2Response.builder()
            .mrdVenueId("MRD-123456789")
            .serviceCode("BFA1")
            .build();

        when(courtVenueServiceMock.retrieveCourtVenuesByServiceCodeV2("BFA1"))
            .thenReturn(List.of(courtVenueResponse));

        ResponseEntity<List<LrdCourtVenueV2Response>> responseEntity =
            lrdCourtVenueV2Controller.retrieveCourtVenuesByServiceCodeV2(" BFA1 ");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(responseEntity.getBody()).containsExactly(courtVenueResponse);
        verify(courtVenueServiceMock, times(1)).retrieveCourtVenuesByServiceCodeV2("BFA1");
    }

    @Test
    void testGetCourtVenuesByServiceCodeV2WithBlankServiceCodeThrows400() {
        assertInvalidRequestMessage(
            () -> lrdCourtVenueV2Controller.retrieveCourtVenuesByServiceCodeV2(""),
            "No service code provided"
        );
        verifyNoInteractions(courtVenueServiceMock);
    }

    @Test
    void testGetCourtVenuesByServiceCodeV2WithInvalidServiceCodeThrows400() {
        assertInvalidRequestMessage(
            () -> lrdCourtVenueV2Controller.retrieveCourtVenuesByServiceCodeV2("@AB_C"),
            "Invalid service code. Please provide service code without special characters"
        );
        verifyNoInteractions(courtVenueServiceMock);
    }

    private void assertInvalidRequestMessage(Runnable request, String expectedMessage) {
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, request::run);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}
