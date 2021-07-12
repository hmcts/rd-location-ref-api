package uk.gov.hmcts.reform.lrdapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;

public interface ClusterRepository extends JpaRepository<Cluster, String> {
}
