package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;

import static org.assertj.core.api.Assertions.assertThat;
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
        when(regionRepositoryMock.findByDescription("London")).thenReturn(regionMock);
    }

    @Test
    public void testRetrieveRegionByRegionDescription() {

        LrdRegionResponse response = regionService.retrieveRegionByRegionDescription("London");

        assertThat(response).isNotNull();
        assertThat(response.getRegionId()).isEqualTo("2");
        assertThat(response.getDescription()).isEqualTo("London");
        assertThat(response.getWelshDescription()).isEqualTo("Llundain");

        verify(regionRepositoryMock, times(1)).findByDescription("London");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRetrieveRegionByRegionDescriptionThrows404ForInvalidDescription() {
        regionService.retrieveRegionByRegionDescription("Invalid Description");
    }

}
