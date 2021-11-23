package uk.gov.hmcts.reform.lrdapi.controllers.advice;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class LrdApiExceptionTest {

    @Test
    public void testLrdApiException() {
        LrdApiException externalApiException = new LrdApiException(BAD_REQUEST, "BAD REQUEST");
        assertNotNull(externalApiException);
        assertThat(externalApiException.getHttpStatus()).hasToString("400 BAD_REQUEST");
        assertEquals("BAD REQUEST", externalApiException.getErrorMessage());
    }
}
