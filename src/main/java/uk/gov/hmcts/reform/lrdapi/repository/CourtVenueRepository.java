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


    @Query(value = """
             select cv from court_venue cv LEFT JOIN FETCH cv.courtType
             LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region
             where cv.epimmsId in (:epimmsIdList) and cv.courtTypeId = :courtTypeId and cv.courtStatus='Open'
             """)
    List<CourtVenue> findByCourtTypeIdAndEpimmsIdWithOpenCourtStatus(List<String> epimmsIdList, String courtTypeId);


    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.regionId = :regionId and cv.courtStatus='Open'")
    List<CourtVenue> findByRegionIdWithOpenCourtStatus(String regionId);


    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.clusterId = :clusterId and cv.courtStatus='Open'")
    List<CourtVenue> findByClusterIdWithOpenCourtStatus(String clusterId);

    @Query(value = "select cv from court_venue cv "
        + "where upper(cv.courtName) = upper(:courtVenueName) "
        + "or upper(cv.siteName) = upper(:courtVenueName)")
    List<CourtVenue> findByCourtVenueNameOrSiteName(String courtVenueName);

    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.courtStatus='Open'")
    List<CourtVenue> findAllWithOpenCourtStatus();

    @Query(value = "select cv from court_venue cv LEFT JOIN FETCH cv.courtType"
        + " LEFT JOIN FETCH cv.cluster LEFT JOIN FETCH cv.region "
        + "where cv.courtStatus='Open' "
        + "and (coalesce(:courtTypeId) is null or (cv.courtTypeId in (:courtTypeId))) "
        + "and ((:isCaseManagementLocation) is null or (cv.isCaseManagementLocation in (:isCaseManagementLocation))) "
        + "and ((:isHearingLocation) is null or (cv.isHearingLocation in (:isHearingLocation))) "
        + "and ((:locationType) is null or (cv.locationType in (:locationType))) "
        + "and ((:isTemporaryLocation) is null or (cv.isTemporaryLocation in (:isTemporaryLocation))) "
        + "and (upper(cv.siteName) like %:searchString% "
        + "or upper(cv.courtName) like %:searchString% "
        + "or upper(cv.postcode) like %:searchString% "
        + "or upper(cv.courtAddress) like %:searchString%)")
    List<CourtVenue> findBySearchStringAndCourtTypeId(String searchString, List<String> courtTypeId,
                                                      String isCaseManagementLocation, String isHearingLocation,
                                                      String locationType, String isTemporaryLocation);
}
