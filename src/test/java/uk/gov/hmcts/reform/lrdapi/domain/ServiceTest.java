package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ServiceTest {

    @Test
    void testService() {

        OrgUnit orgUnit = new OrgUnit(1L,"orgUnit");
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
        List<ServiceToCcdCaseTypeAssoc> serviceToCcdCaseTypeAssocs = new ArrayList<>();
        serviceToCcdCaseTypeAssocs.add(serviceToCcdCaseTypeAssoc);
        service.setServiceToCcdCaseTypeAssocs(serviceToCcdCaseTypeAssocs);

        assertNotNull(service);
        assertEquals("AAA1", service.getServiceCode());
        assertNotNull(service.getJurisdiction());
        assertEquals(1L, service.getJurisdiction().getJurisdictionId());
        assertEquals("Insolvency", service.getServiceDescription());
        assertEquals("Insolvency", service.getServiceShortDescription());
        assertNotNull(service.getLastUpdate());
        assertNotNull(service.getServiceToCcdCaseTypeAssocs());
        assertEquals(1, service.getServiceToCcdCaseTypeAssocs().size());
    }
}
