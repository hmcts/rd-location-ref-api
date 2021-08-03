package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtTypeServiceAssoc;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.CourtTypeServiceAssocRepository;
import uk.gov.hmcts.reform.lrdapi.repository.CourtVenueRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CourtVenueServiceImplTest {

    @InjectMocks
    CourtVenueServiceImpl courtVenueService;

    @Mock
    CourtTypeServiceAssocRepository courtTypeServiceAssocRepository;
    @Mock
    CourtVenueRepository courtVenueRepository;

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

        CourtVenue courtVenue = CourtVenue.builder()
            .courtVenueId(1L)
            .courtType(courtType)
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

    @Test
    public void test_RetrieveCourtVenuesByEpimsIDs_OneIdPassed() {

        when(courtVenueRepository.findByEpimmsIdIn(anyList())).thenReturn(prepareCourtVenue());

        List<LrdCourtVenueResponse> courtVenueResponses =
            courtVenueService
                .retrieveCourtVenueDetails("123", null,  null, null, null);

        LrdCourtVenueResponse courtVenueResponse = courtVenueResponses.get(0);

        verifySingleResponse(courtVenueResponse);
    }

    @Test
    public void test_RetrieveBuildingLocationsByEpimsIDs_MultipleIdsPassed() {

        when(courtVenueRepository.findByEpimmsIdIn(anyList())).thenReturn(prepareMultiCourtVenueResponse());
        List<LrdCourtVenueResponse> courtVenueResponses =
            courtVenueService
                .retrieveCourtVenueDetails("123,1234", null,  null, null, null);
        verifyMultiResponse(courtVenueResponses);
    }

    @Test
    public void testGetAllCourtVenues_EpimmsIdAll() {
        when(courtVenueRepository.findAll())
            .thenReturn(prepareMultiCourtVenueResponse());
        List<LrdCourtVenueResponse> courtVenueResponses =
            courtVenueService
                .retrieveCourtVenueDetails("All", null,  null, null, null);
        verifyMultiResponse(courtVenueResponses);
    }

    @Test
    public void testGetAllCourtVenues() {
        when(courtVenueRepository.findAllWithOpenCourtStatus())
            .thenReturn(prepareCourtVenue());
        List<LrdCourtVenueResponse> courtVenueResponses =
            courtVenueService
                .retrieveCourtVenueDetails("", null,  null, null, null);
        verifySingleResponse(courtVenueResponses.get(0));
    }

    @Test
    public void test_RetrieveCourtVenuesByRegionId() {

        when(courtVenueRepository.findByRegionIdWithOpenCourtStatus(anyString())).thenReturn(prepareCourtVenue());

        List<LrdCourtVenueResponse> courtVenueResponses =
            courtVenueService
                .retrieveCourtVenueDetails("", null,  1, null, null);

        LrdCourtVenueResponse courtVenueResponse = courtVenueResponses.get(0);

        verifySingleResponse(courtVenueResponse);
    }

    @Test
    public void test_RetrieveCourtVenuesByClusterId() {

        when(courtVenueRepository.findByClusterIdWithOpenCourtStatus(anyString())).thenReturn(prepareCourtVenue());

        List<LrdCourtVenueResponse> courtVenueResponses =
            courtVenueService
                .retrieveCourtVenueDetails("", null,  null, 1, null);

        LrdCourtVenueResponse courtVenueResponse = courtVenueResponses.get(0);

        verifySingleResponse(courtVenueResponse);
    }

    @Test
    public void test_RetrieveCourtVenuesByCourtTypeId() {

        when(courtVenueRepository.findByCourtTypeIdWithOpenCourtStatus(anyString())).thenReturn(prepareCourtVenue());

        List<LrdCourtVenueResponse> courtVenueResponses =
            courtVenueService
                .retrieveCourtVenueDetails("", 1,  null, null, null);

        LrdCourtVenueResponse courtVenueResponse = courtVenueResponses.get(0);

        verifySingleResponse(courtVenueResponse);
    }

    @Test
    public void test_RetrieveCourtVenuesByCourtVenueName() {

        when(courtVenueRepository.findByCourtVenueNameOrSiteName(anyString())).thenReturn(prepareCourtVenue());

        List<LrdCourtVenueResponse> courtVenueResponses =
            courtVenueService
                .retrieveCourtVenueDetails("", null,  null, null, "Court ABC");

        LrdCourtVenueResponse courtVenueResponse = courtVenueResponses.get(0);

        verifySingleResponse(courtVenueResponse);
    }

    @Test
    public void test_RetrieveCourtVenuesByEpimmsId_NotFound() {
        when(courtVenueRepository.findByEpimmsIdIn(anyList())).thenReturn(null);
        Assert.assertThrows(ResourceNotFoundException.class, () -> courtVenueService
            .retrieveCourtVenueDetails("123", null,  null, null, null));

        verify(courtVenueRepository, times(1)).findByEpimmsIdIn(anyList());
        verify(courtVenueRepository, times(0)).findByRegionIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0))
            .findByClusterIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0)).findAll();
        verify(courtVenueRepository, times(0)).findByCourtTypeIdWithOpenCourtStatus(anyString());
    }

    @Test
    public void test_RetrieveCourtVenuesByClusterId_NotFound() {
        when(courtVenueRepository.findByClusterIdWithOpenCourtStatus(anyString())).thenReturn(null);
        Assert.assertThrows(ResourceNotFoundException.class, () -> courtVenueService
            .retrieveCourtVenueDetails("", null,  null, 1, null));

        verify(courtVenueRepository, times(0)).findByEpimmsIdIn(anyList());
        verify(courtVenueRepository, times(0)).findByRegionIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(1))
            .findByClusterIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0)).findAll();
        verify(courtVenueRepository, times(0)).findByCourtTypeIdWithOpenCourtStatus(anyString());
    }

    @Test
    public void test_RetrieveCourtVenuesByRegionId_NotFound() {
        when(courtVenueRepository.findByRegionIdWithOpenCourtStatus(anyString())).thenReturn(null);
        Assert.assertThrows(ResourceNotFoundException.class, () -> courtVenueService
            .retrieveCourtVenueDetails("", null,  1, null, null));

        verify(courtVenueRepository, times(0)).findByEpimmsIdIn(anyList());
        verify(courtVenueRepository, times(1)).findByRegionIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0))
            .findByClusterIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0)).findAll();
        verify(courtVenueRepository, times(0)).findByCourtTypeIdWithOpenCourtStatus(anyString());
    }

    @Test
    public void test_RetrieveCourtVenuesByCourtTypeId_NotFound() {
        when(courtVenueRepository.findByCourtTypeIdWithOpenCourtStatus(anyString())).thenReturn(null);
        Assert.assertThrows(ResourceNotFoundException.class, () -> courtVenueService
            .retrieveCourtVenueDetails("", 1,  null, null, null));

        verify(courtVenueRepository, times(0)).findByEpimmsIdIn(anyList());
        verify(courtVenueRepository, times(0)).findByRegionIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0))
            .findByClusterIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0)).findAll();
        verify(courtVenueRepository, times(0)).findAllWithOpenCourtStatus();
        verify(courtVenueRepository, times(1)).findByCourtTypeIdWithOpenCourtStatus(anyString());
    }

    @Test
    public void test_RetrieveCourtVenuesByCourtVenueName_NotFound() {
        when(courtVenueRepository.findByCourtTypeIdWithOpenCourtStatus(anyString())).thenReturn(null);
        Assert.assertThrows(ResourceNotFoundException.class, () -> courtVenueService
            .retrieveCourtVenueDetails("", null,  null, null, "test-name"));

        verify(courtVenueRepository, times(0)).findByEpimmsIdIn(anyList());
        verify(courtVenueRepository, times(0)).findByRegionIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0))
            .findByClusterIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(0)).findAll();
        verify(courtVenueRepository, times(0)).findAllWithOpenCourtStatus();
        verify(courtVenueRepository, times(0)).findByCourtTypeIdWithOpenCourtStatus(anyString());
        verify(courtVenueRepository, times(1)).findByCourtVenueNameOrSiteName(anyString());
    }

    private void verifyMultiResponse(List<LrdCourtVenueResponse> courtVenueResponses) {
        assertThat(courtVenueResponses).hasSize(2);

        courtVenueResponses.forEach(courtVenueResponse -> {
            if (courtVenueResponse.getEpimmsId().equalsIgnoreCase("123")) {
                assertThat(courtVenueResponse.getEpimmsId()).isEqualTo("123");
                assertThat(courtVenueResponse.getClusterId()).isEqualTo("1");
                assertThat(courtVenueResponse.getRegionId()).isEqualTo("regionId");
                assertThat(courtVenueResponse.getRegion()).isEqualTo("region");
                assertThat(courtVenueResponse.getClusterId()).isEqualTo("1");
                assertThat(courtVenueResponse.getClusterName()).isEqualTo("cluster name");
                assertThat(courtVenueResponse.getPostcode()).isEqualTo("postcode");
            } else {
                assertThat(courtVenueResponse.getEpimmsId()).isEqualTo("1234");
                assertThat(courtVenueResponse.getClusterId()).isEqualTo("1");
                assertThat(courtVenueResponse.getRegionId()).isEqualTo("regionId");
                assertThat(courtVenueResponse.getRegion()).isEqualTo("region");
                assertThat(courtVenueResponse.getClusterId()).isEqualTo("1");
                assertThat(courtVenueResponse.getClusterName()).isEqualTo("cluster name");
                assertThat(courtVenueResponse.getPostcode()).isEqualTo("postcode1");
            }
        });
    }

    private void verifySingleResponse(LrdCourtVenueResponse lrdCourtVenueResponse) {
        assertThat(lrdCourtVenueResponse.getEpimmsId()).isEqualTo("123");
        assertThat(lrdCourtVenueResponse.getClusterId()).isEqualTo("1");
        assertThat(lrdCourtVenueResponse.getRegionId()).isEqualTo("regionId");
        assertThat(lrdCourtVenueResponse.getRegion()).isEqualTo("region");
        assertThat(lrdCourtVenueResponse.getClusterId()).isEqualTo("1");
        assertThat(lrdCourtVenueResponse.getClusterName()).isEqualTo("cluster name");
        assertThat(lrdCourtVenueResponse.getPostcode()).isEqualTo("postcode");
        assertThat(lrdCourtVenueResponse.getCourtName()).isEqualTo("Court ABC");
        assertThat(lrdCourtVenueResponse.getSiteName()).isEqualTo("COURT ABC");
    }

    private List<CourtVenue> prepareCourtVenue() {

        var courtVenues = new ArrayList<CourtVenue>();

        courtVenues.add(CourtVenue.builder()
                            .courtVenueId(5L)
                            .epimmsId("123")
                            .region(getRegion())
                            .cluster(getCluster())
                            .postcode("postcode")
                            .clusterId("1")
                            .courtName("courtName")
                            .openForPublic(true)
                            .courtType(getCourtType())
                            .courtName("Court ABC")
                            .siteName("COURT ABC")
                            .build());

        return courtVenues;
    }

    private List<CourtVenue> prepareMultiCourtVenueResponse() {
        var courtVenues = new ArrayList<CourtVenue>();
        courtVenues.addAll(prepareCourtVenue());
        courtVenues.add(CourtVenue.builder()
                            .courtVenueId(5L)
                            .epimmsId("1234")
                            .region(getRegion())
                            .cluster(getCluster())
                            .postcode("postcode1")
                            .clusterId("2")
                            .courtName("courtName1")
                            .courtType(getCourtType())
                            .openForPublic(true)
                            .build());

        return courtVenues;
    }


    private Cluster getCluster() {
        Cluster cluster = new Cluster();
        cluster.setClusterId("1");
        cluster.setClusterName("cluster name");
        return cluster;
    }

    private Region getRegion() {
        Region region = new Region();
        region.setRegionId("regionId");
        region.setDescription("region");
        return region;
    }

    private CourtType getCourtType() {
        CourtType courtType = new CourtType();
        courtType.setCourtType("courtType");
        courtType.setCourtTypeId("courtTypeId");
        return courtType;
    }
}
