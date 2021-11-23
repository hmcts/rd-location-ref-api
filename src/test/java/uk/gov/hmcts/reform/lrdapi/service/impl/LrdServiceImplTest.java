package uk.gov.hmcts.reform.lrdapi.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Jurisdiction;
import uk.gov.hmcts.reform.lrdapi.domain.OrgBusinessArea;
import uk.gov.hmcts.reform.lrdapi.domain.OrgSubBusinessArea;
import uk.gov.hmcts.reform.lrdapi.domain.OrgUnit;
import uk.gov.hmcts.reform.lrdapi.domain.Service;
import uk.gov.hmcts.reform.lrdapi.domain.ServiceToCcdCaseTypeAssoc;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceToCcdCaseTypeAssocRepositry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LrdServiceImplTest {

    private final ServiceRepository serviceRepository = mock(ServiceRepository.class);

    private final ServiceToCcdCaseTypeAssocRepositry serToCcdCsTypeRep = mock(ServiceToCcdCaseTypeAssocRepositry.class);

    @InjectMocks
    LrdServiceImpl sut;

    List<LrdOrgInfoServiceResponse> lrdOrgInfoServiceResponses = new ArrayList<>();
    List<Service> services = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        OrgUnit orgUnit = new OrgUnit(1L, "orgUnit");
        Service service = new Service();
        service.setServiceId(1L);
        service.setOrgUnit(orgUnit);
        service.setServiceCode("AAA1");
        service.setServiceDescription("Insolvency");
        service.setServiceShortDescription("Insolvency");
        OrgSubBusinessArea orgSubBusArea = new OrgSubBusinessArea(1L, "OrgSubBusinessArea");
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
        services.add(service);
        when(serviceRepository.findByServiceCode(any())).thenReturn(service);
        when(serToCcdCsTypeRep.findByCcdCaseTypeIgnoreCase(any())).thenReturn(serviceToCcdCaseTypeAssoc);
        when(serToCcdCsTypeRep.findByCcdServiceNameInIgnoreCase(any())).thenReturn(List.of(serviceToCcdCaseTypeAssoc));
        when(serviceRepository.findAll()).thenReturn(services);

    }

    @Test
    void testRetrieveAnOrgServiceDetailsByServiceCode() {
        String serviceCode = "AAA1";
        lrdOrgInfoServiceResponses = sut.retrieveOrgServiceDetails(serviceCode, null, null);
        assertNotNull(lrdOrgInfoServiceResponses);
        assertEquals(1, lrdOrgInfoServiceResponses.size());
        verify(serviceRepository, times(1)).findByServiceCode(serviceCode);

    }

    @Test
    void testRetrieveAnOrgServiceDetailsByCcdCodeType() {
        String serviceCode = null;
        String ccdCaseType = "CCDCASETYPE1";
        lrdOrgInfoServiceResponses = sut.retrieveOrgServiceDetails(serviceCode, ccdCaseType, null);
        assertNotNull(lrdOrgInfoServiceResponses);
        assertEquals(1, lrdOrgInfoServiceResponses.size());
        verify(serToCcdCsTypeRep, times(1)).findByCcdCaseTypeIgnoreCase(ccdCaseType);
    }

    @Test
    void testShouldThrowExceptionForUnKnownServiceCode() {
        String serviceCode = "serviceCode";
        String ccdCaseType = null;
        when(serviceRepository.findByServiceCode(any())).thenReturn(null);
        assertThrows(EmptyResultDataAccessException.class, () -> {
            sut.retrieveOrgServiceDetails(serviceCode, ccdCaseType, null);
        });
        verify(serviceRepository, times(1)).findByServiceCode(any());
    }

    @Test
    void testShouldThrowExceptionForUnKnownCcdCodeType() {
        String serviceCode = null;
        String ccdCaseType = "ccdcodeType";
        when(serToCcdCsTypeRep.findByCcdCaseTypeIgnoreCase(any())).thenReturn(null);
        assertThrows(EmptyResultDataAccessException.class, () -> {
            sut.retrieveOrgServiceDetails(serviceCode, ccdCaseType, null);
        });
        verify(serToCcdCsTypeRep, times(1)).findByCcdCaseTypeIgnoreCase(any());
    }

    @Test
    void testRetrieveAnOrgServiceDetailsByDefaultWithoutAnyQueryFormsInTheRequest() {
        lrdOrgInfoServiceResponses = sut.retrieveOrgServiceDetails(null, null,
                                                                   null);
        assertNotNull(lrdOrgInfoServiceResponses);
        assertEquals(1, lrdOrgInfoServiceResponses.size());
        verify(serviceRepository, times(1)).findAll();

    }

    @Test
    void testShouldThrowExceptionForEmptyServiceDetails() {
        when(serviceRepository.findAll()).thenReturn(null);
        assertThrows(EmptyResultDataAccessException.class, () -> {
            sut.retrieveOrgServiceDetails(null, null, null);
        });
        assertNotNull(lrdOrgInfoServiceResponses);
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAnOrgServiceDetailsByCcdServiceName() {
        String serviceCode = null;
        String ccdCaseType = "";
        String ccdServiceName = "CCDSERVICENAME,  CMC ";
        lrdOrgInfoServiceResponses = sut.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertNotNull(lrdOrgInfoServiceResponses);
        assertEquals(1, lrdOrgInfoServiceResponses.size());
        verify(serToCcdCsTypeRep, times(1)).findByCcdServiceNameInIgnoreCase(anyList());
    }

    @Test
    void testRetrieveAnOrgServiceDetailsForCcdServiceNameAllScenario() {
        String serviceCode = null;
        String ccdCaseType = null;
        String ccdServiceName = "All";
        lrdOrgInfoServiceResponses = sut.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);

        assertNotNull(lrdOrgInfoServiceResponses);
        assertEquals(1, lrdOrgInfoServiceResponses.size());
        verify(serviceRepository, times(1)).findAll();

    }

    @Test
    void testRetrieveAnOrgServiceDetailsByCcdServiceNameShouldThrowException() {
        String serviceCode = null;
        String ccdCaseType = "";
        String ccdServiceName = "CCDSERVICENAME,  CMC ";
        when(serToCcdCsTypeRep.findByCcdServiceNameInIgnoreCase(any())).thenReturn(List.of());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            sut.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        });
        assertNotNull(lrdOrgInfoServiceResponses);
        verify(serToCcdCsTypeRep, times(1)).findByCcdServiceNameInIgnoreCase(anyList());
    }

}
