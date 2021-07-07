package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants;
import uk.gov.hmcts.reform.lrdapi.controllers.response.CourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ClusterRepository;
import uk.gov.hmcts.reform.lrdapi.repository.CourtVenueRepository;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;
import uk.gov.hmcts.reform.lrdapi.util.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isRegexSatisfied;


@Slf4j
@Service
public class LrdBuildingLocationServiceImpl implements ILrdBuildingLocationService {

    @Autowired
    private BuildingLocationRepository buildingLocationRepository;

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private CourtVenueRepository courtVenueRepository;

    @Override
    public Object retrieveBuildingLocationDetails(String epimmsIds,
                                                  String buildingLocationName,
                                                  String regionId,
                                                  String clusterId) {
        if (isNotBlank(epimmsIds)) {
            return retrieveBuildingLocationsByEpimmsId(epimmsIds);
        }
        if (isNotBlank(buildingLocationName)) {
            return retrieveBuildingLocationsByName(buildingLocationName.strip());
        }
        if(isNotBlank(regionId)) {
            return retrieveBuildingLocationsByRegionId(regionId);
        }
        if(isNotBlank(clusterId)) {
            return retrieveBuildingLocationsByClusterId(clusterId);
        }

        return getAllBuildingLocations();

    }

    private Object retrieveBuildingLocationsByClusterId(String clusterId) {
        Set<BuildingLocation> buildingLocations;
        Set<CourtVenue> courtVenues;
        if(isRegexSatisfied(clusterId, NUMERIC_REGEX)) {
            Optional<Cluster> cluster = clusterRepository.findById(clusterId);
            if(cluster.isPresent()) {
                buildingLocations = cluster.get().getBuildingLocations();
                if(isEmpty(buildingLocations)) {
                    throw new ResourceNotFoundException(
                        String.format("No building locations found for cluster id - %s", clusterId));
                }
            } else {
                throw new ResourceNotFoundException(String.format(
                    "Cluster id: %s doesn't exist!", clusterId));
            }
        } else {
            throw new InvalidRequestException(String.format("Cluster id - %s is in invalid format!", clusterId));
        }
        return buildingLocations
            .stream()
            .map(building -> this.buildResponse(building, building.getCourtVenues()))
            .collect(Collectors.toList());
    }

    private Object retrieveBuildingLocationsByRegionId(String regionId) {
        if(isRegexSatisfied(regionId, NUMERIC_REGEX)) {

        }
        return new Object();
    }

    private Object retrieveBuildingLocationsByName(String buildingLocationName) {
        BuildingLocation buildingLocation = buildingLocationRepository.findByBuildingLocationNameIgnoreCase(
            buildingLocationName);
        if (isNull(buildingLocation)) {
            throw new ResourceNotFoundException(String.format(
                "No Building Location found for the given building location name: %s ", buildingLocationName));
        }
        return buildResponse(buildingLocation, buildingLocation.getCourtVenues());

    }

    private List<LrdBuildingLocationResponse> retrieveBuildingLocationsByEpimmsId(String epimmsId) {
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


        List<BuildingLocation> buildingLocations = buildingLocationRepository.findByEpimmsId(epimsIdList);

        if (isEmpty(buildingLocations)) {
            throw new ResourceNotFoundException("No Building Locations found with the given epims ID: " + epimsIdList);
        }

        return buildingLocations
            .stream()
            .map(buildingLocation -> this.buildResponse(buildingLocation, buildingLocation.getCourtVenues()))
            .collect(Collectors.toList());
    }

    private LrdBuildingLocationResponse buildResponse(BuildingLocation location, Set<CourtVenue> courtVenues) {
        Set<CourtVenueResponse> courtVenueResponses =  getCourtVenueResponses(courtVenues);
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
            .courtVenues(courtVenueResponses)
            .build();
    }

    private Set<CourtVenueResponse> getCourtVenueResponses(Set<CourtVenue> courtVenues) {
        return courtVenues
            .stream()
            .map(CourtVenueResponse::new)
            .collect(Collectors.toUnmodifiableSet());
    }

    private List<LrdBuildingLocationResponse> getAllBuildingLocations() {
        List<BuildingLocation> buildingLocations = buildingLocationRepository.findByBuildingLocationStatusOpen();
        if (isEmpty(buildingLocations)) {
            throw new ResourceNotFoundException("There are no building locations available at the moment.");
        }
        return buildingLocations
            .stream()
            .map(buildingLocation -> this.buildResponse(buildingLocation, buildingLocation.getCourtVenues()))
            .collect(Collectors.toList());

    }
}
