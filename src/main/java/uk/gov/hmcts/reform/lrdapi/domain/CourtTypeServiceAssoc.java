package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "court_type_service_assoc")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class CourtTypeServiceAssoc implements Serializable {

    @Id
    @Size(max = 16)
    private String courtTypeServiceAssocId;

    @Column(name = "service_code")
    @NotNull
    private String serviceCode;

    @ManyToOne
    @JoinColumn(name = "service_code",referencedColumnName = "service_code", insertable = false,
        updatable = false, nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "court_type_id")
    private CourtType courtType;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime lastUpdated;

}
