package uk.gov.hmcts.reform.lrdapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "org_unit")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "org_unit_seq", sequenceName = "org_unit_seq", allocationSize = 1)
@Getter
@Setter
public class OrgUnit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_unit_seq")
    @Column(name = "org_unit_id")
    private Long orgUnitId;

    @Column(name = "description")
    private String  description;

    @OneToMany(targetEntity = Service.class, mappedBy = "orgUnit")
    @Fetch(FetchMode.SUBSELECT)
    private List<Service> services = new ArrayList<>();

    public OrgUnit(Long orgUnitId, String  description) {
        this.orgUnitId = orgUnitId;
        this.description = description;
    }
}
