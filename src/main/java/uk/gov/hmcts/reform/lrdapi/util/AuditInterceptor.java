package uk.gov.hmcts.reform.lrdapi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.hmcts.reform.lib.exception.ExcelValidationException;
import uk.gov.hmcts.reform.lib.validator.service.IValidationService;
import uk.gov.hmcts.reform.lrdapi.exception.ForbiddenException;
import uk.gov.hmcts.reform.lrdapi.oidc.JwtGrantedAuthoritiesConverter;
import uk.gov.hmcts.reform.lrdapi.repository.LrdAuditRepository;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.lib.util.AuditStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.lib.util.ExcelAdapterConstants.FILE_NO_DATA_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.ErrorConstants.FILE_UPLOAD_IN_PROGRESS;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FILE;

@MultipartConfig
@Component
@Slf4j
public class AuditInterceptor implements HandlerInterceptor {

    @Autowired
    IValidationService validationService;

    @Autowired
    LrdAuditRepository lrdAuditRepository;

    @Autowired
    @Lazy
    private JwtGrantedAuthoritiesConverter jwtConverter;

    @Value("${logging-component-name}")
    private String loggingComponentName;

    @Override

    @Secured("lrd-admin")
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response, @NotNull Object handler) {

        MultipartFile multipartFile = ((MultipartHttpServletRequest) request).getFile(FILE);
        if (nonNull(multipartFile)) {
            long audits = lrdAuditRepository.findByAuthenticatedUserIdAndStatus(
                jwtConverter.getUserInfo().getUid(), IN_PROGRESS.getStatus(),
                multipartFile.getOriginalFilename()
            );

            if (audits == 0) {
                //Starts Auditing with Job Status in Progress.
                long jobId = validationService.startAuditing(IN_PROGRESS, multipartFile.getOriginalFilename());
                log.info("{}:: Started File Upload with job {}", loggingComponentName, jobId);
            } else {
                throw new ForbiddenException(FILE_UPLOAD_IN_PROGRESS.getErrorMessage());
            }
        } else {
            throw new ExcelValidationException(HttpStatus.BAD_REQUEST, FILE_NO_DATA_ERROR_MESSAGE);
        }
        return true;
    }
}
