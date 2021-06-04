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
import uk.gov.hmcts.reform.lrdapi.controllers.LrdApiController;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.service.LrdBuildingLocationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LrdApiControllerGetBuildingLocationsTest {

    @Mock
    LrdBuildingLocationService lrdBuildingLocationService;

    @InjectMocks
    LrdApiController lrdApiController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBuildingLocations_ValidEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationByEpimsId(anyString()))
            .thenReturn(new LrdBuildingLocationResponse());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("111");
        verify(lrdBuildingLocationService, times(1)).retrieveBuildingLocationByEpimsId(anyString());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_EpimmsIdWithASpaceGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationByEpimsId(anyString()))
            .thenReturn(new LrdBuildingLocationResponse());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId(" 111 ");
        verify(lrdBuildingLocationService, times(1)).retrieveBuildingLocationByEpimsId(anyString());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test(expected = InvalidRequestException.class)
    public void testGetBuildingLocations_NullEpimmsIdGiven_ShouldReturnStatusCode400() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId(null);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(lrdBuildingLocationService, times(0)).retrieveBuildingLocationByEpimsId(anyString());
    }

    @Test(expected = InvalidRequestException.class)
    public void testGetBuildingLocations_StringEpimmsIdGiven_ShouldReturnStatusCode400() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("QWERTY");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(lrdBuildingLocationService, times(0)).retrieveBuildingLocationByEpimsId(anyString());
    }

    @Test(expected = InvalidRequestException.class)
    public void testGetBuildingLocations_AlphaNumericEpimmsIdGiven_ShouldReturnStatusCode400() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("1234QWERTY");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(lrdBuildingLocationService, times(0)).retrieveBuildingLocationByEpimsId(anyString());
    }

    @Test(expected = InvalidRequestException.class)
    public void testGetBuildingLocations_SpecialCharactersInEpimmsIdGiven_ShouldReturnStatusCode400() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetailsByEpimsId("@£$%^");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(lrdBuildingLocationService, times(0)).retrieveBuildingLocationByEpimsId(anyString());
    }

}
