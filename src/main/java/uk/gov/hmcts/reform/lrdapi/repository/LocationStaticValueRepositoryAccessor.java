package uk.gov.hmcts.reform.lrdapi.repository;

import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocationStatus;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtLocationCategory;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.util.List;

public interface LocationStaticValueRepositoryAccessor {

    List<BuildingLocationStatus> getBuildingLocationStatus();

    List<CourtLocationCategory> getCourtLocationCategories();

    List<Region> getRegions();

    List<Cluster> getClusters();

}
