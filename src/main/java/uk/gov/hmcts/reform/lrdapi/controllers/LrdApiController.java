package uk.gov.hmcts.reform.lrdapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @ApiOperation(
        value = "This API will retrieve service code details association with ccd case type",
        notes = "No roles required to access this API",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved list of Service Code or Ccd Case Type Details",
            response = LrdOrgInfoServiceResponse.class,
            responseContainer = "list"
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
            message = "No Service found with the given ID"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
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
        log.info("{} : Inside retrieveOrgServiceDetails",loggingComponentName);
        ValidationUtils.validateInputParameters(serviceCode, ccdCaseType, ccdServiceNames);
        List<LrdOrgInfoServiceResponse> lrdOrgInfoServiceResponse;
        log.info("{} :Calling retrieveOrgServiceDetails",loggingComponentName);
        lrdOrgInfoServiceResponse = lrdService.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceNames);
        return ResponseEntity.status(200).body(lrdOrgInfoServiceResponse);
    }

    @ApiOperation(
        value = "This API will retrieve the Building Location details for the request param provided",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        },
        notes = RET_LOC_NOTES_1 + RET_LOC_NOTES_2 + RET_LOC_NOTES_3 + RET_LOC_NOTES_4 + RET_LOC_NOTES_5
            + RET_LOC_NOTES_6 + RET_LOC_NOTES_7 + RET_LOC_NOTES_8 + RET_LOC_NOTES_9 + RET_LOC_NOTES_10
            + RET_LOC_NOTES_11 + RET_LOC_NOTES_12 + RET_LOC_NOTES_13 + RET_LOC_NOTES_14 + RET_LOC_NOTES_15
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved the building Location details",
            response = LrdBuildingLocationResponse[].class,
            responseContainer = "list"
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
            message = "No Building Location found with the given ID"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
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

        log.info("{} : Inside retrieveBuildingLocationDetails",loggingComponentName);
        checkIfSingleValuePresent(ONLY_ONE_PARAM_REQUIRED_BUILDING_LOCATION,epimsIds, buildingLocationName,
                regionId, clusterId);
        log.info("{} : Calling retrieveBuildingLocationDetails",loggingComponentName);
        Object responseEntity = buildingLocationService.retrieveBuildingLocationDetails(epimsIds,
                                                                                        buildingLocationName,
                                                                                        regionId,
                                                                                        clusterId);
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity);
    }

    @ApiOperation(
        value = "This API will retrieve Region details for the given Region description",
        notes = "No roles required to access this API",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved a list of Region Details",
            response = LrdRegionResponse[].class
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
            message = "No Region(s) found with the given Description(s) or ID(s)"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
        )
    })
    @GetMapping(
        path = "/regions",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> retrieveRegionDetails(
        @RequestParam(value = "region", required = false) String region,
        @RequestParam(value = "regionId", required = false) String regionId) {
        log.info("{} : Inside retrieveRegionDetails",loggingComponentName);
        checkIfSingleValuePresent(ONLY_ONE_PARAM_REQUIRED_REGION,region, regionId);
        log.info("{} : Calling retrieveRegionDetails",loggingComponentName);
        Object response = regionService.retrieveRegionDetails(regionId, region);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
