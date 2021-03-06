package uk.gov.hmcts.reform.lrdapi.domain;

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
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "court_venue")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@Builder
@EqualsAndHashCode
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"epimms_id","site_name","court_type_id"}))
public class CourtVenue implements Serializable {

    @Id
    private Long courtVenueId;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "epimms_id")
    private String epimmsId;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "court_type_id")
    private String courtTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

    private Boolean openForPublic;

    private String courtAddress;

    private String postcode;

    private String phoneNumber;

    private LocalDateTime closedDate;

    private String courtLocationCode;

    private String dxAddress;

    private String welshSiteName;

    private String welshCourtAddress;

    private String courtStatus;

    private String courtName;

    private LocalDateTime courtOpenDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epimms_id", referencedColumnName = "epimms_id",
        insertable = false, updatable = false, nullable = false)
    private BuildingLocation buildingLocation;

    public Optional<LocalDateTime> getClosedDate() {
        return Optional.ofNullable(closedDate);
    }

    public Optional<LocalDateTime> getCourtOpenDate() {
        return Optional.ofNullable(courtOpenDate);
    }

    public Optional<Cluster> getCluster() {
        return Optional.ofNullable(cluster);
    }

    public Optional<Region> getRegion() {
        return Optional.ofNullable(region);
    }

}
