package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.lib.audit.domain.CommonFileUploadResponse;
import uk.gov.hmcts.reform.lib.audit.domain.RowDomain;
import uk.gov.hmcts.reform.lib.excel.adapter.service.ExcelAdapterService;
import uk.gov.hmcts.reform.lib.excel.adapter.service.ExcelValidatorService;
import uk.gov.hmcts.reform.lib.util.AuditStatus;
import uk.gov.hmcts.reform.lib.validator.service.IValidationService;
import uk.gov.hmcts.reform.lrdapi.client.domain.Building;
import uk.gov.hmcts.reform.lrdapi.client.domain.Court;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ExceptionMapper;
import uk.gov.hmcts.reform.lrdapi.domain.LrdException;
import uk.gov.hmcts.reform.lrdapi.repository.LrdExceptionRepository;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;

import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.nonNull;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;
import static uk.gov.hmcts.reform.lib.util.AuditStatus.FAILURE;
import static uk.gov.hmcts.reform.lib.util.AuditStatus.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.lib.util.AuditStatus.SUCCESS;
import static uk.gov.hmcts.reform.lib.util.FileUploadResponseUtil.createResponse;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.BUILDING_LOCATION_PARAM_NAME;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.BUILDING_LOCATION_SHEET_NAME;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COURT_LOCATION_SHEET_NAME;

@Service
@Slf4j
public class FileUploadServiceImpl {

    @Value("${excel.acceptableBuildingHeaders}")
    private List<String> acceptableBuildingHeaders;

    @Value("${excel.acceptableCourtLocationHeaders}")
    private List<String> acceptableCourtLocationHeaders;

    @Autowired
    ExcelValidatorService excelValidatorService;

    @Autowired
    IValidationService validationService;

    @Autowired
    ExcelAdapterService excelAdaptorService;

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    LrdExceptionRepository exceptionRepository;

    @Autowired
    LrdService lrdService;

    @SuppressWarnings("unchecked")
    public ResponseEntity<Object> processFile(MultipartFile file, String locationType) {

        AuditStatus status = SUCCESS;

        try {
            long jobId = validationService.getAuditJobId();
            boolean isBuildingLocation = BUILDING_LOCATION_PARAM_NAME.equals(locationType);
            Class<? extends RowDomain> ob = isBuildingLocation ? Building.class : Court.class;
            List<String> acceptableHeaders = isBuildingLocation
                ? acceptableBuildingHeaders : acceptableCourtLocationHeaders;
            long time2 = System.currentTimeMillis();
            List<RowDomain> lrdRecords = (List<RowDomain>) excelAdaptorService.parseExcel(
                excelValidatorService.validateExcelFile(file), isBuildingLocation
                    ? BUILDING_LOCATION_SHEET_NAME : COURT_LOCATION_SHEET_NAME, acceptableHeaders, ob);

            log.info("{}::Time taken to parse the given file {} is {}",
                     loggingComponentName, file.getOriginalFilename(), (currentTimeMillis() - time2));

            long time3 = currentTimeMillis();
            List<RowDomain> invalidRecords = validationService.getInvalidRecords(lrdRecords);
            log.info("{}::Time taken to validate the records is {}", loggingComponentName,
                     (currentTimeMillis() - time3));

            int totalRecords = isNotEmpty(lrdRecords) ? lrdRecords.size() : 0;

            if (isNotEmpty(invalidRecords)) {
                lrdRecords.removeAll(invalidRecords);
                //audit exceptions or invalid records
                status = PARTIAL_SUCCESS;
                //Inserts JSR exceptions
                validationService.saveJsrExceptionsForJob(jobId);
            }
            if (isNotEmpty(lrdRecords)) {
                log.info("persisting LRD records");
                lrdService.createLocations(lrdRecords, isBuildingLocation);
            }

            return sendResponse(file, status, totalRecords);

        } catch (HttpServerErrorException ex) {
            auditLog(file, ex);
            return new ExceptionMapper().errorDetailsResponseEntity(ex, ex.getStatusCode(), ex.getMessage());
        } catch (Exception ex) {
            auditLog(file, ex);
            throw ex;
        }
    }

    private void auditLog(MultipartFile file, Exception ex) {
        long jobId = validationService.updateAuditStatus(FAILURE, file.getOriginalFilename());
        log.error("{}:: Failed File Upload for job {}", loggingComponentName, jobId);
        validationService.logFailures(ex.getMessage(), 0L);
    }

    public ResponseEntity<Object> sendResponse(MultipartFile file, AuditStatus status, int totalRecords) {
        List<LrdException> exceptionRecordsList = exceptionRepository.findByJobId(validationService.getAuditJobId());
        CommonFileUploadResponse lrdFileUploadResponse = createResponse(totalRecords, exceptionRecordsList);
        status = (nonNull(exceptionRecordsList) && (exceptionRecordsList.size()) > 0) ? PARTIAL_SUCCESS : status;
        long jobId = validationService.updateAuditStatus(status, file.getOriginalFilename());
        log.info("{}:: Completed File Upload for Job {} with status {}", loggingComponentName, jobId, status);
        return ResponseEntity.ok().body(lrdFileUploadResponse);
    }
}
