package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {

    @Mock
    RegionRepository regionRepositoryMock;

    @InjectMocks
    RegionServiceImpl regionService;

    Region regionMock = mock(Region.class);

    @BeforeEach
    public void setUp() {
        when(regionMock.getRegionId()).thenReturn("2");
        when(regionMock.getDescription()).thenReturn("London");
        when(regionMock.getWelshDescription()).thenReturn("Llundain");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRetrieveRegionDetailsById() {
        when(regionRepositoryMock.findByRegionIdIn(any())).thenReturn(Collections.singletonList(regionMock));

        List<LrdRegionResponse> response =
            (List<LrdRegionResponse>) regionService.retrieveRegionDetails("2", "");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());

    }

    @Test
    @SuppressWarnings("unchecked")
    void testRetrieveRegionDetailsByDescription() {
        when(regionRepositoryMock.findByRegionIdIn(any())).thenReturn(Collections.singletonList(regionMock));

        List<LrdRegionResponse> response =
            (List<LrdRegionResponse>) regionService.retrieveRegionDetails("2", "");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());

        verify(regionRepositoryMock, times(1)).findByRegionIdIn(any());
    }

    @Test
    void testRetrieveRegionByRegionDescription() {
        when(regionRepositoryMock.findByDescriptionInIgnoreCase(any())).thenReturn(asList(regionMock));

        List<LrdRegionResponse> response = regionService.retrieveRegionByRegionDescription("London");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());

        verify(regionRepositoryMock, times(1)).findByDescriptionInIgnoreCase(any());
    }

    @Test
    void testRetrieveRegionByRegionDescriptionCaseInsensitive() {
        when(regionRepositoryMock.findByDescriptionInIgnoreCase(any())).thenReturn(asList(regionMock));

        List<LrdRegionResponse> response = regionService.retrieveRegionByRegionDescription("LoNdOn");

        assertNotNull(response);

        verify(regionRepositoryMock, times(1)).findByDescriptionInIgnoreCase(any());
    }

    @Test
    void testRetrieveRegionByRegionDescriptionThrows404ForUnknownDescription() {
        assertThrows(ResourceNotFoundException.class, () -> {
            regionService.retrieveRegionByRegionDescription("Unknown Description");
        });
    }
}
