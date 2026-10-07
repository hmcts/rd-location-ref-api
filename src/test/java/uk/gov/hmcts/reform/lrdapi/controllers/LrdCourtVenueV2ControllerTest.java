package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LrdCourtVenueV2ControllerTest {

    @InjectMocks
    LrdCourtVenueV2Controller lrdCourtVenueV2Controller;

    @Mock
    CourtVenueService courtVenueServiceMock;

    @Test
    void testGetCourtVenuesBySearchStringV2Returns200() {
        ResponseEntity<List<LrdCourtVenueV2Response>> responseEntity =
            lrdCourtVenueV2Controller.retrieveCourtVenuesBySearchStringV2(
                " ABC ",
                "17",
                "AAA6",
                "Y",
                "N",
                "Court",
                "N",
                "N",
                "N",
                "N"
            );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ArgumentCaptor<CourtVenueRequestParam> requestParamCaptor =
            ArgumentCaptor.forClass(CourtVenueRequestParam.class);
        verify(courtVenueServiceMock, times(1)).retrieveCourtVenuesBySearchStringV2(
            eq("ABC"),
            eq("17"),
            eq("AAA6"),
            requestParamCaptor.capture()
        );

        CourtVenueRequestParam requestParam = requestParamCaptor.getValue();
        assertThat(requestParam.getIsHearingLocation()).isEqualTo("Y");
        assertThat(requestParam.getIsCaseManagementLocation()).isEqualTo("N");
        assertThat(requestParam.getLocationType()).isEqualTo("Court");
        assertThat(requestParam.getIsTemporaryLocation()).isEqualTo("N");
        assertThat(requestParam.getIsNightingaleCourt()).isEqualTo("N");
        assertThat(requestParam.getIsDistrictRegistry()).isEqualTo("N");
        assertThat(requestParam.getIsAppealCentre()).isEqualTo("N");
    }

    @Test
    void testGetCourtVenuesBySearchStringV2WithInvalidSearchStringThrows400() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueV2Controller.retrieveCourtVenuesBySearchStringV2(
                "",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ));
    }

    @Test
    void testGetCourtVenuesBySearchStringV2WithInvalidServiceCodeThrows400() {
        assertThrows(InvalidRequestException.class, () ->
            lrdCourtVenueV2Controller.retrieveCourtVenuesBySearchStringV2(
                "ABC",
                null,
                "AB$",
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ));
        verify(courtVenueServiceMock, times(0)).retrieveCourtVenuesBySearchStringV2(
            any(),
            any(),
            any(),
            any()
        );
    }
}
