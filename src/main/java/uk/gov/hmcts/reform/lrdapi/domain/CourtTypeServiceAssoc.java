package uk.gov.hmcts.reform.lrdapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_code",referencedColumnName = "service_code", insertable = false,
        updatable = false, nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_type_id")
    private CourtType courtType;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime lastUpdated;

}
