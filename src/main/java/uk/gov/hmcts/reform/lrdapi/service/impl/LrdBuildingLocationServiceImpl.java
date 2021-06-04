package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;
import uk.gov.hmcts.reform.lrdapi.service.LrdBuildingLocationService;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
@Service
public class LrdBuildingLocationServiceImpl implements LrdBuildingLocationService {

    @Autowired
    BuildingLocationRepository buildingLocationRepository;

    @Override
    public LrdBuildingLocationResponse retrieveBuildingLocationByEpimsId(String epimsId) {

        BuildingLocation buildingLocation = buildingLocationRepository.findByEpimmsId(epimsId);

        if (isEmpty(buildingLocation)) {
            throw new ResourceNotFoundException("No Building Location found with the given epims ID: " + epimsId);
        }

        return LrdBuildingLocationResponse.builder()
            .buildingLocationId(buildingLocation.getBuildingLocationId())
            .buildingLocationName(buildingLocation.getBuildingLocationName())
            .buildingLocationStatus(buildingLocation.getBuildingLocationStatus().getStatus())
            .address(buildingLocation.getAddress())
            .area(buildingLocation.getArea())
            .epimmsId(buildingLocation.getEpimmsId())
            .clusterId(buildingLocation.getCluster().getClusterId())
            .clusterName(buildingLocation.getCluster().getClusterName())
            .regionId(buildingLocation.getRegion().getRegionId())
            .region(buildingLocation.getRegion().getDescription())
            .courtFinderUrl(buildingLocation.getCourtFinderUrl())
            .postcode(buildingLocation.getPostcode())
            .build();
    }
}
