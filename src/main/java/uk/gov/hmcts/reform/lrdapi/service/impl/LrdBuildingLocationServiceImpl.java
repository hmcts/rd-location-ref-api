package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;
import uk.gov.hmcts.reform.lrdapi.util.ValidationUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED;


@Slf4j
@Service
public class LrdBuildingLocationServiceImpl implements ILrdBuildingLocationService {

    @Autowired
    BuildingLocationRepository buildingLocationRepository;

    @Autowired
    ILrdBuildingLocationService buildingLocationService;

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Override
    public Object retrieveBuildingLocationDetails(String epimmsIds,
                                                  String buildingLocationName) {
        if (StringUtils.isNotBlank(epimmsIds)) {
            return retrieveDetailsByEpimmsId(epimmsIds);
        }
        if (StringUtils.isNotBlank(buildingLocationName)) {
            return retrieveDetailsByBuildingLocationName(buildingLocationName.strip());
        }

        return getAllBuildingLocations();

    }

    private Object retrieveDetailsByBuildingLocationName(String buildingLocationName) {
        BuildingLocation buildingLocation = buildingLocationRepository.findByBuildingLocationNameIgnoreCase(
            buildingLocationName);
        if (Objects.isNull(buildingLocation)) {
            throw new ResourceNotFoundException(String.format(
                "No Building Location found for the given building location name: %s ", buildingLocationName));
        }
        return buildResponse(buildingLocation);

    }

    private List<LrdBuildingLocationResponse> retrieveDetailsByEpimmsId(String epimmsId) {
        log.info("{} : Obtaining building locations for epimms id(s): {}", loggingComponentName, epimmsId);
        if (epimmsId.strip().equalsIgnoreCase(LocationRefConstants.ALL)) {
            return getAllBuildingLocations();
        }
        List<String> epimsIdList = ValidationUtils
            .checkIfValidCsvIdentifiersAndReturnList(epimmsId, EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED);
        if (ValidationUtils.isListContainsTextIgnoreCase(epimsIdList, LocationRefConstants.ALL)) {
            return getAllBuildingLocations();
        }
        ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList(epimsIdList,
                                                                      ALPHA_NUMERIC_REGEX, log,
                                                                      loggingComponentName,
                                                                      EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );


        List<BuildingLocation> buildingLocations = buildingLocationRepository.findByEpimmsIdIn(epimsIdList);

        if (isEmpty(buildingLocations)) {
            throw new ResourceNotFoundException("No Building Locations found with the given epims ID: " + epimsIdList);
        }

        return buildingLocations.stream().map(this::buildResponse)
            .collect(Collectors.toList());
    }

    private LrdBuildingLocationResponse buildResponse(BuildingLocation location) {
        return LrdBuildingLocationResponse.builder()
            .buildingLocationId(location.getBuildingLocationId().toString())
            .buildingLocationName(location.getBuildingLocationName())
            .buildingLocationStatus(location.getBuildingLocationStatus())
            .address(location.getAddress())
            .area(location.getArea())
            .epimmsId(location.getEpimmsId())
            .clusterId(location.getCluster().getClusterId())
            .clusterName(location.getCluster().getClusterName())
            .regionId(location.getRegion().getRegionId())
            .region(location.getRegion().getDescription())
            .courtFinderUrl(location.getCourtFinderUrl())
            .postcode(location.getPostcode())
            .build();
    }

    private List<LrdBuildingLocationResponse> getAllBuildingLocations() {
        List<BuildingLocation> buildingLocations = buildingLocationRepository.findAll();
        if (isEmpty(buildingLocations)) {
            throw new ResourceNotFoundException("There are no building locations available at the moment.");
        }
        return buildingLocations.stream().map(this::buildResponse)
            .collect(Collectors.toList());

    }
}
