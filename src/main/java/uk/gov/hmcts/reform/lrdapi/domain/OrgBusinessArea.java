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

@Entity(name = "org_business_area")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "org_business_area_seq", sequenceName = "org_business_area_seq", allocationSize = 1)
@Getter
@Setter
public class OrgBusinessArea implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_business_area_seq")
    @Column(name = "business_area_id")
    private Long businessAreaId;

    @Column(name = "description")
    private String  description;

    @OneToMany(targetEntity = Service.class, mappedBy = "orgBusinessArea")
    @Fetch(FetchMode.SUBSELECT)
    private List<Service> services = new ArrayList<>();

    public OrgBusinessArea(Long businessAreaId, String  description) {
        this.businessAreaId = businessAreaId;
        this.description = description;
    }
}
