package uk.gov.hmcts.reform.lrdapi.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationUtilsTest {

    private String regexNumeric = "\\d+";

    @Test
    public void testInvalidEpimsIdInput_Alphanumeric() {
        assertThat(ValidationUtils.isStringInExpectedFormat("1QWERTY", regexNumeric))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testInvalidEpimsIdInput_Alphabetic() {
        assertThat(ValidationUtils.isStringInExpectedFormat("QWERTY", regexNumeric))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testInvalidEpimsIdInput_Space() {
        assertThat(ValidationUtils.isStringInExpectedFormat(" ", regexNumeric))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testInvalidEpimsIdInput_SpecialCharacters() {
        assertThat(ValidationUtils.isStringInExpectedFormat("!@$$£", regexNumeric))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testInvalidEpimsIdInput_NegativeIntegers() {
        assertThat(ValidationUtils.isStringInExpectedFormat("-1", regexNumeric))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testValidEpimsIdInput() {
        assertThat(ValidationUtils.isStringInExpectedFormat("1", regexNumeric))
            .isEqualTo(Boolean.TRUE);
    }

}
