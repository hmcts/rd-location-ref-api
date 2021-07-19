package uk.gov.hmcts.reform.lrdapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.lrdapi.service.impl.CourtVenueServiceImpl.validateServiceCode;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfSingleValuePresent;

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
    public ResponseEntity<Object> retrieveCourtVenues(
        @RequestParam(value = "epimms_id", required = false) @NotBlank String epimmsIds,
        @RequestParam(value = "court_type_id", required = false) @NotNull Integer courtTypeId,
        @RequestParam(value = "region_id", required = false) @NotNull Integer regionId,
        @RequestParam(value = "cluster_id", required = false) @NotNull Integer clusterId) {
        //TODO
        checkIfSingleValuePresent(epimmsIds, String.valueOf(courtTypeId), String.valueOf(regionId),
                                  String.valueOf(clusterId));
        var lrdCourtVenueResponses = courtVenueService.retrieveCourtVenueDetails(epimmsIds,
                                                                                 courtTypeId, regionId, clusterId);
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
}
