package uk.gov.hmcts.reform.lrdapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.lrdapi.service.impl.CourtVenueServiceImpl.validateServiceCode;

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
        summary = "This API will retrieve V2 Court Venues for given Service Code",
        description = "No roles required to access this API",
        security = {
            @SecurityRequirement(name = "ServiceAuthorization"),
            @SecurityRequirement(name = "Authorization")
        }
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved list of V2 Court Venues for given Service Code",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = LrdCourtVenueV2Response.class)))
    )
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @ApiResponse(responseCode = "401", description = "Forbidden Error: Access denied", content = @Content)
    @ApiResponse(responseCode = "404", description = "No Court Venues found with the given Service Code",
        content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    @GetMapping(
        path = "/services",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<LrdCourtVenueV2Response>> retrieveCourtVenuesByServiceCodeV2(
        @RequestParam(value = "service_code") @NotBlank String serviceCode) {

        log.info("{} : Inside retrieveCourtVenuesByServiceCodeV2", loggingComponentName);
        String trimmedServiceCode = validateServiceCode(serviceCode);

        log.info("{} : Calling retrieveCourtVenuesByServiceCodeV2", loggingComponentName);
        List<LrdCourtVenueV2Response> response = courtVenueService
            .retrieveCourtVenuesByServiceCodeV2(trimmedServiceCode);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
