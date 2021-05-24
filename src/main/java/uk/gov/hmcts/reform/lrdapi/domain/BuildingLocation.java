package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity(name = "building_location")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class BuildingLocation {

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
    @Column(name = "created")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "building_location_status_id")
    @Size(max = 16)
    private String buildingLocationStatusId;

    @Column(name = "area")
    @Size(max = 16)
    private String area;

    @Column(name = "region_id")
    @Size(max = 16)
    private String regionId;

    @Column(name = "cluster_id")
    @Size(max = 16)
    private String clusterId;

    @Column(name = "court_finder_url")
    @Size(max = 512)
    private String courtFinderUrl;

    @Column(name = "postcode", nullable = false)
    @Size(max = 8)
    private String postcode;

    @Column(name = "address", nullable = false)
    @Size(max = 512)
    private String address;



}
