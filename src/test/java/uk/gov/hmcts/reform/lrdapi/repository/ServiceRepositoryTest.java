package uk.gov.hmcts.reform.lrdapi.repository;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.domain.Service;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceRepositoryTest {

    public ServiceRepository serviceRepository = mock(ServiceRepository.class);

    @Test
    void findByServiceCodeTest() {
        when(serviceRepository.findByServiceCode(anyString())).thenReturn(new Service());
        assertNotNull(serviceRepository.findByServiceCode(anyString()));
    }

    @Test
    void findAllTest() {
        when(serviceRepository.findAll()).thenReturn(new ArrayList<>());
        assertNotNull(serviceRepository.findAll());
    }

}
