package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.reform.lib.audit.domain.CommonExceptionEntity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "location_refdata_exception")
@SequenceGenerator(name = "exception_id_seq", sequenceName = "exception_id_seq", allocationSize = 1)
@SuperBuilder
@Getter
@NoArgsConstructor
public class LrdException extends CommonExceptionEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "job_id",
        insertable = false, updatable = false, nullable = false)
    public LrdAudit lrdAudit;
}

