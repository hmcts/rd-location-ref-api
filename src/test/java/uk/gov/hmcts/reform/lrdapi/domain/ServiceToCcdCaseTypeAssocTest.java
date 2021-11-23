package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceToCcdCaseTypeAssocTest {

    public static final String CCDCASETYPE1 = "CCDCASETYPE1";
    public static final String CCDSERVICENAME = "CCDSERVICENAME";
    public static final String AAA1 = "AAA1";


    public Service createService(String serviceCode) {
        OrgUnit orgUnit = new OrgUnit(1L,"orgUnit");
        Service service = new Service();
        service.setServiceId(1L);
        service.setOrgUnit(orgUnit);
        service.setServiceCode(serviceCode);
        service.setServiceDescription("Insolvency");
        service.setServiceShortDescription("Insolvency");
        service.setLastUpdate(LocalDateTime.now());
        return service;
    }

    public ServiceToCcdCaseTypeAssoc createAssoc(String serviceName) {
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = new ServiceToCcdCaseTypeAssoc();
        serviceToCcdCaseTypeAssoc.setId(1L);
        serviceToCcdCaseTypeAssoc.setCcdCaseType(CCDCASETYPE1);
        serviceToCcdCaseTypeAssoc.setCcdServiceName(serviceName);
        return serviceToCcdCaseTypeAssoc;
    }

    @Test
    void testServiceToCcdCaseTypeAssoc() {

        Service service = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = createAssoc(CCDSERVICENAME);

        serviceToCcdCaseTypeAssoc.setService(service);
        List<Service> services = new ArrayList<>();
        services.add(service);
        assertEquals(1L, serviceToCcdCaseTypeAssoc.getId());
        assertEquals(CCDCASETYPE1, serviceToCcdCaseTypeAssoc.getCcdCaseType());
        assertEquals(CCDSERVICENAME, serviceToCcdCaseTypeAssoc.getCcdServiceName());
        assertNotNull(serviceToCcdCaseTypeAssoc.getService());
        assertNotNull(service.getLastUpdate());
        assertNotNull(service.getServiceToCcdCaseTypeAssocs());
        assertEquals(1, services.size());
    }

    @Test
    void testEqualsWithEqualObjects() {
        Service service1 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc1 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc1.setService(service1);
        Service service2 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc2 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc2.setService(service2);
        boolean result = serviceToCcdCaseTypeAssoc1.equals(serviceToCcdCaseTypeAssoc2);
        assertTrue(result);
    }

    @Test
    void testEqualsWithSameInstance() {
        Service service1 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc1 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc1.setService(service1);
        boolean result = serviceToCcdCaseTypeAssoc1.equals(serviceToCcdCaseTypeAssoc1);
        assertTrue(result);
    }

    @Test
    void testEqualsWithNonEqualObjects() {
        Service service1 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc1 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc1.setService(service1);
        Service service2 = createService("AAA2");
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc2 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc2.setService(service2);
        boolean result = serviceToCcdCaseTypeAssoc1.equals(serviceToCcdCaseTypeAssoc2);
        assertFalse(result);
    }

    @Test
    void testEqualsWithNonEqualServiceName() {
        Service service1 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc1 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc1.setService(service1);
        Service service2 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc2 = createAssoc("CCDSERVICENAME2");
        serviceToCcdCaseTypeAssoc2.setService(service2);
        boolean result = serviceToCcdCaseTypeAssoc1.equals(serviceToCcdCaseTypeAssoc2);
        assertFalse(result);
    }

    @Test
    void testEqualsWithNullObjects() {
        Service service1 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc1 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc1.setService(service1);
        boolean result = serviceToCcdCaseTypeAssoc1.equals(null);
        assertFalse(result);
    }

    @Test
    void testEqualsWithDifferentObjectType() {
        Service service = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc.setService(service);
        boolean result = serviceToCcdCaseTypeAssoc.equals(new LrdOrgInfoServiceResponse());
        assertFalse(result);
    }

    @Test
    void testHashcodeWithServiceNameNonNull() {
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = createAssoc(CCDSERVICENAME);
        assertEquals(CCDSERVICENAME.length(), serviceToCcdCaseTypeAssoc.hashCode());
    }

    @Test
    void testHashcodeWithServiceNameNull() {
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = createAssoc(null);
        assertEquals(1, serviceToCcdCaseTypeAssoc.hashCode());
    }
}
