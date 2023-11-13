package uk.gov.hmcts.reform.lrdapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity(name = "building_location")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@Builder
@EqualsAndHashCode(exclude = "courtVenues")
public class BuildingLocation implements Serializable {

    @Id
    private Long buildingLocationId;

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

    @OneToMany(targetEntity = CourtVenue.class)
    @JoinColumn(name = "epimms_id", referencedColumnName = "epimms_id")
    @Builder.Default
    private Set<CourtVenue> courtVenues = new HashSet<>();

    public Optional<Cluster> getCluster() {
        return Optional.ofNullable(cluster);
    }

    public Optional<Region> getRegion() {
        return Optional.ofNullable(region);
    }

}
