package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity(name = "building_location")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Setter
@Builder
@EqualsAndHashCode
public class BuildingLocation implements Serializable {

    @Id
    @Size(max = 16)
    private String buildingLocationId;

    @Column(name = "epimms_id", nullable = false)
    @Size(max = 16)
    private String epimmsId;

    @Column(name = "building_location_name", nullable = false)
    @Size(max = 256)
    private String buildingLocationName;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime lastUpdated;

    @Size(max = 32)
    @Column(name = "building_location_status")
    private String buildingLocationStatus;

    @Column(name = "area")
    @Size(max = 16)
    private String area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @Size(max = 16)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id")
    @Size(max = 16)
    private Cluster cluster;

    @Column(name = "court_finder_url")
    @Size(max = 512)
    private String courtFinderUrl;

    @Column(name = "postcode", nullable = false)
    @Size(max = 8)
    private String postcode;

    @Column(name = "address", nullable = false)
    @Size(max = 512)
    private String address;

    public Optional<Cluster> getCluster() {
        return Optional.ofNullable(cluster);
    }

    public Optional<Region> getRegion() {
        return Optional.ofNullable(region);
    }
}
