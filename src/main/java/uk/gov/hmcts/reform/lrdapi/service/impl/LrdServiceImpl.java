package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
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

import static java.util.Objects.nonNull;
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

    private void populateCourtLocation(List<RowDomain> lrdRecords) {
        List<CourtLocation> courtLocations = new ArrayList<>();
        lrdRecords.forEach(courtRecord ->
                               courtLocations.add(mapCourtToCourtLocation((Court) courtRecord)));
        courtLocationRepository.saveAll(courtLocations);
    }

    private BuildingLocation mapBuildingToBuildingLocation(Building buildingRecord) {
        BuildingLocation buildingLocation = new BuildingLocation();
        //TODO: temporary, change this
        buildingLocation.setBuildingLocationId(RandomStringUtils.randomNumeric(15));
        buildingLocation.setEpimmsId(convertIntToString(buildingRecord.getEpimsId()));
        buildingLocation.setBuildingLocationName(buildingRecord.getBuildingLocationName());
        buildingLocation.setBuildingLocationStatus(
            getBuildingLocationStatus(convertIntToString(buildingRecord.getStatusId())));
        buildingLocation.setArea(buildingRecord.getArea());
        buildingLocation.setRegion(getRegion(convertIntToString(buildingRecord.getRegionId())));
        buildingLocation.setCluster(getCluster(convertIntToString(buildingRecord.getClusterId())));
        buildingLocation.setCourtFinderUrl(buildingRecord.getCourtFinderUrl());
        buildingLocation.setPostcode(buildingRecord.getPostcode());
        buildingLocation.setAddress(buildingRecord.getAddress());

        return buildingLocation;
    }

    private CourtLocation mapCourtToCourtLocation(Court courtRecord) {
        CourtLocation courtLocation = new CourtLocation();
        courtLocation.setCourtLocationId(RandomStringUtils.randomNumeric(15));
        courtLocation.setBuildingLocation(getBuildingLocation(convertIntToString(courtRecord.getEpimsId())));
        courtLocation.setCourtLocationName(courtRecord.getCourtLocationName());
        courtLocation.setRegion(getRegion(convertIntToString(courtRecord.getRegionId())));
        courtLocation.setCourtLocationCategory(getCourtLocationCategory(
            convertIntToString(courtRecord.getCourtLocationCategoryId())));
        courtLocation.setCluster(getCluster(convertIntToString(courtRecord.getClusterId())));
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

    private String convertIntToString(Integer id) {
        if (nonNull(id)) {
            return id.toString();
        }
        return StringUtils.EMPTY;
    }


    private BuildingLocationStatus getBuildingLocationStatus(String statusId) {
        if (StringUtils.isNotEmpty(statusId)) {
            return locationStaticValueRepositoryAccessor.getBuildingLocationStatus()
                .stream()
                .filter(buildingLocationStatus ->
                            statusId.equalsIgnoreCase(buildingLocationStatus.getBuildingLocationStatusId()))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    private Region getRegion(String regionId) {
        if (StringUtils.isNotEmpty(regionId)) {
            return locationStaticValueRepositoryAccessor.getRegions()
                .stream()
                .filter(region ->
                            regionId.equalsIgnoreCase(region.getRegionId()))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    private Cluster getCluster(String clusterId) {
        if (StringUtils.isNotEmpty(clusterId)) {
            return locationStaticValueRepositoryAccessor.getClusters()
                .stream()
                .filter(cluster ->
                            clusterId.equalsIgnoreCase(cluster.getClusterId()))
                .findFirst()
                .orElse(null);
        }
        return null;
    }


    private BuildingLocation getBuildingLocation(String epimsId) {
        return buildingLocationRepository.findByEpimmsId(epimsId);
    }

    private CourtLocationCategory getCourtLocationCategory(String courtLocationCategoryId) {
        if (StringUtils.isNotEmpty(courtLocationCategoryId)) {
            return locationStaticValueRepositoryAccessor.getCourtLocationCategories()
                .stream()
                .filter(courtLocationCategory ->
                           courtLocationCategoryId.equalsIgnoreCase(courtLocationCategory.getCourtLocationCategoryId()))
                .findFirst()
                .orElse(null);
        }
        return null;
    }


}
