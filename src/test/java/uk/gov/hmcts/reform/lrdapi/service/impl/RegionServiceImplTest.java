package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegionServiceImplTest {

    @Mock
    RegionRepository regionRepositoryMock;

    @InjectMocks
    RegionServiceImpl regionService;

    Region regionMock = mock(Region.class);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(regionMock.getRegionId()).thenReturn("2");
        when(regionMock.getDescription()).thenReturn("London");
        when(regionMock.getWelshDescription()).thenReturn("Llundain");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRetrieveRegionDetailsById() {
        when(regionRepositoryMock.findByRegionIdIn(any())).thenReturn(asList(regionMock));

        List<LrdRegionResponse> response =
            (List<LrdRegionResponse>) regionService.retrieveRegionDetails("2", "");

        assertThat(response).isNotNull();
        assertThat(response.get(0).getRegionId()).isEqualTo("2");
        assertThat(response.get(0).getDescription()).isEqualTo("London");
        assertThat(response.get(0).getWelshDescription()).isEqualTo("Llundain");

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRetrieveRegionDetailsByDescription() {
        when(regionRepositoryMock.findByRegionIdIn(any())).thenReturn(asList(regionMock));

        List<LrdRegionResponse> response =
            (List<LrdRegionResponse>) regionService.retrieveRegionDetails("2", "");

        assertThat(response).isNotNull();
        assertThat(response.get(0).getRegionId()).isEqualTo("2");
        assertThat(response.get(0).getDescription()).isEqualTo("London");
        assertThat(response.get(0).getWelshDescription()).isEqualTo("Llundain");

        verify(regionRepositoryMock, times(1)).findByRegionIdIn(any());
    }

    @Test
    public void testRetrieveRegionByRegionDescription() {
        when(regionRepositoryMock.findByDescriptionInIgnoreCase(any())).thenReturn(asList(regionMock));

        List<LrdRegionResponse> response = regionService.retrieveRegionByRegionDescription("London");

        assertThat(response).isNotNull();
        assertThat(response.get(0).getRegionId()).isEqualTo("2");
        assertThat(response.get(0).getDescription()).isEqualTo("London");
        assertThat(response.get(0).getWelshDescription()).isEqualTo("Llundain");

        verify(regionRepositoryMock, times(1)).findByDescriptionInIgnoreCase(any());
    }

    @Test
    public void testRetrieveRegionByRegionDescriptionCaseInsensitive() {
        when(regionRepositoryMock.findByDescriptionInIgnoreCase(any())).thenReturn(asList(regionMock));

        List<LrdRegionResponse> response = regionService.retrieveRegionByRegionDescription("LoNdOn");

        assertThat(response).isNotNull();

        verify(regionRepositoryMock, times(1)).findByDescriptionInIgnoreCase(any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRetrieveRegionByRegionDescriptionThrows404ForUnknownDescription() {
        regionService.retrieveRegionByRegionDescription("Unknown Description");
    }
}
