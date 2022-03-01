package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtTypeServiceAssoc;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;
import uk.gov.hmcts.reform.lrdapi.repository.CourtTypeServiceAssocRepository;
import uk.gov.hmcts.reform.lrdapi.repository.CourtVenueRepository;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.lang.BooleanUtils.isFalse;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COMMA;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.IS_CASE_MANAGEMENT_LOCATION_N;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.IS_CASE_MANAGEMENT_LOCATION_Y;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.IS_HEARING_LOCATION_N;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.IS_HEARING_LOCATION_Y;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.IS_TEMPORARY_LOCATION_N;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.IS_TEMPORARY_LOCATION_Y;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_CLUSTER_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_COURT_TYPE_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_COURT_VENUE_NAME;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_FOR_EPIMMS_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_REGION_ID;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfValidCsvIdentifiersAndReturnList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isListContainsTextIgnoreCase;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isRegexSatisfied;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.trimCourtVenueRequestParam;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.validateCourtVenueFilters;

@Slf4j
@Service
public class CourtVenueServiceImpl implements CourtVenueService {

    @Autowired
    CourtTypeServiceAssocRepository courtTypeServiceAssocRepository;

    @Autowired
    CourtVenueRepository courtVenueRepository;

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    public static void validateServiceCode(String serviceCode) {

        if (isBlank(serviceCode)) {
            throw new InvalidRequestException("No service code provided");
        }
        if (isFalse(isRegexSatisfied(serviceCode, ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE))) {
            throw new InvalidRequestException(EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR);
        }
    }

    @Override
    public LrdCourtVenuesByServiceCodeResponse retrieveCourtVenuesByServiceCode(String serviceCode) {

        String serviceCodeIgnoreCase = serviceCode.toUpperCase();

        CourtTypeServiceAssoc courtTypeServiceAssoc =
            courtTypeServiceAssocRepository.findByServiceCode(serviceCodeIgnoreCase);

        if (isNull(courtTypeServiceAssoc)) {
            throw new ResourceNotFoundException("No court types found for the given service code " + serviceCode);
        }

        CourtType courtType = courtTypeServiceAssoc.getCourtType();

        if (CollectionUtils.isEmpty(courtType.getCourtVenues())) {
            throw new ResourceNotFoundException("No court venues found for the given service code " + serviceCode);
        }

        return new LrdCourtVenuesByServiceCodeResponse(courtType, serviceCodeIgnoreCase);

    }

    @Override
    public List<LrdCourtVenueResponse> retrieveCourtVenuesBySearchString(String searchString, String courtTypeId,
                                                                         CourtVenueRequestParam requestParam) {
        log.info("{} : Obtaining court venue for search String: searchString: {}, courtTypeId: {}, "
                     + "isHearingLocation: {}, isCaseManagementLocation: {}, locationType: {}, "
                     + "isTemporaryLocation: {} ",
                 loggingComponentName, searchString, courtTypeId, requestParam.getIsHearingLocation(),
                 requestParam.getIsCaseManagementLocation(),
                 requestParam.getLocationType(), requestParam.getIsTemporaryLocation());

        var result =  trimCourtVenueRequestParam(requestParam);
        validateCourtVenueFilters(result);

        List<String> courtTypeIdList = StringUtils.isEmpty(courtTypeId) ? null :
            Arrays.stream(courtTypeId.split(COMMA)).map(String::strip).collect(
                Collectors.toList());

        String isCaseManagementLocation = (StringUtils.isNotEmpty(result.getIsCaseManagementLocation()))
            ? result.getIsCaseManagementLocation().toUpperCase()
            : result.getIsCaseManagementLocation();

        String isHearingLocation = (StringUtils.isNotEmpty(result.getIsHearingLocation()))
            ? result.getIsHearingLocation().toUpperCase()
            : result.getIsHearingLocation();

        String locationType = (StringUtils.isNotEmpty(result.getLocationType()))
            ? result.getLocationType().toUpperCase()
            : result.getLocationType();

        String isTemporaryLocation = (StringUtils.isNotEmpty(result.getIsTemporaryLocation()))
            ? result.getIsTemporaryLocation().toUpperCase()
            : result.getIsTemporaryLocation();

        return   getCourtVenueListResponse(courtVenueRepository.findBySearchStringAndCourtTypeId(
            searchString.toUpperCase(),
            courtTypeIdList,
            isCaseManagementLocation,
            isHearingLocation,
            locationType,
            isTemporaryLocation
        ));
    }

