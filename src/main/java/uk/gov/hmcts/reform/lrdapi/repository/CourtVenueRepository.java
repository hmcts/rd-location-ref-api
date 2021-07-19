package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;

import java.util.List;

public interface CourtVenueRepository extends JpaRepository<CourtVenue, Long> {

    List<CourtVenue> findByEpimmsIdIn(List<String> epimmsIdList);

    List<CourtVenue> findByCourtTypeId(String courtTypeId);

    List<CourtVenue> findByRegionId(String regionId);

    List<CourtVenue> findByClusterId(String clusterId);
}
