package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServiceToCcdCaseTypeAssocTest {

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
    public void testServiceToCcdCaseTypeAssoc() {

        Service service = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = createAssoc(CCDSERVICENAME);

        serviceToCcdCaseTypeAssoc.setService(service);
        List<Service> services = new ArrayList<>();
        services.add(service);
        assertThat(serviceToCcdCaseTypeAssoc.getId()).isEqualTo(1L);
        assertThat(serviceToCcdCaseTypeAssoc.getCcdCaseType()).isEqualTo(CCDCASETYPE1);
        assertThat(serviceToCcdCaseTypeAssoc.getCcdServiceName()).isEqualTo(CCDSERVICENAME);
        assertThat(serviceToCcdCaseTypeAssoc.getService()).isNotNull();
        assertThat(service.getLastUpdate()).isNotNull();
        assertThat(service.getServiceToCcdCaseTypeAssocs()).isNotNull();
        assertThat(services.size()).isEqualTo(1);
    }

    @Test
    public void testEqualsWithEqualObjects() {
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
    public void testEqualsWithSameInstance() {
        Service service1 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc1 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc1.setService(service1);
        boolean result = serviceToCcdCaseTypeAssoc1.equals(serviceToCcdCaseTypeAssoc1);
        assertTrue(result);
    }

    @Test
    public void testEqualsWithNonEqualObjects() {
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
    public void testEqualsWithNonEqualServiceName() {
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
    public void testEqualsWithNullObjects() {
        Service service1 = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc1 = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc1.setService(service1);
        boolean result = serviceToCcdCaseTypeAssoc1.equals(null);
        assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentObjectType() {
        Service service = createService(AAA1);
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = createAssoc(CCDSERVICENAME);
        serviceToCcdCaseTypeAssoc.setService(service);
        boolean result = serviceToCcdCaseTypeAssoc.equals(new LrdOrgInfoServiceResponse());
        assertFalse(result);
    }

    @Test
    public void testHashcodeWithServiceNameNonNull() {
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = createAssoc(CCDSERVICENAME);
        assertThat(serviceToCcdCaseTypeAssoc.hashCode()).isEqualTo(CCDSERVICENAME.length());
    }

    @Test
    public void testHashcodeWithServiceNameNull() {
        ServiceToCcdCaseTypeAssoc serviceToCcdCaseTypeAssoc = createAssoc(null);
        assertThat(serviceToCcdCaseTypeAssoc.hashCode()).isEqualTo(1);
    }
}
