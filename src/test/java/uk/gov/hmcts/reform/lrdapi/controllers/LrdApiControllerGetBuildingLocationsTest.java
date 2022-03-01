package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LrdApiControllerGetBuildingLocationsTest {

    @Mock
    ILrdBuildingLocationService lrdBuildingLocationService;

    @InjectMocks
    LrdApiController lrdApiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBuildingLocations_ValidEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("111", "", "", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("111", "", "", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("111", "", "", "");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // testGetBuildingLocations_AllEpimmsIdGiven_ShouldReturnStatusCode200
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("ALL", "", "", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity1 =
            lrdApiController.retrieveBuildingLocationDetails("ALL", "", "", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("ALL", "", "", "");
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());

        // testGetBuildingLocations_lowerAllEpimmsIdGiven_ShouldReturnStatusCode200
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("all", "", "", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity2 =
            lrdApiController.retrieveBuildingLocationDetails("all", "", "", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("all", "", "", "");
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());

        // testGetBuildingLocations_AllAndEpimmsIdGiven_ShouldReturnStatusCode200
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("all, 111", "", "", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity3 =
            lrdApiController.retrieveBuildingLocationDetails("all, 111", "", "", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("all, 111", "", "", "");
        assertEquals(HttpStatus.OK, responseEntity3.getStatusCode());


        // testGetBuildingLocations_EpimmsIdWithASpaceGiven_ShouldReturnStatusCode200
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails(" 111 ", "", "", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity4 =
            lrdApiController.retrieveBuildingLocationDetails(" 111 ", "", "", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails(" 111 ", "", "", "");
        assertEquals(HttpStatus.OK, responseEntity4.getStatusCode());

        // testGetBuildingLocations_ValidMultipleEpimmsIdGiven_ShouldReturnStatusCode200
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("111,qwerty, qwerty_123",
                                                                        "", "", ""
        ))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity5 =
            lrdApiController.retrieveBuildingLocationDetails("111,qwerty, qwerty_123",
                                                             "", "", ""
            );
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("111,qwerty, qwerty_123", "", "", "");
        assertEquals(HttpStatus.OK, responseEntity5.getStatusCode());
    }

    @Test
    void testGetBuildingLocations_1Valid1InvalidEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("!@£$%,qwerty", "", "", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("!@£$%,qwerty", "", "", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("!@£$%,qwerty", "", "", "");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetBuildingLocations_StringEpimsIdGiven_ShouldReturnStatusCode200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("QWERTY", "", "", "");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("QWERTY", "", "", "");
    }

    @Test
    void testGetBuildingLocations_AlphaNumericEpimsIdGiven_ShouldReturnStatusCode200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("1234QWERTY", "", "", "");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("1234QWERTY", "", "", "");
    }

    @Test
    void testGetBuildingLocations_MultipleParamsGiven_ShouldReturnStatusCode400() {
        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            lrdApiController.retrieveBuildingLocationDetails("1234QWERTY", "", "1", "");
        });
        assertNotNull(exception);
        assertEquals("Please provide only 1 of 4 values/params: [1234QWERTY, , 1, ]",
                     exception.getMessage());
    }

}
