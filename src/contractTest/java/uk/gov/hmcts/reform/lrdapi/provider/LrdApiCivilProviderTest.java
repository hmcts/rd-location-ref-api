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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.lrdapi.controllers.LrdApiController;
import uk.gov.hmcts.reform.lrdapi.controllers.LrdCourtVenueController;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;
import uk.gov.hmcts.reform.lrdapi.repository.CourtTypeServiceAssocRepository;
import uk.gov.hmcts.reform.lrdapi.repository.CourtVenueRepository;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceToCcdCaseTypeAssocRepositry;
import uk.gov.hmcts.reform.lrdapi.service.impl.CourtVenueServiceImpl;
import uk.gov.hmcts.reform.lrdapi.service.impl.LrdBuildingLocationServiceImpl;
import uk.gov.hmcts.reform.lrdapi.service.impl.LrdServiceImpl;
import uk.gov.hmcts.reform.lrdapi.service.impl.RegionServiceImpl;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Provider("referenceData_location_civil_service")
@PactBroker(scheme = "${PACT_BROKER_SCHEME:http}",
    host = "${PACT_BROKER_URL:localhost}",
    port = "${PACT_BROKER_PORT:80}", consumerVersionSelectors = {@VersionSelector(tag = "master")})
@ContextConfiguration(classes = {LrdApiController.class, LrdCourtVenueController.class, LrdServiceImpl.class,
    LrdBuildingLocationServiceImpl.class, RegionServiceImpl.class, CourtVenueServiceImpl.class})
@TestPropertySource(properties = {"loggingComponentName=LrdApiCivilProviderTest"})
@IgnoreNoPactsToVerify
public class LrdApiCivilProviderTest {

    @MockitoBean
    ServiceRepository serviceRepository;

    @MockitoBean
    RegionRepository regionRepository;

    @MockitoBean
    ServiceToCcdCaseTypeAssocRepositry serviceToCcdCaseTypeAssocRepositry;

    @MockitoBean
    BuildingLocationRepository buildingLocationRepository;

    @MockitoBean
    CourtTypeServiceAssocRepository courtTypeServiceAssocRepository;

    @MockitoBean
    CourtVenueRepository courtVenueRepository;

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

    @State({"There are court locations to be returned"})
    public void toReturnCourtVenuesBySearchString() {
        Region region = getRegion();

        CourtType courtType = getCourtType();

        var courtVenues = getMultipleCourtVenues(region, courtType);

        courtType.setCourtVenues(courtVenues);

        when(courtVenueRepository.findByCourtTypeIdWithOpenCourtStatus(any())).thenReturn(courtVenues);
    }

    private List<CourtVenue> getMultipleCourtVenues(Region region, CourtType courtType) {
        CourtVenue firstCourtVenue = CourtVenue.builder()
            .siteName("siteNameTest123")
            .region(region)
            .courtType(courtType)
            .courtVenueId(12345L)
            .epimmsId("epimmsIdTest123")
            .parentLocation("parentLocationTest123")
            .courtAddress("courtAddressTest123")
            .phoneNumber("phoneNumberTest123")
            .postcode("postcodeTest123")
            .courtLocationCode("courtLocationCodeTest123")
            .courtStatus("Open")
            .isHearingLocation("Y")
            .isCaseManagementLocation("Y")
            .isTemporaryLocation("N")
            .locationType("locationType")
            .courtName("courtNameTest123")
            .venueName("venueNameTest123")
            .build();

        return List.of(firstCourtVenue);
    }

    private CourtType getCourtType() {
        CourtType courtType = new CourtType();
        courtType.setCourtTypeId("courtTypeIdTest123");
        courtType.setTypeOfCourt("courtTypeTest123");
        return courtType;
    }

    private Region getRegion() {
        Region region = new Region();
        region.setDescription("regionTest123");
        region.setRegionId("regionIdTest123");
        return region;
    }

}
