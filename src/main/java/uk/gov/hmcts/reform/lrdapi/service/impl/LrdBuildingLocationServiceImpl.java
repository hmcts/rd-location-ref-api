package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
@Service
public class LrdBuildingLocationServiceImpl implements ILrdBuildingLocationService {

    @Autowired
    BuildingLocationRepository buildingLocationRepository;

    @Override
    public List<LrdBuildingLocationResponse> retrieveBuildingLocationByEpimsIds(List<String> epimsIds) {

        List<BuildingLocation> buildingLocations = buildingLocationRepository.findByEpimmsIdIn(epimsIds);

        if (isEmpty(buildingLocations)) {
            throw new ResourceNotFoundException("No Building Locations found with the given epims ID: " + epimsIds);
        }

        return buildResponse(buildingLocations);
    }

    @Override
    public List<LrdBuildingLocationResponse> getAllBuildingLocations() {
        List<BuildingLocation> buildingLocations = buildingLocationRepository.findAll();
        if (isEmpty(buildingLocations)) {
            throw new ResourceNotFoundException("There are no building locations available at the moment.");
        }
        return buildResponse(buildingLocations);
    }

    private List<LrdBuildingLocationResponse> buildResponse(List<BuildingLocation> buildingLocations) {
        return buildingLocations.stream().map((location) -> LrdBuildingLocationResponse.builder()
            .buildingLocationId(location.getBuildingLocationId())
            .buildingLocationName(location.getBuildingLocationName())
            .buildingLocationStatus(location.getBuildingLocationStatus().getStatus())
            .address(location.getAddress())
            .area(location.getArea())
            .epimmsId(location.getEpimmsId())
            .clusterId(location.getCluster().getClusterId())
            .clusterName(location.getCluster().getClusterName())
            .regionId(location.getRegion().getRegionId())
            .region(location.getRegion().getDescription())
            .courtFinderUrl(location.getCourtFinderUrl())
            .postcode(location.getPostcode())
            .build()).collect(Collectors.toList());
    }
}
