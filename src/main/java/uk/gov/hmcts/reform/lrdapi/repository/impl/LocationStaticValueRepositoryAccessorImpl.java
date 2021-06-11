package uk.gov.hmcts.reform.lrdapi.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocationStatus;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtLocationCategory;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.LocationStaticValueRepositoryAccessor;

import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;

@Component
public class LocationStaticValueRepositoryAccessorImpl implements LocationStaticValueRepositoryAccessor {

    private List<BuildingLocationStatus> buildingLocationStatuses;

    private List<CourtLocationCategory> courtLocationCategories;

    private List<Region> regions;

    private List<Cluster> clusters;

    @Autowired
    private SimpleJpaRepository<BuildingLocationStatus, String> buildingLocationStatusRepository;
    @Autowired
    private SimpleJpaRepository<CourtLocationCategory, String> courtLocationCategoryRepository;
    @Autowired
    private SimpleJpaRepository<Region, String> regionRepository;
    @Autowired
    private SimpleJpaRepository<Cluster, String> clusterRepository;

    @PostConstruct
    public void initialize() {
        buildingLocationStatuses = Collections.unmodifiableList(buildingLocationStatusRepository.findAll());
        courtLocationCategories = Collections.unmodifiableList(courtLocationCategoryRepository.findAll());
        regions = Collections.unmodifiableList(regionRepository.findAll());
        clusters = Collections.unmodifiableList(clusterRepository.findAll());
    }

    @Override
    public List<BuildingLocationStatus> getBuildingLocationStatus() {
        return buildingLocationStatuses;
    }

    @Override
    public List<CourtLocationCategory> getCourtLocationCategories() {
        return courtLocationCategories;
    }

    @Override
    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public List<Cluster> getClusters() {
        return clusters;
    }

}
