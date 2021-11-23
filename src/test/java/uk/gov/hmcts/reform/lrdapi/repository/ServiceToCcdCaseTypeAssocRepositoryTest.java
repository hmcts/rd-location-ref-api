package uk.gov.hmcts.reform.lrdapi.repository;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.domain.ServiceToCcdCaseTypeAssoc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceToCcdCaseTypeAssocRepositoryTest {

    ServiceToCcdCaseTypeAssocRepositry serviceToCcdCaseTypeAssocRepositry
        = mock(ServiceToCcdCaseTypeAssocRepositry.class);

    @Test
    void findByServiceCodeTest() {
        when(serviceToCcdCaseTypeAssocRepositry.findByCcdCaseTypeIgnoreCase(anyString()))
            .thenReturn(new ServiceToCcdCaseTypeAssoc());
        assertNotNull(serviceToCcdCaseTypeAssocRepositry.findByCcdCaseTypeIgnoreCase(anyString()));
    }
}
