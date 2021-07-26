package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;

import java.util.List;

public interface CourtVenueRepository extends JpaRepository<CourtVenue, Long> {

    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.epimmsId in (:epimmsIdList)")
    List<CourtVenue> findByEpimmsIdIn(List<String> epimmsIdList);

    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.courtTypeId = :courtTypeId and cv.courtStatus='Open'")
    List<CourtVenue> findByCourtTypeIdWithOpenCourtStatus(String courtTypeId);

    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.regionId = :regionId and cv.courtStatus='Open'")
    List<CourtVenue> findByRegionIdWithOpenCourtStatus(String regionId);


    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.clusterId = :clusterId and cv.courtStatus='Open'")
    List<CourtVenue> findByClusterIdWithOpenCourtStatus(String clusterId);

    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.courtStatus='Open'")
    List<CourtVenue> findAllWithOpenCourtStatus();
}
