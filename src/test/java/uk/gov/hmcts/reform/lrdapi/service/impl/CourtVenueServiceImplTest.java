package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtTypeServiceAssoc;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.repository.CourtTypeServiceAssocRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CourtVenueServiceImplTest {

    @InjectMocks
    CourtVenueServiceImpl courtVenueService;

    @Mock
    CourtTypeServiceAssocRepository courtTypeServiceAssocRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveCourtVenuesByServiceCode() {
        CourtType courtType = CourtType.builder()
            .courtType("courtType")
            .courtTypeId("10")
            .build();

        BuildingLocation buildingLocation = BuildingLocation.builder()
            .epimmsId("12234")
            .build();

        CourtVenue courtVenue = CourtVenue.builder()
            .courtVenueId(1L)
            .courtType(courtType)
            .buildingLocation(buildingLocation)
            .openForPublic(Boolean.TRUE)
            .build();

        List<CourtVenue> courtVenues = Collections.singletonList(courtVenue);
        courtType.setCourtVenues(courtVenues);
        CourtTypeServiceAssoc courtTypeServiceAssoc = new CourtTypeServiceAssoc();
        courtTypeServiceAssoc.setCourtType(courtType);

        when(courtTypeServiceAssocRepository.findByServiceCode(anyString())).thenReturn(courtTypeServiceAssoc);

        LrdCourtVenuesByServiceCodeResponse response
            = courtVenueService.retrieveCourtVenuesByServiceCode("ABC1");

        assertThat(response).isNotNull();
        assertEquals("ABC1", response.getServiceCode());
        assertEquals(courtType.getCourtTypeId(), response.getCourtTypeId());
        assertEquals(courtType.getCourtType(), response.getCourtType());
        assertNotNull(response.getCourtVenues());
        assertNull(response.getWelshCourtType());

        verify(courtTypeServiceAssocRepository, times(1)).findByServiceCode("ABC1");
    }

    @Test
    public void testRetrieveCourtVenuesByServiceCode_WithInvalidServiceCode() {
        when(courtTypeServiceAssocRepository.findByServiceCode(anyString())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courtVenueService.retrieveCourtVenuesByServiceCode("ABC1"));
    }

    @Test
    public void testRetrieveCourtVenuesByServiceCode_WithNoCourtVenues() {
        CourtTypeServiceAssoc courtTypeServiceAssoc = new CourtTypeServiceAssoc();
        CourtType courtType = new CourtType();
        courtTypeServiceAssoc.setCourtType(courtType);

        when(courtTypeServiceAssocRepository.findByServiceCode(anyString())).thenReturn(courtTypeServiceAssoc);

        assertThrows(ResourceNotFoundException.class, () -> courtVenueService.retrieveCourtVenuesByServiceCode("ABC1"));
    }




}
