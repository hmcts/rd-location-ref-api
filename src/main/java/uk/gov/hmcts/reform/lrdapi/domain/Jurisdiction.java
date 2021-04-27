package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;


@Entity(name = "jurisdiction")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "jurisdiction_seq", sequenceName = "jurisdiction_seq", allocationSize = 1)
@Getter
@Setter
public class Jurisdiction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jurisdiction_seq")
    @Column(name = "jurisdiction_id")
    private Long jurisdictionId;

    @Column(name = "description")
    private String  description;

    @OneToMany(targetEntity = Service.class, mappedBy = "jurisdiction")
    @Fetch(FetchMode.SUBSELECT)
    private List<Service> services = new ArrayList<>();

    public Jurisdiction(Long jurisdictionId, String  description) {
        this.jurisdictionId = jurisdictionId;
        this.description = description;
    }
}
