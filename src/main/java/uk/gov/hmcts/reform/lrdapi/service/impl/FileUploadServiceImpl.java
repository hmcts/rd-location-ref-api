package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.lib.audit.domain.RowDomain;
import uk.gov.hmcts.reform.lib.excel.adapter.service.ExcelAdapterService;
import uk.gov.hmcts.reform.lib.excel.adapter.service.ExcelValidatorService;
import uk.gov.hmcts.reform.lib.util.AuditStatus;
import uk.gov.hmcts.reform.lib.validator.service.IValidationService;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ExceptionMapper;
import uk.gov.hmcts.reform.lrdapi.domain.CaseWorkerProfile;
import uk.gov.hmcts.reform.lrdapi.domain.JsrFileErrors;
import uk.gov.hmcts.reform.lrdapi.domain.LrdException;
import uk.gov.hmcts.reform.lrdapi.domain.LrdFileUploadResponse;
import uk.gov.hmcts.reform.lrdapi.oidc.JwtGrantedAuthoritiesConverter;
import uk.gov.hmcts.reform.lrdapi.repository.LrdExceptionRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static com.microsoft.applicationinsights.web.dependencies.apachecommons.lang3.StringUtils.SPACE;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;
import static uk.gov.hmcts.reform.lib.util.AuditStatus.FAILURE;
import static uk.gov.hmcts.reform.lib.util.AuditStatus.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.lib.util.AuditStatus.SUCCESS;
import static uk.gov.hmcts.reform.lib.util.ExcelAdapterConstants.RECORDS_FAILED;
import static uk.gov.hmcts.reform.lib.util.ExcelAdapterConstants.RECORDS_UPLOADED;
import static uk.gov.hmcts.reform.lib.util.ExcelAdapterConstants.REQUEST_FAILED_FILE_UPLOAD_JSR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REQUEST_COMPLETED_SUCCESSFULLY;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REQUIRED_CW_SHEET_NAME;

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
    @Lazy
    private JwtGrantedAuthoritiesConverter jwtConverter;

    @SuppressWarnings("unchecked")
    public ResponseEntity<Object> processFile(MultipartFile file) {

        AuditStatus status = SUCCESS;

        try {
            long jobId = validationService.getAuditJobId();
            Class<? extends RowDomain> ob = CaseWorkerProfile.class;
            long time2 = System.currentTimeMillis();
            List<RowDomain> lrdRecords = (List<RowDomain>) excelAdaptorService.parseExcel(
                excelValidatorService.validateExcelFile(file), REQUIRED_CW_SHEET_NAME, acceptableBuildingHeaders, ob);

            log.info("{}::Time taken to parse the given file {} is {}",
                     loggingComponentName, file.getOriginalFilename(), (System.currentTimeMillis() - time2));

            long time3 = System.currentTimeMillis();
            List<RowDomain> invalidRecords = validationService.getInvalidRecords(lrdRecords);
            log.info("{}::Time taken to validate the records is {}", loggingComponentName,
                     (System.currentTimeMillis() - time3));

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
                //TODO: call dao service to persist records from excel
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
        LrdFileUploadResponse lrdFileUploadResponse = createResponse(totalRecords, exceptionRecordsList);
        status = (nonNull(exceptionRecordsList) && (exceptionRecordsList.size()) > 0)
            ? PARTIAL_SUCCESS : status;
        long jobId = validationService.updateAuditStatus(status, file.getOriginalFilename());
        log.info("{}:: Completed File Upload for Job {} with status {}", loggingComponentName, jobId, status);
        return ResponseEntity.ok().body(lrdFileUploadResponse);
    }

    /**
     * create LrdFileUploadResponse.
     *
     * @return LrdFileUploadResponse lrdFileUploadResponse
     */
    private LrdFileUploadResponse createResponse(int totalRecords, List<LrdException> exceptionLrdList) {
        var lrdFileUploadResponseBuilder = LrdFileUploadResponse.builder();

        if (isNotEmpty(exceptionLrdList)) {

            Map<String, List<LrdException>> failedRecords = exceptionLrdList.stream()
                .collect(groupingBy(LrdException::getExcelRowId));

            LinkedList<JsrFileErrors> jsrFileErrors = new LinkedList<>();

            failedRecords.entrySet().stream()
                .sorted(Comparator.comparingInt(s -> Integer.valueOf(s.getKey())))
                .forEachOrdered(map ->
                                    map.getValue().forEach(jsrInvalid ->
                                                               jsrFileErrors.add(JsrFileErrors.builder()
                                                                                     .rowId(jsrInvalid.getExcelRowId())
                                                                                     .errorDescription(
                                                                                         jsrInvalid
                                                                                              .getErrorDescription())
                                                                                     .filedInError(
                                                                                         jsrInvalid.getFieldInError())
                                                                                     .build())));

            String detailedMessage = constructDetailedMessage(totalRecords, failedRecords);
            return lrdFileUploadResponseBuilder.message(REQUEST_FAILED_FILE_UPLOAD_JSR)
                .detailedMessage(detailedMessage).errorDetails(jsrFileErrors).build();
        } else {
            StringJoiner detailedMessage = new StringJoiner(SPACE + "and" + SPACE);
            //get the uploaded records excluding suspended records
            int noOfUploadedRecords = totalRecords;

            if (noOfUploadedRecords > 0) {
                detailedMessage.add(format(RECORDS_UPLOADED, noOfUploadedRecords));
            }

            return lrdFileUploadResponseBuilder
                .message(REQUEST_COMPLETED_SUCCESSFULLY)
                .detailedMessage(detailedMessage.toString()).build();
        }
    }

    private String constructDetailedMessage(int totalRecords, Map<String, List<LrdException>> failedRecords) {
        String detailedMessage = format(RECORDS_FAILED, failedRecords.size());
        //get the uploaded records excluding failed records
        int uploadedRecords = totalRecords - failedRecords.size();
        if (uploadedRecords > 0) {
            detailedMessage = format(RECORDS_FAILED, failedRecords.size()) + SPACE + "and" + SPACE
                .concat(format(RECORDS_UPLOADED, uploadedRecords));
        }
        return detailedMessage;
    }
}
