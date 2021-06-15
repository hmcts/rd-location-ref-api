package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "court_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtLocation implements Serializable {

    @Id
    @Column(name = "court_location_id")
    @Size(max = 16)
    private String courtLocationId;

    @ManyToOne
    @JoinColumn(name = "epimms_id", referencedColumnName = "epimms_id",
        nullable = false)
    private BuildingLocation buildingLocation;

    @Column(name = "court_location_name")
    @Size(max = 256)
    @NotNull
    private String courtLocationName;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "court_location_category_id")
    private CourtLocationCategory courtLocationCategory;

    @ManyToOne
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

    @Column(name = "open_for_public")
    @NotNull
    private Boolean openForPublic;

    @Column(name = "court_address")
    @Size(max = 512)
    @NotNull
    private String courtAddress;

    @Column(name = "postcode")
    @Size(max = 8)
    @NotNull
    private String postcode;

    @Column(name = "phone_number")
    @Size(max = 128)
    private String phoneNumber;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @Column(name = "court_location_code", unique = true)
    @Size(max = 8)
    private String courtLocationCode;

    @Column(name = "dx_address")
    @Size(max = 64)
    private String dxAddress;

    @Column(name = "welsh_court_location_name")
    @Size(max = 256)
    private String welshCourtLocationName;

    @Column(name = "welsh_court_address")
    @Size(max = 512)
    private String welshCourtAddress;
}
