package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.lib.audit.domain.RowDomain;
import uk.gov.hmcts.reform.lrdapi.client.domain.Building;
import uk.gov.hmcts.reform.lrdapi.client.domain.Court;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocationStatus;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.CourtLocation;
import uk.gov.hmcts.reform.lrdapi.domain.CourtLocationCategory;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.domain.Service;
import uk.gov.hmcts.reform.lrdapi.domain.ServiceToCcdCaseTypeAssoc;
import uk.gov.hmcts.reform.lrdapi.repository.BuildingLocationRepository;
import uk.gov.hmcts.reform.lrdapi.repository.CourtLocationRepository;
import uk.gov.hmcts.reform.lrdapi.repository.LocationStaticValueRepositoryAccessor;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceToCcdCaseTypeAssocRepositry;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALL;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COMMA;

@org.springframework.stereotype.Service
@Slf4j
public class LrdServiceImpl implements LrdService {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    BuildingLocationRepository buildingLocationRepository;

    @Autowired
    CourtLocationRepository courtLocationRepository;

    @Autowired
    ServiceToCcdCaseTypeAssocRepositry serviceToCcdCaseTypeAssocRepositry;

    @Autowired
    LocationStaticValueRepositoryAccessor locationStaticValueRepositoryAccessor;

    @Override
    public List<LrdOrgInfoServiceResponse> retrieveOrgServiceDetails(String serviceCode,
                                                                     String ccdCaseType, String ccdServiceNames) {
        Service servicePojo;
        ServiceToCcdCaseTypeAssoc serToCcdCaseType;
        List<Service> services;
        final List<LrdOrgInfoServiceResponse> orgInfoServiceResponses = new ArrayList<>();
        if (StringUtils.isNotBlank(serviceCode)) {

            servicePojo = serviceRepository.findByServiceCode(serviceCode.trim().toUpperCase());
            ifServiceResponseNullThrowException(servicePojo);
            orgInfoServiceResponses.add(new LrdOrgInfoServiceResponse(servicePojo));

        } else if (StringUtils.isNotBlank(ccdCaseType)) {

            serToCcdCaseType = serviceToCcdCaseTypeAssocRepositry
                .findByCcdCaseTypeIgnoreCase(ccdCaseType.trim());
            servicePojo = serToCcdCaseType != null ? serToCcdCaseType.getService() : null;
            ifServiceResponseNullThrowException(servicePojo);
            orgInfoServiceResponses.add(new LrdOrgInfoServiceResponse(servicePojo));

        } else if (StringUtils.isNotBlank(ccdServiceNames) && !ccdServiceNames.equalsIgnoreCase(ALL)) {
            List<String> serviceNameList = Arrays.asList(ccdServiceNames.split(COMMA));

            List<ServiceToCcdCaseTypeAssoc> serviceToCcdCaseTypeAssocs = serviceToCcdCaseTypeAssocRepositry
                .findByCcdServiceNameInIgnoreCase(serviceNameList
                                                      .stream()
                                                      .map(String::trim)
                                                      .collect(toList()));

            if (CollectionUtils.isEmpty(serviceToCcdCaseTypeAssocs)) {
                throw new EmptyResultDataAccessException(1);
            }

            List<ServiceToCcdCaseTypeAssoc> distinctAssociations =
                serviceToCcdCaseTypeAssocs.stream().distinct().collect(toList());

            distinctAssociations.forEach(association ->
                                                   orgInfoServiceResponses.add(
                                                       new LrdOrgInfoServiceResponse(association.getService())));

        } else {

            services = serviceRepository.findAll();

            if (null == services) {
                throw new EmptyResultDataAccessException(1);
            }
            services.forEach(service ->
                orgInfoServiceResponses.add(new LrdOrgInfoServiceResponse(service))
            );

        }
        return orgInfoServiceResponses;
    }