    @Override
    public List<LrdCourtVenueResponse> retrieveCourtVenueDetails(String epimmsIds, Integer courtTypeId,
                                                                 Integer regionId, Integer clusterId,
                                                                 String courtVenueName,
                                                                 CourtVenueRequestParam courtVenueRequestParam) {
        if (isNotBlank(epimmsIds)) {
            return getLrdCourtVenueResponses(
                retrieveCourtVenuesByEpimmsId(epimmsIds),
                courtVenueRequestParam
            );

        }
        if (isNotEmpty(courtTypeId)) {
            log.info("{} : Obtaining court venues for court type id: {}", loggingComponentName, courtTypeId);

            List<LrdCourtVenueResponse> lrdCourtVenueResponse =
                getAllCourtVenues(
                    () -> courtVenueRepository.findByCourtTypeIdWithOpenCourtStatus(courtTypeId.toString()),
                    courtTypeId.toString(),
                    NO_COURT_VENUES_FOUND_FOR_COURT_TYPE_ID
                );
            return getLrdCourtVenueResponses(lrdCourtVenueResponse, courtVenueRequestParam);
        }
        if (isNotEmpty(regionId)) {
            log.info("{} : Obtaining court venues for region id: {}", loggingComponentName, regionId);
            List<LrdCourtVenueResponse> lrdCourtVenueResponse = getAllCourtVenues(
                () -> courtVenueRepository.findByRegionIdWithOpenCourtStatus(regionId.toString()),
                regionId.toString(),
                NO_COURT_VENUES_FOUND_FOR_REGION_ID
            );
            return getLrdCourtVenueResponses(lrdCourtVenueResponse, courtVenueRequestParam);

        }
        if (isNotEmpty(clusterId)) {
            log.info("{} : Obtaining court venues for cluster id: {}", loggingComponentName, clusterId);
            List<LrdCourtVenueResponse> lrdCourtVenueResponse = getAllCourtVenues(
                () -> courtVenueRepository.findByClusterIdWithOpenCourtStatus(clusterId.toString()),
                clusterId.toString(),
                NO_COURT_VENUES_FOUND_FOR_CLUSTER_ID
            );
            return getLrdCourtVenueResponses(lrdCourtVenueResponse, courtVenueRequestParam);
        }
        if (isNotEmpty(courtVenueName)) {
            log.info("{} : Obtaining court venues for court venue name: {}", loggingComponentName, courtVenueName);
            List<LrdCourtVenueResponse> lrdCourtVenueResponse = getAllCourtVenues(
                () -> courtVenueRepository.findByCourtVenueNameOrSiteName(courtVenueName.strip()),
                courtVenueName,
                NO_COURT_VENUES_FOUND_FOR_COURT_VENUE_NAME
            );
            return getLrdCourtVenueResponses(lrdCourtVenueResponse, courtVenueRequestParam);
        }
        List<LrdCourtVenueResponse> initialResult =
            getAllCourtVenues(() -> courtVenueRepository.findAllWithOpenCourtStatus(), null,
                              NO_COURT_VENUES_FOUND
            );
        return getLrdCourtVenueResponses(initialResult, courtVenueRequestParam);
    }

    private List<LrdCourtVenueResponse> retrieveCourtVenuesByEpimmsId(String epimmsId) {
        log.info("{} : Obtaining court venue for epimms id(s): {}", loggingComponentName, epimmsId);

        if (epimmsId.strip().equalsIgnoreCase(LocationRefConstants.ALL)) {
            return getAllCourtVenues(() -> courtVenueRepository.findAll(), null, NO_COURT_VENUES_FOUND);
        }
        List<String> epimsIdList = checkIfValidCsvIdentifiersAndReturnList(
            epimmsId,
            EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );
        if (isListContainsTextIgnoreCase(epimsIdList, LocationRefConstants.ALL)) {
            return getAllCourtVenues(() -> courtVenueRepository.findAll(), null, NO_COURT_VENUES_FOUND);
        }
        checkForInvalidIdentifiersAndRemoveFromIdList(
            epimsIdList,
            ALPHA_NUMERIC_REGEX, log,
            loggingComponentName,
            EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );

        List<CourtVenue> courtVenues = courtVenueRepository.findByEpimmsIdIn(epimsIdList);

        handleIfCourtVenuesEmpty(
            () -> isEmpty(courtVenues), NO_COURT_VENUES_FOUND_FOR_FOR_EPIMMS_ID, epimsIdList.toString()
        );

        return getCourtVenueListResponse(courtVenues);
    }

