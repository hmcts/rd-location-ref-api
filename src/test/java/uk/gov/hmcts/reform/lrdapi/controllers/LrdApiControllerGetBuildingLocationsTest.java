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
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
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
        when(lrdBuildingLocationService.retrieveBuildingLocationByEpimsIds(anyList()))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("111");
        verify(lrdBuildingLocationService, times(1)).retrieveBuildingLocationByEpimsIds(anyList());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_AllEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.getAllBuildingLocations())
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("ALL");
        verify(lrdBuildingLocationService, times(1)).getAllBuildingLocations();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_lowerAllEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.getAllBuildingLocations())
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("all");
        verify(lrdBuildingLocationService, times(1)).getAllBuildingLocations();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_EpimmsIdWithASpaceGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationByEpimsIds(anyList()))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId(" 111 ");
        verify(lrdBuildingLocationService, times(1)).retrieveBuildingLocationByEpimsIds(anyList());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_ValidMultipleEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationByEpimsIds(anyList()))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("111,qwerty, qwerty_123");
        verify(lrdBuildingLocationService, times(1)).retrieveBuildingLocationByEpimsIds(anyList());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_1Valid1InvalidEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationByEpimsIds(anyList()))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("!@£$%,qwerty");
        verify(lrdBuildingLocationService, times(1)).retrieveBuildingLocationByEpimsIds(anyList());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test(expected = InvalidRequestException.class)
    public void testGetBuildingLocations_AllInvalidEpimmsIdGiven_ShouldReturnStatusCode400() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("!@£$%,&*(");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(lrdBuildingLocationService, times(0)).getAllBuildingLocations();
        verify(lrdBuildingLocationService, times(0))
            .retrieveBuildingLocationByEpimsIds(anyList());
    }

    @Test(expected = InvalidRequestException.class)
    public void testGetBuildingLocations_SingleInvalidEpimmsIdGiven_ShouldReturnStatusCode400() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("abc 123");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(lrdBuildingLocationService, times(0)).getAllBuildingLocations();
        verify(lrdBuildingLocationService, times(0))
            .retrieveBuildingLocationByEpimsIds(anyList());
    }

    @Test
    public void testGetBuildingLocations_StringEpimsIdGiven_ShouldReturnStatusCode200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("QWERTY");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lrdBuildingLocationService, times(1)).retrieveBuildingLocationByEpimsIds(anyList());
    }

    @Test
    public void testGetBuildingLocations_AlphaNumericEpimsIdGiven_ShouldReturnStatusCode200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("1234QWERTY");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lrdBuildingLocationService, times(1)).retrieveBuildingLocationByEpimsIds(anyList());
    }

    @Test(expected = InvalidRequestException.class)
    public void testGetBuildingLocations_OneSpecialCharEpimsIdGiven_ShouldReturnStatusCode400() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("@£$%^");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(lrdBuildingLocationService, times(0)).getAllBuildingLocations();
        verify(lrdBuildingLocationService, times(0)).retrieveBuildingLocationByEpimsIds(anyList());
    }

}