    public void ifServiceResponseNullThrowException(Service service) {
        if (null == service) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    @Override
    public void createLocations(List<RowDomain> lrdRecords, Boolean isBuildingLocation) {
        if (Boolean.TRUE.equals(isBuildingLocation)) {
            populateBuildingLocation(lrdRecords);
        } else {
            populateCourtLocation(lrdRecords);
        }
    }

    private void populateBuildingLocation(List<RowDomain> lrdRecords) {
        List<BuildingLocation> buildingLocations = new ArrayList<>();
        lrdRecords.forEach(buildingRecord ->
                               buildingLocations.add(mapBuildingToBuildingLocation((Building) buildingRecord)));
        buildingLocationRepository.saveAll(buildingLocations);
    }

    private BuildingLocation mapBuildingToBuildingLocation(Building buildingRecord) {
        BuildingLocation buildingLocation = new BuildingLocation();
        buildingLocation.setEpimmsId(buildingRecord.getEpimsId());
        buildingLocation.setBuildingLocationName(buildingRecord.getBuildingLocationName());
        buildingLocation.setBuildingLocationStatus(
            getBuildingLocationStatus(buildingRecord.getStatusId()).orElse(null));
        buildingLocation.setArea(buildingRecord.getArea());
        buildingLocation.setRegion(getRegion(buildingRecord.getRegionId()).orElse(null));
        buildingLocation.setCluster(getCluster(buildingRecord.getClusterId()).orElse(null));
        buildingLocation.setCourtFinderUrl(buildingRecord.getCourtFinderUrl());
        buildingLocation.setPostcode(buildingRecord.getPostcode());
        buildingLocation.setAddress(buildingRecord.getAddress());

        return buildingLocation;
    }

    private Optional<BuildingLocationStatus> getBuildingLocationStatus(String statusId) {
        return locationStaticValueRepositoryAccessor.getBuildingLocationStatus()
            .stream()
            .filter(buildingLocationStatus ->
                        statusId.equalsIgnoreCase(buildingLocationStatus.getBuildingLocationStatusId()))
            .findFirst();
    }

    private Optional<Region> getRegion(String regionId) {
        return locationStaticValueRepositoryAccessor.getRegions()
            .stream()
            .filter(region ->
                        regionId.equalsIgnoreCase(region.getRegionId()))
            .findFirst();
    }

    private Optional<Cluster> getCluster(String clusterId) {
        return locationStaticValueRepositoryAccessor.getClusters()
            .stream()
            .filter(cluster ->
                        clusterId.equalsIgnoreCase(cluster.getClusterId()))
            .findFirst();
    }

    private void populateCourtLocation(List<RowDomain> lrdRecords) {
        List<CourtLocation> courtLocations = new ArrayList<>();
        lrdRecords.forEach(courtRecord ->
                               courtLocations.add(mapCourtToCourtLocation((Court) courtRecord)));
        courtLocationRepository.saveAll(courtLocations);
    }

    private CourtLocation mapCourtToCourtLocation(Court courtRecord) {
        CourtLocation courtLocation = new CourtLocation();
        courtLocation.setBuildingLocation(getBuildingLocation(courtRecord.getEpimsId()));
        courtLocation.setCourtLocationName(courtRecord.getCourtLocationName());
        courtLocation.setRegion(getRegion(courtRecord.getRegionId()).orElse(null));
        courtLocation.setCourtLocationCategory(getCourtLocationCategory(courtRecord.getCourtLocationCategoryId()));
        courtLocation.setCluster(getCluster(courtRecord.getClusterId()).orElse(null));
        courtLocation.setOpenForPublic(Boolean.valueOf(courtRecord.getOpenForPublic()));
        courtLocation.setCourtAddress(courtRecord.getCourtAddress());
        courtLocation.setPostcode(courtRecord.getPostcode());
        courtLocation.setPhoneNumber(courtRecord.getPhoneNumber());
        courtLocation.setClosedDate(courtRecord.getClosedDate());
        courtLocation.setCourtLocationCode(courtRecord.getCourtLocationCode());
        courtLocation.setDxAddress(courtRecord.getDxAddress());
        courtLocation.setWelshCourtLocationName(courtRecord.getWelshCourtLocationName());
        courtLocation.setWelshCourtAddress(courtRecord.getWelshCourtAddress());

        return courtLocation;
    }

    private BuildingLocation getBuildingLocation(String epimsId) {
        return buildingLocationRepository.findByEpimmsId(epimsId);
    }

    private CourtLocationCategory getCourtLocationCategory(String courtLocationCategoryId) {
        return locationStaticValueRepositoryAccessor.getCourtLocationCategories()
            .stream()
            .filter(courtLocationCategory ->
                        courtLocationCategoryId.equalsIgnoreCase(courtLocationCategory.getCourtLocationCategoryId()))
            .findFirst()
            .get();
    }


}
