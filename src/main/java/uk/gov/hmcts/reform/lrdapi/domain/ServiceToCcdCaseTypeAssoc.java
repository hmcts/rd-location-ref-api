package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import static java.util.Objects.nonNull;


@Entity(name = "service_to_ccd_case_type_assoc")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "service_to_ccd_case_type_assoc_seq", sequenceName = "service_to_ccd_case_type_assoc_seq",
    allocationSize = 1)
@Getter
@Setter
public class ServiceToCcdCaseTypeAssoc implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_to_ccd_case_type_assoc_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "ccd_service_name")
    @Size(max = 256)
    private String ccdServiceName;

    @Column(name = "ccd_case_type")
    @Size(max = 256)
    private String  ccdCaseType;

    @ManyToOne
    @JoinColumn(name = "service_code",referencedColumnName = "service_code", insertable = false, updatable = false)
    private Service service;

    public boolean equals(Object o) {
        if (nonNull(o) && o instanceof ServiceToCcdCaseTypeAssoc assoc) {
            if (this == assoc) {
                return true;
            }
            if (nonNull(this.ccdServiceName) && nonNull(this.getService()) && nonNull(assoc.getService())) {
                return this.ccdServiceName.equals(assoc.ccdServiceName)
                    && this.getService().getServiceCode().equals(assoc.getService().getServiceCode());
            }
        }
        return false;
    }

    public int hashCode() {
        if (nonNull(this.ccdServiceName)) {
            return this.ccdServiceName.length();
        }
        return 1;
    }

}
