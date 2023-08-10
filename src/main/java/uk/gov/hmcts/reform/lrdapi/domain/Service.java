package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NamedEntityGraph(
        name = "Service.alljoins",
        attributeNodes = {
                @NamedAttributeNode(value = "jurisdiction"),
                @NamedAttributeNode(value = "orgBusinessArea"),
                @NamedAttributeNode(value = "orgUnit"),
                @NamedAttributeNode(value = "orgSubBusinessArea"),
                @NamedAttributeNode(value = "serviceToCcdCaseTypeAssocs")
        }
)
public class Service implements Serializable {

    @Id
    @Column(name = "service_id")
    private Long  serviceId;

    @Column(name = "service_code")
    @Size(max = 16)
    @NaturalId
    private String serviceCode;

    @Column(name = "service_description")
    @Size(max = 512)
    private String serviceDescription;

    @Column(name = "service_short_description")
    @Size(max = 256)
    private String  serviceShortDescription;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @OneToMany(targetEntity = ServiceToCcdCaseTypeAssoc.class, mappedBy = "service")
    private List<ServiceToCcdCaseTypeAssoc> serviceToCcdCaseTypeAssocs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "jurisdiction_id",nullable = false, insertable = false, updatable = false)
    private Jurisdiction jurisdiction;

    @ManyToOne
    @JoinColumn(name = "business_area_id", nullable = false, insertable = false, updatable = false)
    private OrgBusinessArea orgBusinessArea;

    @ManyToOne
    @JoinColumn(name = "org_unit_id", nullable = false, insertable = false, updatable = false)
    private OrgUnit orgUnit;

    @ManyToOne
    @JoinColumn(name = "sub_business_area_id", nullable = false, insertable = false, updatable = false)
    private OrgSubBusinessArea orgSubBusinessArea;

    @OneToMany(targetEntity = CourtTypeServiceAssoc.class, mappedBy = "service")
    @Fetch(FetchMode.SUBSELECT)
    private List<CourtTypeServiceAssoc> courtTypeServiceAssocs;

}
