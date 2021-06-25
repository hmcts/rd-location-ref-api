package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LrdApiControllerGetBuildingLocationsTest {

    @Mock
    ILrdBuildingLocationService lrdBuildingLocationService;

    @InjectMocks
    LrdApiController lrdApiController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBuildingLocations_ValidSingleEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("111",""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("111","");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("111", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_AllEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("ALL", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("ALL", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("ALL", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_lowerAllEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("all", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("all", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("all", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_EpimmsIdWithASpaceGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails(" 111 ", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId(" 111 ", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails(" 111 ", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_ValidMultipleEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("111,qwerty, qwerty_123",
                                                                        ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("111,qwerty, qwerty_123",
                                                                      "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("111,qwerty, qwerty_123", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_1Valid1InvalidEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("!@£$%,qwerty",""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("!@£$%,qwerty", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("!@£$%,qwerty", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_StringEpimsIdGiven_ShouldReturnStatusCode200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("QWERTY", "");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("QWERTY", "");
    }

    @Test
    public void testGetBuildingLocations_AlphaNumericEpimsIdGiven_ShouldReturnStatusCode200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("1234QWERTY", "");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("1234QWERTY", "");
    }

}
