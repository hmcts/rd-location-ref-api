package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.lrdapi.domain.LrdAudit;

@Repository
public interface LrdAuditRepository extends JpaRepository<LrdAudit, String> {

    @Query(value = "select count(*) from location_refdata_audit where job_start_time\\:\\:date "
        + " >= current_date - 1  and authenticated_user_id = :authenticatedUserId  and status = :status "
        + "and file_name = :fileName",  nativeQuery = true)
    long findByAuthenticatedUserIdAndStatus(String authenticatedUserId, String status, String fileName);
}
