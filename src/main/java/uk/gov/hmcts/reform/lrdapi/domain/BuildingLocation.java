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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity(name = "building_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingLocation implements Serializable {

    @Id
    @Column(name = "building_location_id")
    @Size(max = 16)
    private String buildingLocationId;

    @Column(name = "epimms_id")
    @Size(max = 16)
    @NotNull
    private String epimmsId;

    @Column(name = "building_location_name")
    @Size(max = 256)
    @NotNull
    private String buildingLocationName;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @ManyToOne
    @JoinColumn(name = "building_location_status_id")
    private BuildingLocationStatus buildingLocationStatus;

    @Column(name = "area")
    @Size(max = 16)
    private String area;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

    @Column(name = "court_finder_url")
    @Size(max = 512)
    private String courtFinderUrl;

    @Column(name = "postcode")
    @Size(max = 8)
    @NotNull
    private String postcode;

    @Column(name = "address")
    @Size(max = 512)
    @NotNull
    private String address;

    @OneToMany(targetEntity = CourtLocation.class, mappedBy = "buildingLocation")
    @Fetch(FetchMode.SUBSELECT)
    private List<CourtLocation> courtLocations;

}
