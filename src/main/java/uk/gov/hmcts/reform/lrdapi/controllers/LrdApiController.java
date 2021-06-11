package uk.gov.hmcts.reform.lrdapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;
import uk.gov.hmcts.reform.lrdapi.service.impl.FileUploadServiceImpl;
import uk.gov.hmcts.reform.lrdapi.util.ConstraintValidation;

import java.util.List;
import javax.validation.constraints.NotBlank;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.lib.util.ExcelAdapterConstants.REQUEST_COMPLETED_SUCCESSFULLY;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.BAD_REQUEST;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FILE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FORBIDDEN_ERROR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INTERNAL_SERVER_ERROR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.UNAUTHORIZED_ERROR;

@RequestMapping(
    path = "/refdata/location"
)
@RestController
@Slf4j
public class LrdApiController {

    @Autowired
    LrdService lrdService;

    @Autowired
    FileUploadServiceImpl fileUploadService;

    @Value("${loggingComponentName}")
    private String loggingComponentName;


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
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> retrieveOrgServiceDetails(
        @RequestParam(value = "serviceCode", required = false) String serviceCode,
        @RequestParam(value = "ccdCaseType", required = false) String ccdCaseType,
        @RequestParam(value = "ccdServiceNames", required = false) String ccdServiceNames) {
        log.info("Inside retrieveOrgServiceDetails");

        ConstraintValidation.validateInputParameters(serviceCode, ccdCaseType, ccdServiceNames);
        List<LrdOrgInfoServiceResponse> lrdOrgInfoServiceResponse = null;
        lrdOrgInfoServiceResponse = lrdService.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceNames);
        return ResponseEntity.status(200).body(lrdOrgInfoServiceResponse);
    }

    @ApiOperation(
        value = "This API allows the upload of excel files that contain building or court location information",
        notes = "**IDAM Roles to access API** :\n lrd-admin",
        authorizations = {
            @Authorization(value = "ServiceAuthorization"),
            @Authorization(value = "Authorization")
        }
    )
    @ApiResponses({
        @ApiResponse(
            code = 201,
            message = REQUEST_COMPLETED_SUCCESSFULLY
        ),
        @ApiResponse(
            code = 400,
            message = BAD_REQUEST
        ),
        @ApiResponse(
            code = 401,
            message = UNAUTHORIZED_ERROR
        ),
        @ApiResponse(
            code = 403,
            message = FORBIDDEN_ERROR
        ),
        @ApiResponse(
            code = 500,
            message = INTERNAL_SERVER_ERROR
        )
    })
    @PostMapping(value = "/{location-type}/upload-file",
                 consumes = "multipart/form-data")
    @Secured("lrd-admin")
    public ResponseEntity<Object> locationFileUpload(@RequestParam(FILE) MultipartFile file,
                                                     @PathVariable("location-type") @NotBlank String locationType) {

        ConstraintValidation.validateFileType(locationType);
        long time1 = currentTimeMillis();
        ResponseEntity<Object> responseEntity = fileUploadService.processFile(file, locationType);
        log.info("{}::Total Time taken to upload the given file is {}", loggingComponentName,
                 (currentTimeMillis() - time1));
        return responseEntity;
    }

}
