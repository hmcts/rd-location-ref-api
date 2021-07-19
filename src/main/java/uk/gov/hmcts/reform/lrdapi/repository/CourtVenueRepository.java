package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;

import java.util.List;

public interface CourtVenueRepository extends JpaRepository<CourtVenue, Long> {

//    @Query(value = "select distinct loc from building_location loc "
//        + "LEFT JOIN FETCH court_venue cv "
//        + "on loc.epimmsId = cv.epimmsId "
//        + "where loc.epimmsId in (:epimmsIdList)")
    List<CourtVenue> findByEpimmsIdIn(List<String> epimmsIdList);

    List<CourtVenue> findByCourtTypeId(String courtTypeId);

    List<CourtVenue> findByRegionId(String regionId);

    List<CourtVenue> findByClusterId(String clusterId);
}
