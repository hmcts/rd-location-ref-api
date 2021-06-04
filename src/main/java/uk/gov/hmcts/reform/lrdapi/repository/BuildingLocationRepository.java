package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;

@Repository
public interface BuildingLocationRepository extends JpaRepository<BuildingLocation, String> {

    BuildingLocation findByEpimmsId(String epimmsId);

}
