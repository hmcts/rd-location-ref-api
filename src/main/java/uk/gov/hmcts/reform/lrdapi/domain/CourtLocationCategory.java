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

@Entity(name = "court_location_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtLocationCategory implements Serializable {

    @Id
    @Column(name = "court_location_category_id")
    @Size(max = 16)
    private String courtLocationCategoryId;

    @Column(name = "court_location_category")
    @Size(max = 128)
    private String courtLocationCategory;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @OneToMany(targetEntity = CourtLocation.class, mappedBy = "courtLocationCategory")
    @Fetch(FetchMode.SUBSELECT)
    private List<CourtLocation> courtLocations;
}
