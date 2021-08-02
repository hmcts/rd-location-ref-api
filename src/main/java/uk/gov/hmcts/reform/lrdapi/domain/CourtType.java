package uk.gov.hmcts.reform.lrdapi.domain;

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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

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

    @SuppressWarnings("java:S1700")
    @Column(name = "court_type", nullable = false)
    @Size(max = 128)
    private String courtType;

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
