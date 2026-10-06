package uk.gov.hmcts.reform.lrdapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EPPIMS_ID_WITH_COURT_TYPE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_INVALID_SERVICE_CODE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_COURT_VENUE_NAME_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_NO_VALID_MRD_VENUE_ID_PASSED;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INVALID_CLUSTER_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INVALID_COURT_TYPE_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INVALID_REGION_ID;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ONLY_ONE_PARAM_REQUIRED_COURT_VENUE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REGEX_FOR_BUILDING_LOCATION_SEARCH;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkBothValuesPresent;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfMultipleValuePresentForVenue;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfValidCsvIdentifiersAndReturnList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.findInvalidIdentifiers;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isRegexSatisfied;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.trimCourtVenueRequestParam;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.validateCourtVenueFilters;

@RequestMapping(
    path = "/refdata/location/v2/court-venues"
)
@RestController
@Slf4j
public class LrdCourtVenueV2Controller {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    CourtVenueService courtVenueService;

    @Operation(
        summary = "This API will retrieve Court Venues in the V2 response format",
        description = "No roles required to access this API",
        security = {
            @SecurityRequirement(name = "ServiceAuthorization"),
            @SecurityRequirement(name = "Authorization")
        }
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved list of Court Venues for the request provided",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = LrdCourtVenueV2Response.class)))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content
    )
    @ApiResponse(
        responseCode = "401",
        description = "Forbidden Error: Access denied",
        content = @Content
    )
    @ApiResponse(
        responseCode = "404",
        description = "No Court Venues found for the request provided",
        content = @Content
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content
    )
    @GetMapping(
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<LrdCourtVenueV2Response>> retrieveCourtVenues(
        @RequestParam(value = "epimms_id", required = false) String epimmsIds,
        @RequestParam(value = "mrd_venue_id", required = false) String mrdVenueId,
        @RequestParam(value = "service_code", required = false) String serviceCode,
        @RequestParam(value = "court_type_id", required = false) Integer courtTypeId,
        @RequestParam(value = "region_id", required = false) Integer regionId,
        @RequestParam(value = "cluster_id", required = false) Integer clusterId,
        @RequestParam(value = "court_venue_name", required = false) String courtVenueName,
        @RequestParam(value = "is_hearing_location", required = false) String isHearingLocation,
        @RequestParam(value = "is_case_management_location", required = false) String isCaseManagementLocation,
        @RequestParam(value = "location_type", required = false) String locationType,
        @RequestParam(value = "is_temporary_location", required = false) String isTemporaryLocation,
        @RequestParam(value = "mrd_building_id", required = false) String mrdBuildingId,
        @RequestParam(value = "court_status", required = false) String courtStatus) {

        log.info("{} : Inside retrieveCourtVenues V2", loggingComponentName);

        String trimmedEpimmsIds = StringUtils.stripToNull(epimmsIds);
        String trimmedMrdVenueId = mrdVenueId == null ? null : StringUtils.strip(mrdVenueId);
        String trimmedServiceCode = StringUtils.isBlank(serviceCode) ? serviceCode : StringUtils.strip(serviceCode);
        String trimmedCourtVenueName = StringUtils.isBlank(courtVenueName)
            ? courtVenueName : StringUtils.strip(courtVenueName);

        validateRequestParameters(
            trimmedEpimmsIds,
            trimmedMrdVenueId,
            trimmedServiceCode,
            courtTypeId,
            regionId,
            clusterId,
            trimmedCourtVenueName
        );

        boolean epimmsIdWithCourtTypeOrServiceCodePresent = checkBothValuesPresent(trimmedEpimmsIds,
                                                                                   String.valueOf(courtTypeId),
                                                                                   String.valueOf(trimmedServiceCode));

        if (epimmsIdWithCourtTypeOrServiceCodePresent) {
            checkIfMultipleValuePresentForVenue(ONLY_ONE_PARAM_REQUIRED_COURT_VENUE, EPPIMS_ID_WITH_COURT_TYPE,
                                                trimmedMrdVenueId, String.valueOf(regionId), String.valueOf(clusterId),
                                                trimmedCourtVenueName);
        } else {
            String eitherServiceCodeOrCourtTypeId = StringUtils.isNotBlank(trimmedServiceCode)
                ? trimmedServiceCode : String.valueOf(courtTypeId);

            checkIfMultipleValuePresentForVenue(ONLY_ONE_PARAM_REQUIRED_COURT_VENUE, trimmedEpimmsIds,
                                                trimmedMrdVenueId, eitherServiceCodeOrCourtTypeId,
                                                String.valueOf(regionId), String.valueOf(clusterId),
                                                trimmedCourtVenueName);
        }

        CourtVenueRequestParam courtVenueRequestParam = new CourtVenueRequestParam();
        courtVenueRequestParam.setIsHearingLocation(isHearingLocation);
        courtVenueRequestParam.setIsCaseManagementLocation(isCaseManagementLocation);
        courtVenueRequestParam.setLocationType(locationType);
        courtVenueRequestParam.setIsTemporaryLocation(isTemporaryLocation);
        courtVenueRequestParam.setMrdBuildingId(mrdBuildingId);
        courtVenueRequestParam.setCourtStatus(courtStatus);

        CourtVenueRequestParam result = trimCourtVenueRequestParam(courtVenueRequestParam);
        validateCourtVenueFilters(result);

        log.info("{} : Calling retrieveCourtVenues V2", loggingComponentName);
        var lrdCourtVenueResponses = courtVenueService.retrieveCourtVenueDetailsV2(
            trimmedEpimmsIds,
            trimmedMrdVenueId,
            courtTypeId,
            trimmedServiceCode,
            regionId,
            clusterId,
            trimmedCourtVenueName,
            epimmsIdWithCourtTypeOrServiceCodePresent,
            result
        );
        return ResponseEntity.status(HttpStatus.OK).body(lrdCourtVenueResponses);
    }

    private void validateRequestParameters(String epimmsIds,
                                           String mrdVenueId,
                                           String serviceCode,
                                           Integer courtTypeId,
                                           Integer regionId,
                                           Integer clusterId,
        String courtVenueName) {
        validateEpimmsIds(epimmsIds);
        validateProvidedSingleValue(mrdVenueId, ALPHA_NUMERIC_REGEX, EXCEPTION_MSG_NO_VALID_MRD_VENUE_ID_PASSED);
        validateSingleValue(serviceCode, ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE, EXCEPTION_MSG_INVALID_SERVICE_CODE);
        validatePositiveNumber(courtTypeId, INVALID_COURT_TYPE_ID);
        validatePositiveNumber(regionId, INVALID_REGION_ID);
        validatePositiveNumber(clusterId, INVALID_CLUSTER_ID);
        validateSingleValue(courtVenueName, REGEX_FOR_BUILDING_LOCATION_SEARCH,
                            EXCEPTION_MSG_NO_VALID_COURT_VENUE_NAME_PASSED);
    }

    private void validateEpimmsIds(String epimmsIds) {
        if (StringUtils.isBlank(epimmsIds)) {
            return;
        }

        if ("ALL".equalsIgnoreCase(epimmsIds)) {
            return;
        }

        List<String> epimmsIdList = checkIfValidCsvIdentifiersAndReturnList(
            epimmsIds,
            EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );
        List<String> invalidEpimmsIds = findInvalidIdentifiers(
            epimmsIdList,
            ALPHA_NUMERIC_REGEX
        );
        if (!invalidEpimmsIds.isEmpty()) {
            throw new InvalidRequestException(String.format(EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED, invalidEpimmsIds));
        }
    }

    private void validateSingleValue(String value, String regex, String exceptionMessage) {
        if (StringUtils.isNotBlank(value) && !isRegexSatisfied(value, regex)) {
            throw new InvalidRequestException(String.format(exceptionMessage, value));
        }
    }

    private void validateProvidedSingleValue(String value, String regex, String exceptionMessage) {
        if (value != null && (StringUtils.isBlank(value) || !isRegexSatisfied(value, regex))) {
            throw new InvalidRequestException(String.format(exceptionMessage, value));
        }
    }

    private void validatePositiveNumber(Integer value, String exceptionMessage) {
        if (value != null && value < 1) {
            throw new InvalidRequestException(String.format(exceptionMessage, value));
        }
    }
}
