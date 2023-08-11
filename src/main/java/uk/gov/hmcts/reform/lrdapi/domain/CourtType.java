package uk.gov.hmcts.reform.lrdapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "court_type")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Setter
@Builder
@EqualsAndHashCode
public class CourtType implements Serializable {

    @Id
    @Column(name = "court_type_id")
    @Size(max = 16)
    private String courtTypeId;

    @Column(name = "court_type", nullable = false)
    @Size(max = 128)
    private String typeOfCourt;

    @Column(name = "welsh_court_type", nullable = false)
    @Size(max = 128)
    private String welshCourtType;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime lastUpdated;

    @OneToMany(targetEntity = CourtVenue.class, mappedBy = "courtType")
    @Fetch(FetchMode.SUBSELECT)
    private List<CourtVenue> courtVenues;

    @OneToMany(targetEntity = CourtTypeServiceAssoc.class, mappedBy = "courtType")
    @Fetch(FetchMode.SUBSELECT)
    private List<CourtTypeServiceAssoc> courtTypeServiceAssocs;

}
