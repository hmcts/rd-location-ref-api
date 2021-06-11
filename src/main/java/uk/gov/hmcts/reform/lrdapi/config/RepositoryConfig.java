package uk.gov.hmcts.reform.lrdapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocationStatus;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtLocationCategory;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import javax.persistence.EntityManager;

@Configuration
public class RepositoryConfig {

    @Autowired
    EntityManager entityManager;

    @Bean
    public SimpleJpaRepository<BuildingLocationStatus, String> getBuildingLocationStatusRepository() {
        return new SimpleJpaRepository<>(BuildingLocationStatus.class, entityManager);
    }

    @Bean
    public SimpleJpaRepository<CourtLocationCategory, String> getCourtLocationCategoryRepository() {
        return new SimpleJpaRepository<>(CourtLocationCategory.class, entityManager);
    }

    @Bean
    public SimpleJpaRepository<Region, String> getRegionRepository() {
        return new SimpleJpaRepository<>(Region.class, entityManager);
    }

    @Bean
    public SimpleJpaRepository<Cluster, String> getClusterRepository() {
        return new SimpleJpaRepository<>(Cluster.class, entityManager);
    }
}
