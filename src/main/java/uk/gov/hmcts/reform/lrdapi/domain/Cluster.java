package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity(name = "cluster")
@NoArgsConstructor
@Getter
@Setter
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

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @OneToMany(targetEntity = BuildingLocation.class, mappedBy = "cluster")
    @Fetch(FetchMode.SUBSELECT)
    private Set<BuildingLocation> buildingLocations = new HashSet<>();

    @OneToMany(targetEntity = CourtVenue.class, mappedBy = "cluster")
    @Fetch(FetchMode.SUBSELECT)
    private Set<CourtVenue> courtVenues = new HashSet<>();

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    public Cluster(String clusterId, String clusterName, String welshClusterName) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.welshClusterName = welshClusterName;
    }

}