    private List<LrdCourtVenueResponse> getAllCourtVenues(Supplier<List<CourtVenue>> courtVenueSupplier, String id,
        String noDataFoundMessage) {

        List<CourtVenue> courtVenues = courtVenueSupplier.get();
        handleIfCourtVenuesEmpty(() -> isEmpty(courtVenues), noDataFoundMessage, id);
        return getCourtVenueListResponse(courtVenues);
    }

    private List<LrdCourtVenueResponse> getCourtVenueListResponse(List<CourtVenue> courtVenues) {
        return courtVenues
            .stream()
            .map(LrdCourtVenueResponse::new)
            .collect(Collectors.toUnmodifiableList());
    }

    private void handleIfCourtVenuesEmpty(BooleanSupplier courtVenueResponseVerifier,
                                          String noDataFoundMessage,
                                          String id) {
        if (courtVenueResponseVerifier.getAsBoolean()) {
            noDataFoundMessage = (isNotBlank(id)) ? String.format(noDataFoundMessage, id) : noDataFoundMessage;
            log.error("{} : {}", loggingComponentName, noDataFoundMessage);
            throw new ResourceNotFoundException(noDataFoundMessage);
        }
    }

    private List<LrdCourtVenueResponse> getLrdCourtVenueResponses(List<LrdCourtVenueResponse> inputLrdCourtVenueResponse,
        CourtVenueRequestParam courtVenueRequestParam) {

        List<LrdCourtVenueResponse> result = applyAdditionalFilters(
            inputLrdCourtVenueResponse,
            courtVenueRequestParam
        );
        if (isEmpty(result)) {
            throw new ResourceNotFoundException(NO_COURT_VENUES_FOUND);
        }
        return result;
    }

    private List<LrdCourtVenueResponse> applyAdditionalFilters(List<LrdCourtVenueResponse> inputLrdCourtVenueResponse,
        CourtVenueRequestParam courtVenueRequestParam) {

        List<Predicate<LrdCourtVenueResponse>> allPredicates = getPredicates(courtVenueRequestParam);


        return inputLrdCourtVenueResponse.stream()
            .filter(allPredicates.stream().reduce(x -> true, Predicate::and))
            .collect(Collectors.toList());

    }

    private List<Predicate<LrdCourtVenueResponse>> getPredicates(CourtVenueRequestParam courtVenueRequestParam) {

        List<Predicate<LrdCourtVenueResponse>> allPredicates = new ArrayList<>();

        String isHearingLocationValue = courtVenueRequestParam.getIsHearingLocation();

        if (IS_HEARING_LOCATION_Y.equalsIgnoreCase(isHearingLocationValue)
            || IS_HEARING_LOCATION_N.equalsIgnoreCase(isHearingLocationValue)) {
            allPredicates.add(
                courtVenue -> isHearingLocationValue.equalsIgnoreCase(courtVenue.getIsHearingLocation())
            );

        }

        String isCaseMgntLocationValue = courtVenueRequestParam.getIsCaseManagementLocation();

        if (IS_CASE_MANAGEMENT_LOCATION_Y.equalsIgnoreCase(isCaseMgntLocationValue)
            || IS_CASE_MANAGEMENT_LOCATION_N.equalsIgnoreCase(isCaseMgntLocationValue)) {
            allPredicates.add(
                courtVenue -> isCaseMgntLocationValue.equalsIgnoreCase(courtVenue.getIsCaseManagementLocation())
            );

        }

        String isTemporaryLocationValue = courtVenueRequestParam.getIsTemporaryLocation();

        if (IS_TEMPORARY_LOCATION_Y.equalsIgnoreCase(isTemporaryLocationValue)
            || IS_TEMPORARY_LOCATION_N.equalsIgnoreCase(isTemporaryLocationValue)) {
            allPredicates.add(
                courtVenue -> isTemporaryLocationValue.equalsIgnoreCase(courtVenue.getIsTemporaryLocation())
            );

        }

        String isLocationTypeValue = courtVenueRequestParam.getLocationType();

        if (StringUtils.isNotBlank(isLocationTypeValue)) {
            allPredicates.add(
                courtVenue -> isLocationTypeValue.equalsIgnoreCase(courtVenue.getLocationType())
            );

        }
        return allPredicates;
    }
}
