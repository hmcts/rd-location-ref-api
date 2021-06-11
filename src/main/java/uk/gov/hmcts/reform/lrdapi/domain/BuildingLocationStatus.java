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

@Entity(name = "building_location_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingLocationStatus implements Serializable {

    @Id
    @Column(name = "building_location_status_id")
    @Size(max = 16)
    private String buildingLocationStatusId;

    @Column(name = "status")
    @Size(max = 32)
    private String status;

    @Column(name = "welsh_status")
    @Size(max = 32)
    private String welshStatus;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @OneToMany(targetEntity = BuildingLocation.class, mappedBy = "buildingLocationStatus")
    @Fetch(FetchMode.SUBSELECT)
    private List<BuildingLocation> buildingLocations;
}
