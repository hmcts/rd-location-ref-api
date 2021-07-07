package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {

    Region findByDescriptionIgnoreCase(String description);

    List<Region> findByDescriptionInIgnoreCase(List<String> regionId);

    List<Region> findByRegionIdIn(List<String> regionId);

    List<Region> findByDescriptionNotIgnoreCase(String description);

}
