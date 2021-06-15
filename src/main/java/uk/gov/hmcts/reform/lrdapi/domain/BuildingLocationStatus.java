package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "building_location_status")
@NoArgsConstructor
@Getter
@Setter
public class BuildingLocationStatus implements Serializable {

    @Id
    @Column(name = "building_location_status_id")
    private String buildingStatusId;

    private String status;

    @OneToMany(mappedBy = "buildingLocationStatus")
    private Set<BuildingLocation> buildingLocations;

    public BuildingLocationStatus(String buildingStatusId, String status) {
        this.buildingStatusId = buildingStatusId;
        this.status = status;
    }
}
