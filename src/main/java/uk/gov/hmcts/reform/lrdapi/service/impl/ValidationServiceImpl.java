package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.lib.audit.domain.RowDomain;
import uk.gov.hmcts.reform.lib.util.AuditStatus;
import uk.gov.hmcts.reform.lib.validator.service.IJsrValidatorInitializer;
import uk.gov.hmcts.reform.lib.validator.service.IValidationService;
import uk.gov.hmcts.reform.lrdapi.domain.LrdAudit;
import uk.gov.hmcts.reform.lrdapi.domain.LrdException;
import uk.gov.hmcts.reform.lrdapi.oidc.JwtGrantedAuthoritiesConverter;
import uk.gov.hmcts.reform.lrdapi.repository.LrdAuditRepository;
import uk.gov.hmcts.reform.lrdapi.repository.LrdExceptionRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static uk.gov.hmcts.reform.lib.util.RdCommonsUtil.getKeyField;
import static uk.gov.hmcts.reform.lib.util.RdCommonsUtil.getKeyFieldValue;

@Component
@Slf4j
public class ValidationServiceImpl implements IValidationService {

    @Autowired
    private IJsrValidatorInitializer<RowDomain> jsrValidatorInitializer;

    private LrdAudit lrdAudit;

    @Autowired
    LrdAuditRepository auditRepository;

    @Autowired
    LrdExceptionRepository lrdExceptionRepository;

    @Autowired
    @Lazy
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    private long auditJobId;

    @Value("${loggingComponentName}")
    private String loggingComponentName;


    /**
     * Returns invalid record list and JSR Constraint violations pair.
     *
     * @param lrdRecords List
     * @return CasWorkerDomain list
     */
    public List<RowDomain> getInvalidRecords(List<RowDomain> lrdRecords) {
        //Gets Invalid records
        return jsrValidatorInitializer.getInvalidJsrRecords(lrdRecords);
    }

    /**
     * Audit JSR exceptions.
     *
     * @param jobId long
     */

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void saveJsrExceptionsForJob(long jobId) {
        Set<ConstraintViolation<RowDomain>> constraintViolationSet = jsrValidatorInitializer.getConstraintViolations();
        List<LrdException> lrdExceptions = new LinkedList<>();
        AtomicReference<Field> field = new AtomicReference<>();
        //if JSR violation present then only persist exception
        ofNullable(constraintViolationSet).ifPresent(constraintViolations ->
                                                         constraintViolations.forEach(constraintViolation -> {
                                                             log.info(
                                                                 "{}:: Invalid JSR for row Id {} in job {} ",
                                                                 loggingComponentName,
                                                                 constraintViolation.getRootBean().getRowId(),
                                                                 jobId
                                                             );
                                                             if (isNull(field.get())) {
                                                                 field.set(getKeyField(
                                                                     constraintViolation.getRootBean()).get());
                                                                 ReflectionUtils.makeAccessible(field.get());
                                                             }
                                                             lrdExceptions.add(createLrdException(
                                                                 jobId,
                                                                 constraintViolation,
                                                                 field
                                                             ));
                                                         }));
        lrdExceptionRepository.saveAll(lrdExceptions);
    }

    public LrdException createLrdException(Long jobId, ConstraintViolation<RowDomain> constraintViolation,
                                           AtomicReference<Field> field) {
        LrdException lrdException = new LrdException();
        lrdException.setJobId(jobId);
        lrdException.setFieldInError(constraintViolation.getPropertyPath().toString());
        lrdException.setErrorDescription(constraintViolation.getMessage());
        lrdException.setExcelRowId(String.valueOf(constraintViolation.getRootBean().getRowId()));
        lrdException.setUpdatedTimeStamp(LocalDateTime.now());
        lrdException.setKeyField(getKeyFieldValue(field.get(), constraintViolation.getRootBean()));
        return lrdException;
    }

    /**
     * Inserts Audit details in Audit table.
     *
     * @param auditStatus AuditStatus
     * @param fileName    String
     * @return long id
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public long updateAuditStatus(final AuditStatus auditStatus, final String fileName) {
        createOrUpdateCaseworkerAudit(auditStatus, fileName);
        this.auditJobId = auditRepository.save(lrdAudit).getJobId();
        return auditJobId;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public long startAuditing(final AuditStatus auditStatus, final String fileName) {
        this.lrdAudit = LrdAudit.builder().build();
        createOrUpdateCaseworkerAudit(auditStatus, fileName);
        this.auditJobId = auditRepository.save(lrdAudit).getJobId();
        return auditJobId;
    }

    /**
     * Create ExceptionCaseWorker domain object.
     *
     * @param jobId   long
     * @param message String
     * @return ExceptionCaseWorker ExceptionCaseWorker
     */
    public LrdException createException(final long jobId, final String message, final Long rowId) {
        return LrdException.builder().jobId(jobId)
            .excelRowId((rowId != 0) ? rowId.toString() : EMPTY)
            .errorDescription(message).updatedTimeStamp(LocalDateTime.now()).build();
    }

    /**
     * Create/Updates CaseWorkerAudit domain object.
     *
     * @param auditStatus AuditStatus
     * @param fileName    String
     * @return CaseWorkerAudit CaseWorkerAudit
     */
    private LrdAudit createOrUpdateCaseworkerAudit(AuditStatus auditStatus, String fileName) {
        if (isNull(lrdAudit) || isNull(lrdAudit.getJobId())) {
            UserInfo userInfo = jwtGrantedAuthoritiesConverter.getUserInfo();
            String userId = (nonNull(userInfo) && nonNull(userInfo.getUid())) ? userInfo.getUid() : EMPTY;
            lrdAudit = LrdAudit.builder()
                .status(auditStatus.getStatus())
                .jobStartTime(LocalDateTime.now())
                .fileName(fileName)
                .authenticatedUserId(userId)
                .build();
        } else {
            lrdAudit.setStatus(auditStatus.getStatus());
            lrdAudit.setJobEndTime(LocalDateTime.now());
            lrdAudit.setJobId(getAuditJobId());
        }
        return lrdAudit;
    }


    /**
     * logging User profile failures.
     *
     * @param message String
     * @param rowId   long
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void logFailures(String message, long rowId) {
        log.info("{}:: Failure row Id {} with error {} in job {}  ", loggingComponentName, rowId, message,
                 getAuditJobId()
        );

        LrdException lrdException = createException(getAuditJobId(), message, rowId);
        lrdExceptionRepository.save(lrdException);
    }

    public long getAuditJobId() {
        return auditJobId;
    }
}

