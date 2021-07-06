package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;

public interface CourtVenueRepository extends JpaRepository<CourtVenue, String> {
}
