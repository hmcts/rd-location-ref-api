package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
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

import static org.hibernate.annotations.FetchMode.SUBSELECT;

@Entity(name = "region")
@NoArgsConstructor
@Getter
@Setter
public class Region implements Serializable {

    @Id
    @Column(name = "region_id")
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

    @Column(name = "api_enabled")
    private boolean apiEnabled;

    @OneToMany(targetEntity = BuildingLocation.class, mappedBy = "region")
    @Fetch(SUBSELECT)
    private Set<BuildingLocation> buildingLocationSet;

    @OneToMany(targetEntity = CourtVenue.class, mappedBy = "region")
    @Fetch(SUBSELECT)
    private Set<CourtVenue> courtVenueSet;

    public Region(String regionId, String description, String welshDescription) {
        this.regionId = regionId;
        this.description = description;
        this.welshDescription = welshDescription;
    }

}
