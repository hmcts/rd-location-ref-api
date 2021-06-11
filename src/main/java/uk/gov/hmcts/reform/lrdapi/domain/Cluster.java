package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity(name = "cluster")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cluster implements Serializable {

    @Id
    @Column(name = "cluster_id")
    @Size(max = 16)
    private String clusterId;

    @Column(name = "cluster_name")
    @Size(max = 256)
    private String clusterName;

    @Column(name = "welsh_cluster_name")
    @Size(max = 256)
    private String welshClusterName;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @OneToMany(targetEntity = BuildingLocation.class, mappedBy = "cluster")
    @Fetch(FetchMode.SUBSELECT)
    private List<BuildingLocation> buildingLocations;

    @OneToMany(targetEntity = CourtLocation.class, mappedBy = "cluster")
    @Fetch(FetchMode.SUBSELECT)
    private List<CourtLocation> courtLocations;
}
