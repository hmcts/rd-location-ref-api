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
import javax.validation.constraints.Size;

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
    @Column(name = "court_venue_id")
    private Long courtVenueId;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "epimms_id")
    private String epimmsId;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column(name = "region_id")
    private String regionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "region_id",
        insertable = false, updatable = false, nullable = false)
    private Region region;

    @Column(name = "court_type_id")
    private String courtTypeId;

    @Column(name = "cluster_id")
    private String clusterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id",
        insertable = false, updatable = false, nullable = false)
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

    private String venueName;

    @Column(name = "is_case_management_location")
    private String isCaseManagementLocation;

    @Column(name = "is_hearing_location")
    private String isHearingLocation;

    @Column(name = "welsh_venue_name")
    private String welshVenueName;

    @Column(name = "is_temporary_location")
    private String isTemporaryLocation;

    @Column(name = "is_nightingale_court")
    private String isNightingaleCourt;

    @Column(name = "location_type")
    private String locationType;

    @Column(name = "parent_location")
    private String parentLocation;

    @Column(name = "welsh_court_name")
    private String welshCourtName;

    @Column(name = "uprn")
    private String uprn;

    @Column(name = "venue_ou_code")
    private String venueOuCode;

    @Column(name = "mrd_building_location_id")
    private String mrdBuildingLocationId;

    @Column(name = "mrd_venue_id")
    private String mrdVenueId;

    @Column(name = "service_url")
    private String serviceUrl;

    @Column(name = "fact_url")
    private String factUrl;

    @Column(name = "mrd_created_time")
    private String mrdCreatedTime;

    @Column(name = "mrd_updated_time")
    private String mrdUpdatedTime;

    @Column(name = "mrd_deleted_time")
    private String mrdDeletedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_type_id", referencedColumnName = "court_type_id",
        insertable = false, updatable = false, nullable = false)
    @Size(max = 16)
    private CourtType courtType;

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
