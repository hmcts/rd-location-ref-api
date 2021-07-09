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
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COMMA;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INVALID_CLUSTER_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INVALID_REGION_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATIONS;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATIONS_FOR_CLUSTER_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATIONS_FOR_EPIMMS_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATIONS_FOR_REGION_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_BUILDING_LOCATION_FOR_BUILDING_LOCATION_NAME;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfSingleValuePresent;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfValidCsvIdentifiersAndReturnList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isListContainsTextIgnoreCase;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isRegexSatisfied;

@Slf4j
@Service
public class LrdBuildingLocationServiceImpl implements ILrdBuildingLocationService {

    @Autowired
    private BuildingLocationRepository buildingLocationRepository;

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Override
    public Object retrieveBuildingLocationDetails(String epimmsIds,
                                                  String buildingLocationName,
                                                  String regionId,
                                                  String clusterId) {
        String id;
        if (isNotBlank(epimmsIds)) {
            return retrieveBuildingLocationsByEpimmsId(epimmsIds);
        }
        if (isNotBlank(buildingLocationName)) {
            checkIfSingleValuePresent(buildingLocationName.split(COMMA));
            return retrieveBuildingLocationsByName(buildingLocationName.strip());
        }
        if (isNotBlank(regionId)) {
            log.info("{} : Obtaining building locations for the region id: {}", loggingComponentName, regionId);
            id = regionId.strip();
            return getBuildingLocationsForNumericFilters(
                id, INVALID_REGION_ID,
                NO_BUILDING_LOCATIONS_FOR_REGION_ID,
                () -> buildingLocationRepository.findByRegionId(id)
            );
        }
        if (isNotBlank(clusterId)) {
            log.info("{} : Obtaining building locations for the cluster id: {}", loggingComponentName, clusterId);
            id = clusterId.strip();
            return getBuildingLocationsForNumericFilters(
                id, INVALID_CLUSTER_ID,
                NO_BUILDING_LOCATIONS_FOR_CLUSTER_ID,
                () -> buildingLocationRepository.findByClusterId(id)
            );
        }

        return getAllBuildingLocations(() -> buildingLocationRepository.findByBuildingLocationStatusOpen());
    }

    private List<LrdBuildingLocationResponse> getBuildingLocationsForNumericFilters(
        String id,
        String invalidExceptionMsg,
        String noBuildingLocationsMsg,
        Supplier<List<BuildingLocation>> buildingLocationSupplier) {

        validateNumericFilter(id, invalidExceptionMsg);
        List<BuildingLocation> buildingLocations = buildingLocationSupplier.get();
        handleIfBuildingLocationsEmpty(
            () -> isEmpty(buildingLocations),
            noBuildingLocationsMsg,
            id
        );

        return getBuildingLocationListResponse(buildingLocations);
    }

    private Object retrieveBuildingLocationsByName(String buildingLocationName) {
        BuildingLocation buildingLocation = buildingLocationRepository.findByBuildingLocationNameIgnoreCase(
            buildingLocationName);
        handleIfBuildingLocationsEmpty(
            () -> isNull(buildingLocation),
            NO_BUILDING_LOCATION_FOR_BUILDING_LOCATION_NAME,
            buildingLocationName
        );
        return buildResponse(buildingLocation, buildingLocation.getCourtVenues());

    }

