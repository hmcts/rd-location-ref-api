package uk.gov.hmcts.reform.lrdapi.controllers.advice;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LrdApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String errorMessage;

    public LrdApiException(HttpStatus httpStatus, String errorMessage) {
        super(errorMessage);
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
