package uk.gov.hmcts.reform.lrdapi.provider;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.VersionSelector;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.lrdapi.controllers.LrdApiController;
import uk.gov.hmcts.reform.lrdapi.controllers.LrdCourtVenueController;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtTypeServiceAssoc;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Jurisdiction;
import uk.gov.hmcts.reform.lrdapi.domain.OrgBusinessArea;
import uk.gov.hmcts.reform.lrdapi.domain.OrgSubBusinessArea;
import uk.gov.hmcts.reform.lrdapi.domain.OrgUnit;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.domain.Service;
import uk.gov.hmcts.reform.lrdapi.domain.ServiceToCcdCaseTypeAssoc;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;
import uk.gov.hmcts.reform.lrdapi.repository.CourtTypeServiceAssocRepository;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceToCcdCaseTypeAssocRepositry;
import uk.gov.hmcts.reform.lrdapi.service.impl.CourtVenueServiceImpl;
import uk.gov.hmcts.reform.lrdapi.service.impl.LrdBuildingLocationServiceImpl;
import uk.gov.hmcts.reform.lrdapi.service.impl.LrdServiceImpl;
import uk.gov.hmcts.reform.lrdapi.service.impl.RegionServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Provider("referenceData_location")
@PactBroker(scheme = "${PACT_BROKER_SCHEME:http}",
    host = "${PACT_BROKER_URL:localhost}",
    port = "${PACT_BROKER_PORT:80}", consumerVersionSelectors = {
    @VersionSelector(tag = "master")})
@ContextConfiguration(classes = {LrdApiController.class, LrdCourtVenueController.class, LrdServiceImpl.class,
    LrdBuildingLocationServiceImpl.class, RegionServiceImpl.class, CourtVenueServiceImpl.class})
@TestPropertySource(properties = {"loggingComponentName=LrdApiProviderTest"})
@IgnoreNoPactsToVerify
public class LrdApiProviderTest {

    @MockBean
    ServiceRepository serviceRepository;

    @MockBean
    RegionRepository regionRepository;

    @MockBean
    ServiceToCcdCaseTypeAssocRepositry serviceToCcdCaseTypeAssocRepositry;

    @MockBean
    BuildingLocationRepository buildingLocationRepository;

    @MockBean
    CourtTypeServiceAssocRepository courtTypeServiceAssocRepository;

    @Autowired
    LrdApiController lrdApiController;

