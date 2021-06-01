package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "building_location_status")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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
