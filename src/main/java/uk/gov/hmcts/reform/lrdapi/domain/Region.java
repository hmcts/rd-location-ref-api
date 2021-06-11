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

@Entity(name = "region")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region implements Serializable {

    @Id
    @Column(name = "region_id")
    @Size(max = 16)
    private String regionId;

    @Column(name = "description")
    @Size(max = 256)
    private String description;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "welsh_description")
    @Size(max = 256)
    private String welshDescription;

    @OneToMany(targetEntity = BuildingLocation.class, mappedBy = "region")
    @Fetch(FetchMode.SUBSELECT)
    private List<BuildingLocation> buildingLocations;

    @OneToMany(targetEntity = CourtLocation.class, mappedBy = "region")
    @Fetch(FetchMode.SUBSELECT)
    private List<CourtLocation> courtLocations;
}
