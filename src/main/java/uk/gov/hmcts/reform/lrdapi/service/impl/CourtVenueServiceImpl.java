package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
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
import uk.gov.hmcts.reform.lrdapi.repository.CourtTypeServiceAssocRepository;
import uk.gov.hmcts.reform.lrdapi.repository.CourtVenueRepository;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_CLUSTER_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_COURT_TYPE_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_FOR_EPIMMS_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.NO_COURT_VENUES_FOUND_FOR_REGION_ID;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfValidCsvIdentifiersAndReturnList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isListContainsTextIgnoreCase;

@Slf4j
@Service
public class CourtVenueServiceImpl implements CourtVenueService {

    @Autowired
    CourtTypeServiceAssocRepository courtTypeServiceAssocRepository;

    @Autowired
    CourtVenueRepository courtVenueRepository;

    @Value("${loggingComponentName}")
    private String loggingComponentName;

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

        return new LrdCourtVenuesByServiceCodeResponse(courtType,serviceCodeIgnoreCase);

    }

    @Override
    public List<LrdCourtVenueResponse> retrieveCourtVenueDetails(String epimmsIds, Integer courtTypeId,
                                                                 Integer regionId, Integer clusterId) {
        if (isNotBlank(epimmsIds)) {
            return retrieveCourtVenuesByEpimmsId(epimmsIds);
        } else if (!Objects.isNull(courtTypeId)) {
            log.info("{} : Obtaining court venues for court type id: {}", loggingComponentName, courtTypeId);
            return getAllCourtVenues(() -> courtVenueRepository
                                         .findByCourtTypeIdWithOpenCourtStatus(courtTypeId.toString()),
                                     courtTypeId.toString(), NO_COURT_VENUES_FOUND_FOR_COURT_TYPE_ID);
        } else if (!Objects.isNull(regionId)) {
            log.info("{} : Obtaining court venues for region id: {}", loggingComponentName, regionId);
            return getAllCourtVenues(() -> courtVenueRepository.findByRegionIdWithOpenCourtStatus(regionId.toString()),
                                     regionId.toString(), NO_COURT_VENUES_FOUND_FOR_REGION_ID);
        } else if (!Objects.isNull(clusterId)) {
            log.info("{} : Obtaining court venues for cluster id: {}", loggingComponentName, clusterId);
            return getAllCourtVenues(() -> courtVenueRepository
                                         .findByClusterIdWithOpenCourtStatus(clusterId.toString()),
                                     clusterId.toString(), NO_COURT_VENUES_FOUND_FOR_CLUSTER_ID);
        } else {
            return getAllCourtVenues(() -> courtVenueRepository.findAllWithOpenCourtStatus(), null,
                                     NO_COURT_VENUES_FOUND);
        }
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

    private List<LrdCourtVenueResponse> getAllCourtVenues(
        Supplier<List<CourtVenue>> courtVenueSupplier,
        String id,
        String noDataFoundMessage) {
        List<CourtVenue> courtVenues = courtVenueSupplier.get();
        handleIfCourtVenuesEmpty(() -> isEmpty(courtVenues), noDataFoundMessage, id);
        return getCourtVenueListResponse(courtVenues);
    }

    private List<LrdCourtVenueResponse> getCourtVenueListResponse(
        List<CourtVenue> courtVenues) {
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
            log.error("{} : {}",loggingComponentName, noDataFoundMessage);
            throw new ResourceNotFoundException(noDataFoundMessage);
        }
    }

    public static void validateServiceCode(String serviceCode) {

        if (isBlank(serviceCode)) {
            throw new InvalidRequestException("No service code provided");
        }


        if (!Pattern.compile(ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE).matcher(serviceCode).matches()) {
            throw new InvalidRequestException(EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR);
        }
    }
}
