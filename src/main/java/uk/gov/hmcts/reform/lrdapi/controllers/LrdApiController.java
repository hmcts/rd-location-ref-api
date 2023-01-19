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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationBySearchResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;
import uk.gov.hmcts.reform.lrdapi.service.RegionService;
import uk.gov.hmcts.reform.lrdapi.util.ValidationUtils;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ONLY_ONE_PARAM_REQUIRED_BUILDING_LOCATION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ONLY_ONE_PARAM_REQUIRED_REGION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_1;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_10;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_11;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_12;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_13;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_14;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_15;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_2;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_3;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_4;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_5;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_6;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_7;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_8;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.RET_LOC_NOTES_9;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfSingleValuePresent;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.validateSearchStringForBuildingLocationDetails;

@RequestMapping(
    path = "/refdata/location"
)
@RestController
@Slf4j
public class LrdApiController {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    LrdService lrdService;

    @Autowired
    ILrdBuildingLocationService buildingLocationService;

    @Autowired
    RegionService regionService;

    @Operation(
        summary = "This API will retrieve service code details association with ccd case type",
        description = "No roles required to access this API",
        security =
            {
                @SecurityRequirement(name = "Authorization"),
                @SecurityRequirement(name = "ServiceAuthorization")
            }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of Service Code or Ccd Case Type Details",
            content = @Content(schema = @Schema(implementation = LrdOrgInfoServiceResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Forbidden Error: Access denied",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No Service found with the given ID",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content
        )
    })
    @GetMapping(
        path = "/orgServices",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> retrieveOrgServiceDetails(
        @RequestParam(value = "serviceCode", required = false) String serviceCode,
        @RequestParam(value = "ccdCaseType", required = false) String ccdCaseType,
        @RequestParam(value = "ccdServiceNames", required = false) String ccdServiceNames) {
        log.info("{} : Inside retrieveOrgServiceDetails", loggingComponentName);
        ValidationUtils.validateInputParameters(serviceCode, ccdCaseType, ccdServiceNames);
        List<LrdOrgInfoServiceResponse> lrdOrgInfoServiceResponse;
        log.info("{} :Calling retrieveOrgServiceDetails", loggingComponentName);
        lrdOrgInfoServiceResponse = lrdService.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceNames);
        return ResponseEntity.status(200).body(lrdOrgInfoServiceResponse);
    }

    @Operation(
        summary = "This API will retrieve the Building Location details for the request param provided",
        description = RET_LOC_NOTES_1 + RET_LOC_NOTES_2 + RET_LOC_NOTES_3 + RET_LOC_NOTES_4 + RET_LOC_NOTES_5
            + RET_LOC_NOTES_6 + RET_LOC_NOTES_7 + RET_LOC_NOTES_8 + RET_LOC_NOTES_9 + RET_LOC_NOTES_10
            + RET_LOC_NOTES_11 + RET_LOC_NOTES_12 + RET_LOC_NOTES_13 + RET_LOC_NOTES_14 + RET_LOC_NOTES_15,
        security =
            {
                @SecurityRequirement(name = "Authorization"),
                @SecurityRequirement(name = "ServiceAuthorization")
            }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of Service Code or Ccd Case Type Details",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LrdBuildingLocationResponse.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Forbidden Error: Access denied",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No Service found with the given ID",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content
        )
    })
    @GetMapping(
        path = "/building-locations",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> retrieveBuildingLocationDetails(
        @RequestParam(value = "epimms_id", required = false) String epimsIds,
        @RequestParam(value = "building_location_name", required = false) String buildingLocationName,
        @RequestParam(value = "region_id", required = false) String regionId,
        @RequestParam(value = "cluster_id", required = false) String clusterId) {

        log.info("{} : Inside retrieveBuildingLocationDetails", loggingComponentName);
        checkIfSingleValuePresent(ONLY_ONE_PARAM_REQUIRED_BUILDING_LOCATION, epimsIds, buildingLocationName,
                                  regionId, clusterId
        );
        log.info("{} : Calling retrieveBuildingLocationDetails", loggingComponentName);
        Object responseEntity = buildingLocationService.retrieveBuildingLocationDetails(
            epimsIds,
            buildingLocationName,
            regionId,
            clusterId
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity);
    }

    @Operation(
        summary = "This API will retrieve Region details for the given Region description",
        description = "No roles required to access this API",
        security =
            {
                @SecurityRequirement(name = "Authorization"),
                @SecurityRequirement(name = "ServiceAuthorization")
            }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved a list of Region Details",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LrdRegionResponse.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Forbidden Error: Access denied",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No Region(s) found with the given Description(s) or ID(s)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content
        )
    })
    @GetMapping(
        path = "/regions",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> retrieveRegionDetails(
        @RequestParam(value = "region", required = false) String region,
        @RequestParam(value = "regionId", required = false) String regionId) {
        log.info("{} : Inside retrieveRegionDetails", loggingComponentName);
        checkIfSingleValuePresent(ONLY_ONE_PARAM_REQUIRED_REGION, region, regionId);
        log.info("{} : Calling retrieveRegionDetails", loggingComponentName);
        Object response = regionService.retrieveRegionDetails(regionId, region);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
        summary = "This end point will be used to search the open building locations based on the the "
            + "partial search string . When the consumers "
            + "inputs minimum 3 characters, they will call this api to fetch "
            + "the required result.",
        description = "No roles required to access this API",
        security =
            {
                @SecurityRequirement(name = "Authorization"),
                @SecurityRequirement(name = "ServiceAuthorization")
            }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the building location information for the given search string",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LrdBuildingLocationBySearchResponse.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Forbidden Error: Access denied",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content
        )
    })
    @GetMapping(
        path = "/building-locations/search",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> retrieveBuildingLocationDetailsBySearchString(
        @RequestParam(value = "search")
        @Parameter(name = "search", description = "Alphabets, Numeric And Special characters(&,/-()[]<space>') "
            + "only allowed and String should contain minimum three chars.", required = true) String searchString) {
        log.info("{} : Inside retrieveBuildingLocationDetailsBySearchString", loggingComponentName);
        String trimmedSearchString = searchString.strip();
        validateSearchStringForBuildingLocationDetails(trimmedSearchString);
        log.info("{} : Calling searchBuildingLocationsBySearchString", loggingComponentName);
        Object responseEntity = buildingLocationService.searchBuildingLocationsBySearchString(trimmedSearchString);
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity);
    }


}
