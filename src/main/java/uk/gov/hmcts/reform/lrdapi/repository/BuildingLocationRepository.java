package uk.gov.hmcts.reform.lrdapi.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;

import java.util.List;

@Repository
public interface BuildingLocationRepository extends JpaRepository<BuildingLocation, Long> {

    @Query(value = "select loc from building_location loc "
        + "JOIN FETCH court_venue cv "
        + "on loc.epimmsId = cv.epimmsId "
        + "where loc.epimmsId in (:epimmsIdList)")
    List<BuildingLocation> findByEpimmsId(@Param("epimmsIdList") List<String> epimmsIdList);

    @Query(value = "select loc from building_location loc "
        + "JOIN FETCH court_venue cv "
        + "on loc.epimmsId = cv.epimmsId "
        + "where upper(loc.buildingLocationName) = upper(:buildingLocationName)")
    BuildingLocation findByBuildingLocationNameIgnoreCase(@Param("buildingLocationName") String buildingLocationName);

    @Query(value = "select loc from building_location loc where upper(loc.buildingLocationStatus) = 'OPEN'")
    List<BuildingLocation> findByBuildingLocationStatusOpen();
}
