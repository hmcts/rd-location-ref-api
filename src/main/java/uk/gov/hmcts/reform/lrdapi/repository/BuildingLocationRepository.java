package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;

import java.util.List;

@Repository
public interface BuildingLocationRepository extends JpaRepository<BuildingLocation, String> {

    List<BuildingLocation> findByEpimmsIdIn(List<String> epimmsId);

    BuildingLocation findByBuildingLocationNameIgnoreCase(String buildingLocationName);

}
