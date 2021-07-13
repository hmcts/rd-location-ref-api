package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.lrdapi.domain.CourtTypeServiceAssoc;

@Repository
public interface CourtTypeServiceAssocRepository extends JpaRepository<CourtTypeServiceAssoc, String> {

    CourtTypeServiceAssoc findByServiceCode(String serviceCode);

}
