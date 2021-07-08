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

import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Entity(name = "court_venue")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Setter
@Builder
@EqualsAndHashCode
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"epimms_id","site_name","court_type_id"}))
public class CourtVenue {

    @Id
    @Size(max = 16)
    private Long courtVenueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epimms_id", referencedColumnName = "epimms_id",
        insertable = false, updatable = false, nullable = false)
    private BuildingLocation buildingLocation;

    @Column(name = "site_name", nullable = false)
    @Size(max = 256)
    private String siteName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @Size(max = 16)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_type_id")
    @Size(max = 16)
    private CourtType courtType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id")
    @Size(max = 16)
    private Cluster cluster;

    @Column(name = "open_for_public", nullable = false)
    private Boolean openForPublic;

    @Column(name = "court_address", nullable = false)
    @Size(max = 512)
    private String courtAddress;

    @Column(name = "postcode", nullable = false)
    @Size(max = 8)
    private String postcode;

    @Column(name = "phone_number")
    @Size(max = 128)
    private String phoneNumber;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @Column(name = "court_location_code")
    @Size(max = 8)
    private String courtLocationCode;

    @Column(name = "dx_address")
    @Size(max = 64)
    private String dxAddress;

    @Column(name = "welsh_site_name")
    @Size(max = 256)
    private String welshSiteName;

    @Column(name = "welsh_court_address")
    @Size(max = 512)
    private String welshCourtAddress;

    @Column(name = "court_status")
    @Size(max = 32)
    private String courtStatus;

    @Column(name = "court_open_date")
    private LocalDateTime courtOpenDate;

    @Column(name = "court_name")
    @Size(max = 256)
    private String courtName;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime lastUpdated;

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
