package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity(name = "region")
@NoArgsConstructor
@Getter
@Setter
public class Region implements Serializable {

    @Id
    @Size(max = 16)
    private String regionId;

    @Column(name = "description")
    @Size(max = 256)
    private String description;

    @Column(name = "welsh_description")
    @Size(max = 256)
    private String welshDescription;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @OneToMany(targetEntity = BuildingLocation.class, mappedBy = "region")
    private Set<BuildingLocation> buildingLocationSet;

    @OneToMany(targetEntity = CourtVenue.class, mappedBy = "region")
    private Set<CourtVenue> courtVenuesSet;

    public Region(String regionId, String description, String welshDescription) {
        this.regionId = regionId;
        this.description = description;
        this.welshDescription = welshDescription;
    }

}