    @Autowired
    LrdCourtVenueController lrdCourtVenueController;


    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setControllers(
            lrdApiController, lrdCourtVenueController);
        if (nonNull(context)) {
            context.setTarget(testTarget);
        }

    }

    @State({"Organisational Service details exist"})
    public void toReturnOrganisationalServiceDetails() {
        OrgUnit orgUnit = new OrgUnit(1L, "orgUnit");
        Service service = new Service();
        service.setServiceId(1L);
        service.setOrgUnit(orgUnit);
        service.setServiceCode("AAA1");
        service.setServiceDescription("Insolvency");
        service.setServiceShortDescription("Insolvency");
        OrgSubBusinessArea orgSubBusArea =
            new OrgSubBusinessArea(1L, "OrgSubBusinessArea");
        OrgBusinessArea orgBusinessArea = new OrgBusinessArea(1L, "BusinessArea");
        Jurisdiction jurisdiction = new Jurisdiction(1L, "Jurisdiction");
        service.setOrgBusinessArea(orgBusinessArea);
        service.setOrgSubBusinessArea(orgSubBusArea);
        service.setJurisdiction(jurisdiction);
        service.setLastUpdate(LocalDateTime.now());
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = new ServiceToCcdCaseTypeAssoc();
        serviceToCcdCaseTypeAssoc.setId(1L);
        serviceToCcdCaseTypeAssoc.setCcdCaseType("CCDCASETYPE1");
        serviceToCcdCaseTypeAssoc.setCcdServiceName("CCDSERVICENAME");
        serviceToCcdCaseTypeAssoc.setService(service);
        service.setServiceToCcdCaseTypeAssocs(Collections.singletonList(serviceToCcdCaseTypeAssoc));
        List<Service> services = new ArrayList<>();
        services.add(service);
        when(serviceRepository.findByServiceCode(any())).thenReturn(service);
        when(serviceRepository.findAll()).thenReturn(services);
        when(serviceToCcdCaseTypeAssocRepositry.findByCcdCaseTypeIgnoreCase(any()))
            .thenReturn(serviceToCcdCaseTypeAssoc);
        when(serviceToCcdCaseTypeAssocRepositry.findByCcdServiceNameInIgnoreCase(any()))
            .thenReturn(List.of(serviceToCcdCaseTypeAssoc));
    }

    @State({"Building Location details exist for the request provided"})
    public void toReturnBuildingLocationDetails() {
        Cluster cluster = new Cluster();
        cluster.setClusterId("456");
        cluster.setClusterName("ClusterXYZ");
        cluster.setWelshClusterName("ClusterABC");
        cluster.setCreatedTime(LocalDateTime.now());
        cluster.setUpdatedTime(LocalDateTime.now());

        Region region = new Region();
        region.setCreatedTime(LocalDateTime.now());
        region.setDescription("Region XYZ");
        region.setRegionId("123");
        region.setUpdatedTime(LocalDateTime.now());
        region.setWelshDescription("Region ABC");

        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("17");
        courtType.setCourtType("Immigration and Asylum");

        CourtVenue courtVenue = CourtVenue.builder()
            .courtTypeId("17")
            .courtType(courtType)
            .siteName("Aberdeen Tribunal Hearing Centre 1")
            .openForPublic(Boolean.TRUE)
            .epimmsId("123456789")
            .region(region)
            .courtName("ABERDEEN TRIBUNAL HEARING CENTRE 1")
            .courtStatus("Open")
            .postcode("AB11 6LT")
            .courtAddress("AB1, 48 HUNTLY STREET, ABERDEEN")
            .courtVenueId(1L)
            .build();

        Set<CourtVenue> courtVenues = new HashSet<>();
        courtVenues.add(courtVenue);

        BuildingLocation buildingLocation = BuildingLocation.builder()
            .buildingLocationStatus("OPEN")
            .area("Area 1")
            .buildingLocationId(123L)
            .buildingLocationName("Taylor House Tribunal Hearing Centre")
            .courtFinderUrl("https://testUrl.com")
            .created(LocalDateTime.now())
            .epimmsId("4567")
            .lastUpdated(LocalDateTime.now())
            .postcode("XY2 YY3")
            .region(region)
            .cluster(cluster)
            .address("Address 123")
            .courtVenues(courtVenues)
            .build();

        BuildingLocation buildingLocation2 = BuildingLocation.builder()
            .buildingLocationStatus("CLOSED")
            .area("Area 2")
            .buildingLocationId(1234L)
            .buildingLocationName("Taylor House Tribunal Hearing Centre 2")
            .courtFinderUrl("https://testUrl.com")
            .created(LocalDateTime.now())
            .epimmsId("45678")
            .lastUpdated(LocalDateTime.now())
            .postcode("XY21 YY3")
            .region(region)
            .cluster(cluster)
            .address("Address 123456")
            .courtVenues(courtVenues)
            .build();

        when(buildingLocationRepository.findByBuildingLocationNameIgnoreCase(anyString())).thenReturn(buildingLocation);
        when(buildingLocationRepository.findByEpimmsId(anyList())).thenReturn(List.of(buildingLocation));
        when(buildingLocationRepository.findAll()).thenReturn(List.of(buildingLocation, buildingLocation2));
        when(buildingLocationRepository.findByBuildingLocationStatusOpen()).thenReturn(List.of(buildingLocation));
        when(buildingLocationRepository.findByClusterId(anyString()))
            .thenReturn(List.of(buildingLocation, buildingLocation2));
        when(buildingLocationRepository.findByRegionId(anyString()))
            .thenReturn(List.of(buildingLocation, buildingLocation2));
    }

    @State({"Region Details exist"})
    public void toReturnRegionDetails() {
        Region region = new Region("1", "National", "");
        Region region1 = new Region("2", "London", "");
        List<Region> regions = asList(region, region1);
        when(regionRepository.findByDescriptionInIgnoreCase(any())).thenReturn(regions);
        when(regionRepository.findByRegionIdIn(any())).thenReturn(regions);
        when(regionRepository.findByApiEnabled(any())).thenReturn(regions);
    }


    @State({"Court Venues exist for the service code provided"})
    public void toReturnCourtVenuesByServiceCode() {
        Cluster cluster = new Cluster();
        cluster.setClusterId("456");
        cluster.setClusterName("ClusterXYZ");

        Region region = new Region();
        region.setDescription("Region XYZ");
        region.setRegionId("123");

        BuildingLocation buildingLocation = BuildingLocation.builder()
            .epimmsId("4567")
            .build();

        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("17");
        courtType.setCourtType("Immigration and Asylum");

        CourtVenue courtVenue = CourtVenue.builder()
            .courtVenueId(1L)
            .epimmsId("12345")
            .siteName("siteName")
            .region(region)
            .courtType(courtType)
            .cluster(cluster)
            .openForPublic(Boolean.TRUE)
            .courtAddress("courtAddress")
            .postcode("AB EYZ")
            .phoneNumber("122324234")
            .closedDate(LocalDateTime.now())
            .courtLocationCode("courtLocationCode")
            .dxAddress("dxAddress")
            .courtStatus("Open")
            .courtName("courtName")
            .build();

        courtType.setCourtVenues(Collections.singletonList(courtVenue));

        CourtTypeServiceAssoc courtTypeServiceAssoc = new CourtTypeServiceAssoc();
        courtTypeServiceAssoc.setCourtType(courtType);
        when(courtTypeServiceAssocRepository.findByServiceCode(anyString())).thenReturn(courtTypeServiceAssoc);
    }
}