    private List<LrdBuildingLocationResponse> retrieveBuildingLocationsByEpimmsId(String epimmsId) {
        log.info("{} : Obtaining building locations for epimms id(s): {}", loggingComponentName, epimmsId);
        if (epimmsId.strip().equalsIgnoreCase(LocationRefConstants.ALL)) {
            return getAllBuildingLocations(() -> buildingLocationRepository.findAll());
        }
        List<String> epimsIdList = checkIfValidCsvIdentifiersAndReturnList(
            epimmsId,
            EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );
        if (isListContainsTextIgnoreCase(epimsIdList, LocationRefConstants.ALL)) {
            return getAllBuildingLocations(() -> buildingLocationRepository.findAll());
        }
        checkForInvalidIdentifiersAndRemoveFromIdList(
            epimsIdList,
            ALPHA_NUMERIC_REGEX, log,
            loggingComponentName,
            EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );

        List<BuildingLocation> buildingLocations = buildingLocationRepository.findByEpimmsId(epimsIdList);

        handleIfBuildingLocationsEmpty(
            () -> isEmpty(buildingLocations),
            NO_BUILDING_LOCATIONS_FOR_EPIMMS_ID,
            epimsIdList.toString()
        );

        return getBuildingLocationListResponse(buildingLocations);
    }

    private LrdBuildingLocationResponse buildResponse(BuildingLocation location, Set<CourtVenue> courtVenues) {
        LrdBuildingLocationResponse.LrdBuildingLocationResponseBuilder lrdBuildingLocationResponseBuilder =
            LrdBuildingLocationResponse.builder()
                .buildingLocationId(location.getBuildingLocationId().toString())
                .buildingLocationName(location.getBuildingLocationName())
                .buildingLocationStatus(location.getBuildingLocationStatus())
                .address(location.getAddress())
                .area(location.getArea())
                .epimmsId(location.getEpimmsId());
        location.getCluster().ifPresent(cluster -> {
            lrdBuildingLocationResponseBuilder.clusterId(cluster.getClusterId());
            lrdBuildingLocationResponseBuilder.clusterName(cluster.getClusterName());
        });
        location.getRegion().ifPresent(region -> {
            lrdBuildingLocationResponseBuilder.regionId(region.getRegionId());
            lrdBuildingLocationResponseBuilder.region(region.getDescription());
        });

        if (!courtVenues.isEmpty()) {
            lrdBuildingLocationResponseBuilder.courtVenues(getCourtVenueResponses(courtVenues));
        }

        return lrdBuildingLocationResponseBuilder
            .courtFinderUrl(location.getCourtFinderUrl())
            .postcode(location.getPostcode())
            .build();
    }

    private Set<CourtVenueResponse> getCourtVenueResponses(Set<CourtVenue> courtVenues) {
        return courtVenues
            .stream()
            .map(CourtVenueResponse::new)
            .collect(Collectors.toUnmodifiableSet());
    }

    private List<LrdBuildingLocationResponse> getAllBuildingLocations(
        Supplier<List<BuildingLocation>> buildingLocationSupplier) {
        List<BuildingLocation> buildingLocations = buildingLocationSupplier.get();
        handleIfBuildingLocationsEmpty(() -> isEmpty(buildingLocations), NO_BUILDING_LOCATIONS, null);
        return getBuildingLocationListResponse(buildingLocations);
    }

    private void validateNumericFilter(String id, String invalidExceptionMsg) {
        checkIfSingleValuePresent(id.split(COMMA));
        if (!isRegexSatisfied(id, NUMERIC_REGEX)) {
            invalidExceptionMsg = String.format(invalidExceptionMsg, id);
            log.error(invalidExceptionMsg);
            throw new InvalidRequestException(invalidExceptionMsg);
        }
    }

    private List<LrdBuildingLocationResponse> getBuildingLocationListResponse(
        List<BuildingLocation> buildingLocations) {
        return buildingLocations
            .stream()
            .map(buildingLocation -> this.buildResponse(buildingLocation, buildingLocation.getCourtVenues()))
            .collect(Collectors.toList());
    }

    private void handleIfBuildingLocationsEmpty(Supplier<Boolean> buildingLocationResponseVerifier,
                                                String noLocationsMsg,
                                                String id) {
        if (buildingLocationResponseVerifier.get()) {
            noLocationsMsg = (isNotBlank(id)) ? String.format(noLocationsMsg, id) : noLocationsMsg;
            log.error("{} : {}",loggingComponentName, noLocationsMsg);
            throw new ResourceNotFoundException(noLocationsMsg);
        }
    }
}
