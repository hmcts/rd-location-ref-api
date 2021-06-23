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
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;
import uk.gov.hmcts.reform.lrdapi.service.RegionService;
import uk.gov.hmcts.reform.lrdapi.util.ValidationUtils;

import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
    path = "/refdata/location"
)
@RestController
@Slf4j
public class LrdApiController {

    private static final String AlphaNumericRegex = "[0-9a-zA-Z_]+";

    private static final String EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED = "Bad Request - "
        + "Invalid epims id(s): %s  passed.";

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
        log.info("Inside retrieveOrgServiceDetails");
        ValidationUtils.validateInputParameters(serviceCode, ccdCaseType, ccdServiceNames);
        List<LrdOrgInfoServiceResponse> lrdOrgInfoServiceResponse = null;
        lrdOrgInfoServiceResponse = lrdService.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceNames);
        return ResponseEntity.status(200).body(lrdOrgInfoServiceResponse);
    }

    @ApiOperation(
        value = "This API will retrieve a Building Location details for the provided list of epims IDs. "
            + "The list of ids are passed as comma separated values. "
            + "If no epims id is passed, this endpoint returns all the available building locations.",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved the building Location details",
            response = LrdOrgInfoServiceResponse[].class,
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
    public ResponseEntity<Object> retrieveBuildingLocationDetailsByEpimsId(
        @RequestParam(value = "epimms_id", required = false) String epimsIds) {

        log.info("{} : Obtaining building locations for epim id(s): {}", loggingComponentName, epimsIds);
        if (isEmpty(epimsIds) || epimsIds.strip().equalsIgnoreCase(LocationRefConstants.ALL)) {
            return getAllBuildingLocations();
        }
        List<String> epimsIdList = ValidationUtils
            .checkIfValidCsvIdentifiersAndReturnList(epimsIds, EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED);
        if (ValidationUtils.isListContainsTextIgnoreCase(epimsIdList, LocationRefConstants.ALL)) {
            return getAllBuildingLocations();
        }
        ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList(epimsIdList,
                                                                      AlphaNumericRegex, log,
                                                                      loggingComponentName,
                                                                      EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );
        List<LrdBuildingLocationResponse> response =
            buildingLocationService.retrieveBuildingLocationByEpimsIds(epimsIdList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(
        value = "This API will retrieve Region details for the given Region description",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved list of Service Code or Ccd Case Type Details",
            response = LrdRegionResponse.class
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
        path = "/region",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> retriveRegionDetails(
        @RequestParam(value = "region", required = false) String region) {

        if (isBlank(region)) {
            throw new InvalidRequestException("No description provided");
        }

        LrdRegionResponse response = regionService.retrieveRegionByRegionDescription(region.strip());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    private ResponseEntity<Object> getAllBuildingLocations() {
        List<LrdBuildingLocationResponse> response =
            buildingLocationService.getAllBuildingLocations();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
