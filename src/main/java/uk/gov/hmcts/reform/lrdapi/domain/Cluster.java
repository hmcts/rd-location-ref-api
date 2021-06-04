package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity(name = "cluster")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Cluster implements Serializable {

    @Id
    @Size(max = 16)
    private String clusterId;

    @Column(name = "cluster_name", nullable = false)
    @Size(max = 256)
    private String clusterName;

    @Column(name = "welsh_cluster_name", nullable = false)
    @Size(max = 256)
    private String welshClusterName;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @OneToMany(targetEntity = BuildingLocation.class, mappedBy = "cluster")
    private Set<BuildingLocation> buildingLocations = new HashSet<>();

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    public Cluster(String clusterId, String clusterName, String welshClusterName) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.welshClusterName = welshClusterName;
    }

}
