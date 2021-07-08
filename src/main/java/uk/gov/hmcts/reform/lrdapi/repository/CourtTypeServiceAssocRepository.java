package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.lrdapi.domain.CourtTypeServiceAssoc;

@Repository
public interface CourtTypeServiceAssocRepository extends JpaRepository<CourtTypeServiceAssoc, String> {

    @Query(value = "SELECT * FROM court_type_service_assoc ctsa WHERE ctsa.service_code = :serviceCode",
        nativeQuery = true)
    CourtTypeServiceAssoc findByServiceCode(@Param("serviceCode") String serviceCode);

}
