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
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

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

    @Test
    void testGetCourtVenuesWithPrimaryStringParamsUsesV1Validation() {
        retrieveCourtVenues(null, "MRD@123", null, null, null, null, null,
                            null, null, null, null, null, null);

        ArgumentCaptor<String> mrdVenueIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetailsV2(
            isNull(),
            mrdVenueIdCaptor.capture(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            ArgumentCaptor.forClass(Boolean.class).capture(),
            ArgumentCaptor.forClass(CourtVenueRequestParam.class).capture()
        );
        assertThat(mrdVenueIdCaptor.getValue()).isEqualTo("MRD@123");
    }

    @Test
    void testGetCourtVenuesWithSpecialCharactersForMrdBuildingThrows400() {
        assertInvalidRequestMessage(
            () -> retrieveCourtVenues(null, null, null, null, null, null, null,
                                      null, null, null, null, "MRD-BLD-@123", null),
            "Invalid mrd_building_id. Expected format is MRD-BLD-<digits>"
        );
        verifyNoInteractions(courtVenueServiceMock);
    }

    @Test
    void testGetCourtVenuesWithBlankMrdVenuePassesValueToService() {
        retrieveCourtVenues(null, "   ", null, null, null, null, null,
                            null, null, null, null, null, null);

        ArgumentCaptor<String> mrdVenueIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetailsV2(
            isNull(),
            mrdVenueIdCaptor.capture(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            ArgumentCaptor.forClass(Boolean.class).capture(),
            ArgumentCaptor.forClass(CourtVenueRequestParam.class).capture()
        );
        assertThat(mrdVenueIdCaptor.getValue()).isEqualTo("   ");
    }

    @Test
    void testGetCourtVenuesWithBlankMrdBuildingThrows400() {
        assertInvalidRequestMessage(
            () -> retrieveCourtVenues(null, null, null, null, null, null, null,
                                      null, null, null, null, "   ", null),
            "Invalid mrd_building_id. Expected format is MRD-BLD-<digits>"
        );
        verifyNoInteractions(courtVenueServiceMock);
    }

    @Test
    void testGetCourtVenuesWithBlankServiceCodePassesValueToService() {
        retrieveCourtVenues(null, null, "   ", null, null, null, null,
                            null, null, null, null, null, null);

        ArgumentCaptor<String> serviceCodeCaptor = ArgumentCaptor.forClass(String.class);

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetailsV2(
            isNull(),
            isNull(),
            ArgumentCaptor.forClass(Integer.class).capture(),
            serviceCodeCaptor.capture(),
            isNull(),
            isNull(),
            isNull(),
            ArgumentCaptor.forClass(Boolean.class).capture(),
            ArgumentCaptor.forClass(CourtVenueRequestParam.class).capture()
        );
        assertThat(serviceCodeCaptor.getValue()).isEqualTo("   ");
    }

    @Test
    void testGetCourtVenuesWithBlankCourtVenueNamePassesValueToService() {
        retrieveCourtVenues(null, null, null, null, null, null, "   ",
                            null, null, null, null, null, null);

        ArgumentCaptor<String> courtVenueNameCaptor = ArgumentCaptor.forClass(String.class);

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetailsV2(
            isNull(),
            isNull(),
            ArgumentCaptor.forClass(Integer.class).capture(),
            isNull(),
            isNull(),
            isNull(),
            courtVenueNameCaptor.capture(),
            ArgumentCaptor.forClass(Boolean.class).capture(),
            ArgumentCaptor.forClass(CourtVenueRequestParam.class).capture()
        );
        assertThat(courtVenueNameCaptor.getValue()).isEqualTo("   ");
    }

    @Test
    void testGetCourtVenuesWithNumberParamsUsesV1Validation() {
        retrieveCourtVenues(null, null, null, 0, null, null, null,
                            null, null, null, null, null, null);

        ArgumentCaptor<Integer> courtTypeIdCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(courtVenueServiceMock, times(1)).retrieveCourtVenueDetailsV2(
            isNull(),
            isNull(),
            courtTypeIdCaptor.capture(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            ArgumentCaptor.forClass(Boolean.class).capture(),
            ArgumentCaptor.forClass(CourtVenueRequestParam.class).capture()
        );
        assertThat(courtTypeIdCaptor.getValue()).isZero();
    }

    @Test
    void testGetCourtVenuesWithInvalidFilterParamsThrows400() {
        assertInvalidRequestMessage(
            () -> retrieveCourtVenues(null, null, null, null, null, null, null,
                                      "!", null, null, null, null, null),
            "Invalid is_hearing_location. Allowed values are Y OR N"
        );
        assertInvalidRequestMessage(
            () -> retrieveCourtVenues(null, null, null, null, null, null, null,
                                      null, "!", null, null, null, null),
            "Invalid is_case_management_location. Allowed values are Y OR N"
        );
        assertInvalidRequestMessage(
            () -> retrieveCourtVenues(null, null, null, null, null, null, null,
                                      null, null, "Court@", null, null, null),
            "Param contains special characters. ',' comma and '_' underscore allowed only"
        );
        assertInvalidRequestMessage(
            () -> retrieveCourtVenues(null, null, null, null, null, null, null,
                                      null, null, null, "!", null, null),
            "Invalid is_temporary_location. Allowed values are Y OR N"
        );
        assertInvalidRequestMessage(
            () -> retrieveCourtVenues(null, null, null, null, null, null, null,
                                      null, null, null, null, "MRD-BLD-ABC", null),
            "Invalid mrd_building_id. Expected format is MRD-BLD-<digits>"
        );
        assertInvalidRequestMessage(
            () -> retrieveCourtVenues(null, null, null, null, null, null, null,
                                      null, null, null, null, null, "Pending"),
            "Invalid court_status. Allowed values are Open OR Closed"
        );
        verifyNoInteractions(courtVenueServiceMock);
    }

    private void assertInvalidRequestMessage(Runnable request, String expectedMessage) {
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, request::run);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    private ResponseEntity<List<LrdCourtVenueV2Response>> retrieveCourtVenues(
        String epimmsIds,
        String mrdVenueId,
        String serviceCode,
        Integer courtTypeId,
        Integer regionId,
        Integer clusterId,
        String courtVenueName,
        String isHearingLocation,
        String isCaseManagementLocation,
        String locationType,
        String isTemporaryLocation,
        String mrdBuildingId,
        String courtStatus
    ) {
        return lrdCourtVenueV2Controller.retrieveCourtVenues(
            epimmsIds,
            mrdVenueId,
            serviceCode,
            courtTypeId,
            regionId,
            clusterId,
            courtVenueName,
            isHearingLocation,
            isCaseManagementLocation,
            locationType,
            isTemporaryLocation,
            mrdBuildingId,
            courtStatus
        );
    }
}
