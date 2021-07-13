package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;
import uk.gov.hmcts.reform.lrdapi.service.RegionService;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.replaceIgnoreCase;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALL;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHABET_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_REGION_DESCRIPTION_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_REGION_ID_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NON_NUMERIC_VALUE_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REGION_NAME_REGEX;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfValidCsvIdentifiersAndReturnList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isListContainsTextIgnoreCase;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isPatternPresent;

@Service
@Slf4j
public class RegionServiceImpl implements RegionService {

    @Autowired
    RegionRepository regionRepository;

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    public List<LrdRegionResponse> retrieveRegionDetails(String regionId, String description) {
        if (isNotBlank(regionId)) {
            return retrieveRegionByRegionId(regionId);
        }

        if (isNotBlank(description)) {
            return retrieveRegionByRegionDescription(description);
        }

        return retrieveAllRegions(() -> regionRepository.findByApiEnabled(true));
    }

    public List<LrdRegionResponse> retrieveRegionByRegionId(String regionId) {
        isRegionIdParamValid(regionId);

        //Check if Param value provided is 'ALL' - if so, retrieve all Regions except National
        if (regionId.strip().equalsIgnoreCase(ALL)) {
            return retrieveAllRegions(() -> regionRepository.findAll());
        }

        //otherwise generate list of IDs from regionId String
        List<String> regionsIdList =
            checkIfValidCsvIdentifiersAndReturnList(regionId, EXCEPTION_MSG_NO_VALID_REGION_ID_PASSED);

        //then check if List contains 'ALL'
        if (isListContainsTextIgnoreCase(regionsIdList, ALL)) {
            return retrieveAllRegions(() -> regionRepository.findAll());
        }

        //remove invalid IDs from the list
        checkForInvalidIdentifiersAndRemoveFromIdList(
            regionsIdList, ALPHA_NUMERIC_REGEX, log, loggingComponentName, EXCEPTION_MSG_NO_VALID_REGION_ID_PASSED);

        List<Region> regions = regionRepository.findByRegionIdIn(regionsIdList);


        handleIfRegionsEmpty(
            () -> isEmpty(regions), "No Region(s) found with the given Region ID: " + regionsIdList
        );

        return generateResponseList(regions);
    }

    public List<LrdRegionResponse> retrieveRegionByRegionDescription(String description) {
        //generate list of Descriptions from description String
        List<String> regionDescriptionsList =
            checkIfValidCsvIdentifiersAndReturnList(description, EXCEPTION_MSG_NO_VALID_REGION_DESCRIPTION_PASSED);

        //remove invalid Regions from the list
        checkForInvalidIdentifiersAndRemoveFromIdList(
            regionDescriptionsList, REGION_NAME_REGEX, log,
            loggingComponentName, EXCEPTION_MSG_NO_VALID_REGION_DESCRIPTION_PASSED
        );

        List<Region> regions = regionRepository.findByDescriptionInIgnoreCase(regionDescriptionsList);

        handleIfRegionsEmpty(
            () -> isEmpty(regions),
            "No Region(s) found with the given Region Description(s): " + regionDescriptionsList
        );

        return generateResponseList(regions);
    }

    public List<LrdRegionResponse> retrieveAllRegions(Supplier<List<Region>> regionSupplier) {
        return generateResponseList(regionSupplier.get());
    }

    private void isRegionIdParamValid(String param) {
        if (param.toUpperCase().contains(ALL) && doesStringContainAlphabetOtherThanAll(param)
            || doesStringContainAlphabetOtherThanAll(param)) {
            throw new InvalidRequestException(NON_NUMERIC_VALUE_ERROR_MESSAGE);
        }
    }

    private boolean doesStringContainAlphabetOtherThanAll(String param) {
        String paramWithoutAll = replaceIgnoreCase(param.toUpperCase(), ALL, "");

        return isPatternPresent(paramWithoutAll, ALPHABET_REGEX);
    }

    private List<LrdRegionResponse> generateResponseList(List<Region> regions) {
        return regions.stream().map(LrdRegionResponse::new).collect(Collectors.toList());
    }

    private void handleIfRegionsEmpty(BooleanSupplier regionResponseVerifier, String exceptionMessage) {
        if (regionResponseVerifier.getAsBoolean()) {
            throw new ResourceNotFoundException(exceptionMessage);
        }
    }
}
