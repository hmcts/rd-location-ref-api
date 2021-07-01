package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {

    Region findByDescriptionIgnoreCase(String description);

}
