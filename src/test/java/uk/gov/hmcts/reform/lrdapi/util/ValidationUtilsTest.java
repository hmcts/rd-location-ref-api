package uk.gov.hmcts.reform.lrdapi.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationUtilsTest {

    private String REGEX_NUMERIC_ONLY="\\d+";

    @Test
    public void testInvalidEPIMSIDInput_Alphanumeric() {
        assertThat(ValidationUtils.isStringInExpectedFormat("1QWERTY", REGEX_NUMERIC_ONLY))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testInvalidEPIMSIDInput_Alphabetic() {
        assertThat(ValidationUtils.isStringInExpectedFormat("QWERTY", REGEX_NUMERIC_ONLY))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testInvalidEPIMSIDInput_Space() {
        assertThat(ValidationUtils.isStringInExpectedFormat(" ", REGEX_NUMERIC_ONLY))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testInvalidEPIMSIDInput_SpecialCharacters() {
        assertThat(ValidationUtils.isStringInExpectedFormat("!@$$£", REGEX_NUMERIC_ONLY))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testInvalidEPIMSIDInput_NegativeIntegers() {
        assertThat(ValidationUtils.isStringInExpectedFormat("-1", REGEX_NUMERIC_ONLY))
            .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void testValidEPIMSIDInput() {
        assertThat(ValidationUtils.isStringInExpectedFormat("1", REGEX_NUMERIC_ONLY))
            .isEqualTo(Boolean.TRUE);
    }

}
