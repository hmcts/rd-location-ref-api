package uk.gov.hmcts.reform.lrdapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ONLY_ONE_PARAM_REQUIRED_COURT_VENUE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_1;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_10;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_11;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_12;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_13;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_14;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_15;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_16;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_17;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_18;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_19;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_2;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_20;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_21;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_22;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_3;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_4;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_5;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_6;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_7;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_8;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_VEN_NOTES_9;
import static uk.gov.hmcts.reform.lrdapi.service.impl.CourtVenueServiceImpl.validateServiceCode;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfSingleValuePresent;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.trimCourtVenueRequestParam;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.validateCourtTypeId;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.validateCourtVenueFilters;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.validateSearchString;


@RequestMapping(
    path = "/refdata/location/court-venues"
)
@RestController
@Slf4j
public class LrdCourtVenueController {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    CourtVenueService courtVenueService;

    @Operation(
        summary = "This API will retrieve Court Venues for the request provided",
        description = RET_LOC_VEN_NOTES_1 + RET_LOC_VEN_NOTES_2 + RET_LOC_VEN_NOTES_3 + RET_LOC_VEN_NOTES_4
            + RET_LOC_VEN_NOTES_5 + RET_LOC_VEN_NOTES_6 + RET_LOC_VEN_NOTES_7 + RET_LOC_VEN_NOTES_8
            + RET_LOC_VEN_NOTES_9 + RET_LOC_VEN_NOTES_10 + RET_LOC_VEN_NOTES_11 + RET_LOC_VEN_NOTES_12
            + RET_LOC_VEN_NOTES_13 + RET_LOC_VEN_NOTES_14 + RET_LOC_VEN_NOTES_15 + RET_LOC_VEN_NOTES_16
            + RET_LOC_VEN_NOTES_17 + RET_LOC_VEN_NOTES_18 + RET_LOC_VEN_NOTES_19 + RET_LOC_VEN_NOTES_20
            + RET_LOC_VEN_NOTES_21 + RET_LOC_VEN_NOTES_22,
        security = {
            @SecurityRequirement(name = "ServiceAuthorization"),
            @SecurityRequirement(name = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of Court Venues for the request provided",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LrdCourtVenueResponse.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Forbidden Error: Access denied"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No Court Venues found for the request provided"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"
        )
    })
    @GetMapping(
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<LrdCourtVenueResponse>> retrieveCourtVenues(
        @RequestParam(value = "epimms_id", required = false) @NotBlank String epimmsIds,
        @RequestParam(value = "court_type_id", required = false) @NotNull Integer courtTypeId,
        @RequestParam(value = "region_id", required = false) @NotNull Integer regionId,
        @RequestParam(value = "cluster_id", required = false) @NotNull Integer clusterId,
        @RequestParam(value = "court_venue_name", required = false) @NotNull String courtVenueName,
        @RequestParam(value = "is_hearing_location", required = false) @NotNull String isHearingLocation,
        @RequestParam(value = "is_case_management_location", required = false) @NotNull String isCaseManagementLocation,
        @RequestParam(value = "location_type", required = false) @NotNull String locationType,
        @RequestParam(value = "is_temporary_location", required = false) @NotNull String isTemporaryLocation) {

        log.info("{} : Inside retrieveCourtVenues", loggingComponentName);
        checkIfSingleValuePresent(ONLY_ONE_PARAM_REQUIRED_COURT_VENUE, epimmsIds, String.valueOf(courtTypeId),
                                  String.valueOf(regionId), String.valueOf(clusterId), courtVenueName
        );
        CourtVenueRequestParam courtVenueRequestParam =
            new CourtVenueRequestParam();

        courtVenueRequestParam.setIsHearingLocation(isHearingLocation);
        courtVenueRequestParam.setIsCaseManagementLocation(isCaseManagementLocation);
        courtVenueRequestParam.setLocationType(locationType);
        courtVenueRequestParam.setIsTemporaryLocation(isTemporaryLocation);

        CourtVenueRequestParam result = trimCourtVenueRequestParam(courtVenueRequestParam);

        validateCourtVenueFilters(result);

        log.info("{} : Calling retrieveCourtVenues", loggingComponentName);
        var lrdCourtVenueResponses = courtVenueService.retrieveCourtVenueDetails(epimmsIds,
                                                                                 courtTypeId, regionId, clusterId,
                                                                                 courtVenueName,
                                                                                 result
        );
        return ResponseEntity.status(HttpStatus.OK).body(lrdCourtVenueResponses);
    }

    @Operation(
        summary = "This API will retrieve Court Venues for given Service Code",
        description = "No roles required to access this API",
        security = {
            @SecurityRequirement(name = "ServiceAuthorization"),
            @SecurityRequirement(name = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of Court Venues for given Service Code",
            content = @Content(schema = @Schema(implementation = LrdCourtVenuesByServiceCodeResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Forbidden Error: Access denied"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No Court Venues found with the given Service Code"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"
        )
    })
    @GetMapping(
        path = "/services",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> retrieveCourtVenuesByServiceCode(
        @RequestParam(value = "service_code") @NotBlank String serviceCode) {

        log.info("{} : Inside retrieveCourtVenuesByServiceCode", loggingComponentName);
        String trimmedServiceCode = serviceCode.strip();

        validateServiceCode(trimmedServiceCode);

        log.info("{} : Calling retrieveCourtVenuesByServiceCode", loggingComponentName);
        LrdCourtVenuesByServiceCodeResponse response = courtVenueService
            .retrieveCourtVenuesByServiceCode(trimmedServiceCode);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
        summary = "This endpoint will be used for Court Venues search based on partial query. When the consumers "
            + "inputs any 3 characters, they will call this api to fetch "
            + "the required result.",
        description = "No roles required to access this API",
        security = {
            @SecurityRequirement(name = "ServiceAuthorization"),
            @SecurityRequirement(name = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of Court Venues for the request provided",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LrdCourtVenueResponse.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Forbidden Error: Access denied"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"
        )
    })
    @GetMapping(
        path = "/venue-search",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<LrdCourtVenueResponse>> retrieveCourtVenuesBySearchString(
        @RequestParam(value = "search-string")
        @Parameter(name = "search-string",
            description = "Alphabets, Numeric And Special characters(_@.,’&-() ) "
                + "only allowed and String should contain minimum three chars.",
            required = true)
        String searchString,
        @RequestParam(value = "court-type-id", required = false)
        @Parameter(name = "court-type-id",
            description = "Alphabets and Numeric values only allowed in comma separated format")
        String courtTypeId,
        @RequestParam(value = "is_hearing_location", required = false)
        @Parameter(name = "is_hearing_location",
            description = "Allowed values are \"Y\" or \"N\"")
        String isHearingLocation,
        @RequestParam(value = "is_case_management_location", required = false)
        @Parameter(name = "is_case_management_location",
            description = "Allowed values are \"Y\" or \"N\"")
        String isCaseManagementLocation,
        @RequestParam(value = "location_type", required = false)
        @Parameter(name = "location_type",
            description = "allowed values are CTSC, NBC, Court,CCBC etc")
        String locationType,
        @RequestParam(value = "is_temporary_location", required = false)
        @Parameter(name = "is_temporary_location",
            description = "Allowed values are \"Y\" or \"N\"")
        String isTemporaryLocation
    ) {
        log.info("{} : Inside retrieveCourtVenuesBySearchString", loggingComponentName);
        String trimmedSearchString = searchString.strip();
        validateSearchString(trimmedSearchString);
        if (StringUtils.isNotBlank(courtTypeId)) {
            validateCourtTypeId(courtTypeId);
        }

        CourtVenueRequestParam requestParam = CourtVenueRequestParam
            .builder()
            .isHearingLocation(isHearingLocation)
            .isCaseManagementLocation(isCaseManagementLocation)
            .locationType(locationType)
            .isTemporaryLocation(isTemporaryLocation)
            .build();

        log.info("{} : Calling retrieveCourtVenuesBySearchString", loggingComponentName);
        var lrdCourtVenueResponses = courtVenueService.retrieveCourtVenuesBySearchString(
            trimmedSearchString, courtTypeId, requestParam);
        return ResponseEntity.status(HttpStatus.OK).body(lrdCourtVenueResponses);
    }
}
