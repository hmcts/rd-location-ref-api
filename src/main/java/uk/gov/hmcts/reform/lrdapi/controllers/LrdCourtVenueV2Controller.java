package uk.gov.hmcts.reform.lrdapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EPPIMS_ID_WITH_COURT_TYPE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ONLY_ONE_PARAM_REQUIRED_COURT_VENUE;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkBothValuesPresent;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfMultipleValuePresentForVenue;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.trimCourtVenueRequestParam;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.validateCourtVenueFilters;

@RequestMapping(
    path = "/refdata/location/v2/court-venues"
)
@RestController
@Slf4j
public class LrdCourtVenueV2Controller {

    private static final String DEPRECATED_COURT_TYPE_ID = "Deprecated parameter, please use `service_code` instead.";

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
        @Parameter(name = "court_type_id", description = DEPRECATED_COURT_TYPE_ID, deprecated = true)
        @RequestParam(value = "court_type_id", required = false) Integer courtTypeId,
        @RequestParam(value = "region_id", required = false) Integer regionId,
        @RequestParam(value = "cluster_id", required = false) Integer clusterId,
        @RequestParam(value = "court_venue_name", required = false) String courtVenueName,
        @RequestParam(value = "is_hearing_location", required = false) String isHearingLocation,
        @RequestParam(value = "is_case_management_location", required = false) String isCaseManagementLocation,
        @RequestParam(value = "location_type", required = false) String locationType,
        @RequestParam(value = "is_temporary_location", required = false) String isTemporaryLocation) {

        log.info("{} : Inside retrieveCourtVenues V2", loggingComponentName);

        boolean epimmsIdWithCourtTypeOrServiceCodePresent = checkBothValuesPresent(epimmsIds,
                                                                                   String.valueOf(courtTypeId),
                                                                                   String.valueOf(serviceCode));

        if (epimmsIdWithCourtTypeOrServiceCodePresent) {
            checkIfMultipleValuePresentForVenue(ONLY_ONE_PARAM_REQUIRED_COURT_VENUE, EPPIMS_ID_WITH_COURT_TYPE,
                                                mrdVenueId, String.valueOf(regionId), String.valueOf(clusterId),
                                                courtVenueName);
        } else {
            String eitherServiceCodeOrCourtTypeId = StringUtils.isNotBlank(serviceCode)
                ? serviceCode : String.valueOf(courtTypeId);

            checkIfMultipleValuePresentForVenue(ONLY_ONE_PARAM_REQUIRED_COURT_VENUE, epimmsIds,
                                                mrdVenueId, eitherServiceCodeOrCourtTypeId,
                                                String.valueOf(regionId), String.valueOf(clusterId), courtVenueName);
        }

        CourtVenueRequestParam courtVenueRequestParam = new CourtVenueRequestParam();
        courtVenueRequestParam.setIsHearingLocation(isHearingLocation);
        courtVenueRequestParam.setIsCaseManagementLocation(isCaseManagementLocation);
        courtVenueRequestParam.setLocationType(locationType);
        courtVenueRequestParam.setIsTemporaryLocation(isTemporaryLocation);

        CourtVenueRequestParam result = trimCourtVenueRequestParam(courtVenueRequestParam);
        validateCourtVenueFilters(result);

        log.info("{} : Calling retrieveCourtVenues V2", loggingComponentName);
        var lrdCourtVenueResponses = courtVenueService.retrieveCourtVenueDetailsV2(
            epimmsIds,
            mrdVenueId,
            courtTypeId,
            serviceCode,
            regionId,
            clusterId,
            courtVenueName,
            epimmsIdWithCourtTypeOrServiceCodePresent,
            result
        );
        return ResponseEntity.status(HttpStatus.OK).body(lrdCourtVenueResponses);
    }
}
