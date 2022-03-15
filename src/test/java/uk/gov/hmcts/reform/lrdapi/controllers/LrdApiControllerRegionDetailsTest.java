package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;
import uk.gov.hmcts.reform.lrdapi.service.RegionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LrdApiControllerRegionDetailsTest {

    @InjectMocks
    private LrdApiController lrdApiController;

    @Mock
    ILrdBuildingLocationService lrdBuildingLocationService;

    @Mock
    LrdService lrdServiceMock;

    @Mock
    RegionService regionServiceMock;

    @Test
    void testGetRegionDetails_ByDescription_Returns200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveRegionDetails("London", "");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(regionServiceMock, times(1)).retrieveRegionDetails("","London");
    }

    @Test
    void testGetRegionDetails_ByDescriptionAndId_Returns400() {
        Exception exception = assertThrows(InvalidRequestException.class, () ->
            lrdApiController.retrieveRegionDetails("af", "213"));

        assertNotNull(exception);
        assertEquals("Please provide only 1 of 2 values of params: region, regionId", exception.getMessage());
    }

    @Test
    void testGetRegionDetails_ByEmptyDescriptionAndId_Returns400() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveRegionDetails("", "");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(regionServiceMock, times(1)).retrieveRegionDetails("","");
    }
}
