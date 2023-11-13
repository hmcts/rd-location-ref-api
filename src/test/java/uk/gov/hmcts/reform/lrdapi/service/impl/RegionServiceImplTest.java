package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;

import java.util.Collections;
import java.util.List;

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

        var response = (List<LrdRegionResponse>) regionService.retrieveRegionDetails("2", "");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());

    }

    @Test
    @SuppressWarnings("unchecked")
    void testRetrieveRegionDetailsByDescription() {
        when(regionRepositoryMock.findByDescriptionInIgnoreCase(any()))
            .thenReturn(Collections.singletonList(regionMock));

        var response = (List<LrdRegionResponse>) regionService.retrieveRegionDetails("", "London");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());

        verify(regionRepositoryMock, times(1)).findByDescriptionInIgnoreCase(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRetrieveRegionDetailsWithoutRegionAndDescription() {
        when(regionRepositoryMock.findByApiEnabled(true)).thenReturn(Collections.singletonList(regionMock));

        var response = (List<LrdRegionResponse>) regionService.retrieveRegionDetails("", "");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());
    }

    @Test
    void testRetrieveRegionByRegionDescription() {
        when(regionRepositoryMock.findByDescriptionInIgnoreCase(any()))
            .thenReturn(Collections.singletonList(regionMock));

        var response = (List<LrdRegionResponse>) regionService.retrieveRegionByRegionDescription("London");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());

        verify(regionRepositoryMock, times(1)).findByDescriptionInIgnoreCase(any());
    }

    @Test
    void testRetrieveRegionByRegionDescriptionInvalidIdList() {
        assertThrows(InvalidRequestException.class, () -> {
            regionService.retrieveRegionByRegionDescription("{London}, {National}");
        });
    }

    @Test
    void testRetrieveRegionByRegionDescriptionCaseInsensitive() {
        when(regionRepositoryMock.findByDescriptionInIgnoreCase(any()))
            .thenReturn(Collections.singletonList(regionMock));

        var response = (List<LrdRegionResponse>) regionService.retrieveRegionByRegionDescription("LoNdOn");

        assertNotNull(response);

        verify(regionRepositoryMock, times(1)).findByDescriptionInIgnoreCase(any());
    }

    @Test
    void testRetrieveRegionByRegionIdAll() {
        when(regionRepositoryMock.findAll()).thenReturn(Collections.singletonList(regionMock));

        var response = (List<LrdRegionResponse>) regionService.retrieveRegionByRegionId("ALL");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());

        verify(regionRepositoryMock, times(1)).findAll();
    }

    @Test
    void testRetrieveRegionByRegionIdListAll() {
        when(regionRepositoryMock.findAll()).thenReturn(Collections.singletonList(regionMock));

        var response = (List<LrdRegionResponse>) regionService.retrieveRegionByRegionId("ALL,1");

        assertNotNull(response);
        assertEquals("2", response.get(0).getRegionId());
        assertEquals("London", response.get(0).getDescription());
        assertEquals("Llundain", response.get(0).getWelshDescription());

        verify(regionRepositoryMock, times(1)).findAll();
    }

    @Test
    void testRetrieveRegionByRegionIdEmptyRegionList() {
        when(regionRepositoryMock.findByRegionIdIn(any())).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            regionService.retrieveRegionByRegionId("123, 456");
        });

        verify(regionRepositoryMock, times(1)).findByRegionIdIn(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{121},{2332},{1233432}","ALL,London","London,National"})
    void testRetrieveRegionByRegionIdInvalidIdList(String regions) {
        assertThrows(InvalidRequestException.class, () -> {
            regionService.retrieveRegionByRegionId(regions);
        });
    }

    @Test
    void testRetrieveRegionByRegionDescriptionThrows404ForUnknownDescription() {
        assertThrows(ResourceNotFoundException.class, () -> {
            regionService.retrieveRegionByRegionDescription("Unknown Description");
        });
    }
}
