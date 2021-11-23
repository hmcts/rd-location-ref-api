package uk.gov.hmcts.reform.lrdapi.repository;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.lrdapi.domain.CourtTypeServiceAssoc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourtTypeServiceAssocRepositoryTest {

    public CourtTypeServiceAssocRepository courtTypeServiceAssocRepository =
        mock(CourtTypeServiceAssocRepository.class);

    @Test
    void findCourtVenuesByServiceCodeTest() {
        when(courtTypeServiceAssocRepository.findByServiceCode(anyString())).thenReturn(new CourtTypeServiceAssoc());
        assertNotNull(courtTypeServiceAssocRepository.findByServiceCode(anyString()));
    }
}
