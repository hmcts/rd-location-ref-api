package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.reform.lib.audit.domain.CommonAuditEntity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "location_refdata_audit")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "job_id_seq", sequenceName = "job_id_seq", allocationSize = 1)
public class LrdAudit  extends CommonAuditEntity implements Serializable {

    @OneToMany(targetEntity = LrdException.class, mappedBy = "lrdAudit")
    public List<LrdException> exceptions;
}
