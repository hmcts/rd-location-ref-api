package uk.gov.hmcts.reform.lrdapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    CourtVenueService courtVenueService;

    @ApiOperation(
        value = "This API will retrieve Court Venues for the request provided",
        notes = "No roles required to access this API.\n"
            + "For the request param 'epimms_id', either a single epimms_id or a list of epimms_ids separated by comas"
            + "can be passed. In any of these cases, a list of associated court venues would be returned.\n"
            + "Additionally, if 'ALL' is passed as the epimms_id value, then all the available court venues"
            + " associated with the available list of epimms_id are returned as a list.\n"
            + "For the request param 'court_type_id', then all the court venues that have the status as 'Open' "
            + "with the requested court_type_id are returned as a list.\n"
            + "For the request param 'region_id', the value needs to be a single region_id "
            + "for which all the associated court venues with the status as 'Open' would be returned as a list.\n"
            + "For the request param 'cluster_id', the value needs to be a single cluster_id "
            + "for which all the associated court venues with the status as 'Open' would be returned as a list.\n"
            + "For the request param 'court_venue_name', all the associated court venues that have the same site name "
            + "or court name irrespective of the case are returned as a list.\n"
            + "If no params are passed, then all the available court venues which have the "
            + "status as 'OPEN' are returned as a list."
            + "Additional API filters are applied with request params 'is_hearing_location', "
            + "'is_case_management_location'\n"
            + "'location_type' and 'is_temporary_location'.",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved list of Court Venues for the request provided",
            response = LrdCourtVenueResponse[].class
        ),
        @ApiResponse(
            code = 400,
            message = "Bad Request"
        ),
        @ApiResponse(
            code = 401,
            message = "Forbidden Error: Access denied"
        ),
        @ApiResponse(
            code = 404,
            message = "No Court Venues found for the request provided"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
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

        checkIfSingleValuePresent(epimmsIds, String.valueOf(courtTypeId), String.valueOf(regionId),
                                  String.valueOf(clusterId), courtVenueName);
        CourtVenueRequestParam courtVenueRequestParam =
            new CourtVenueRequestParam();

        courtVenueRequestParam.setIsHearingLocation(isHearingLocation);
        courtVenueRequestParam.setIsCaseManagementLocation(isCaseManagementLocation);
        courtVenueRequestParam.setLocationType(locationType);
        courtVenueRequestParam.setIsTemporaryLocation(isTemporaryLocation);

        CourtVenueRequestParam result =  trimCourtVenueRequestParam(courtVenueRequestParam);

        validateCourtVenueFilters(result);

        var lrdCourtVenueResponses = courtVenueService.retrieveCourtVenueDetails(epimmsIds,
                                                                                 courtTypeId, regionId, clusterId,
                                                                                 courtVenueName,
                                                                                 result);
        return ResponseEntity.status(HttpStatus.OK).body(lrdCourtVenueResponses);
    }

    @ApiOperation(
        value = "This API will retrieve Court Venues for given Service Code",
        notes = "No roles required to access this API",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved list of Court Venues for given Service Code",
            response = LrdCourtVenuesByServiceCodeResponse.class
        ),
        @ApiResponse(
            code = 400,
            message = "Bad Request"
        ),
        @ApiResponse(
            code = 401,
            message = "Forbidden Error: Access denied"
        ),
        @ApiResponse(
            code = 404,
            message = "No Court Venues found with the given Service Code"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
        )
    })
    @GetMapping(
        path = "/services",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> retrieveCourtVenuesByServiceCode(
        @RequestParam(value = "service_code", required = true) @NotBlank String serviceCode) {

        String trimmedServiceCode = serviceCode.strip();

        validateServiceCode(trimmedServiceCode);

        LrdCourtVenuesByServiceCodeResponse response = courtVenueService
            .retrieveCourtVenuesByServiceCode(trimmedServiceCode);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(
        value = "This endpoint will be used for Court Venues search based on partial query. When the consumers "
            + "inputs any 3 characters, they will call this api to fetch "
            + "the required result.",
        notes = "No roles required to access this API",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved list of Court Venues for the request provided",
            response = LrdCourtVenueResponse[].class
        ),
        @ApiResponse(
            code = 400,
            message = "Bad Request"
        ),
        @ApiResponse(
            code = 401,
            message = "Forbidden Error: Access denied"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
        )
    })
    @GetMapping(
        path = "/venue-search",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<LrdCourtVenueResponse>> retrieveCourtVenuesBySearchString(
        @RequestParam(value = "search-string", required = true)
        @ApiParam(name = "search-string",
            value = "Alphabets, Numeric And Special characters(_@.,’&-() ) "
                + "only allowed and String should contain minimum three chars.",
            required = true)
            String searchString,
        @RequestParam(value = "court-type-id", required = false)
        @ApiParam(name = "court-type-id",
            value = "Alphabets and Numeric values only allowed in comma separated format")
            String courtTypeId) {
        String trimmedSearchString = searchString.strip();
        validateSearchString(trimmedSearchString);
        if (StringUtils.isNotBlank(courtTypeId)) {
            validateCourtTypeId(courtTypeId);
        }
        var lrdCourtVenueResponses = courtVenueService.retrieveCourtVenuesBySearchString(
            trimmedSearchString,
            courtTypeId
        );
        return ResponseEntity.status(HttpStatus.OK).body(lrdCourtVenueResponses);
    }
}
