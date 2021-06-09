package uk.gov.hmcts.reform.lrdapi.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationUtilsTest {

    private static final String AlphaNumericRegex = "[0-9a-zA-Z_]+";

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_InvalidInputSpecialChars() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add("!@£$");
        assertThat(ValidationUtils.findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(identifiers);
    }

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_ValidInputChars() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add("QWERTY");
        assertThat(ValidationUtils.findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isEqualTo(0);
    }

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_ValidInputNumeric() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add("1234");
        assertThat(ValidationUtils.findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isEqualTo(0);
    }

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_ValidInputAlphaNumeric() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add("qwerty1234");
        assertThat(ValidationUtils.findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isEqualTo(0);
    }

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_ValidInputAlphaNumericUnderscore() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add("qwerty_1234");
        assertThat(ValidationUtils.findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isEqualTo(0);
    }

    @Test
    public void testFindInvalidIdentifiers_MultipleIdsGiven_OneInvalidIdSpecialChars() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add("1234");
        identifiers.addAll(getSingleInvalidIdList());
        assertThat(ValidationUtils.findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(getSingleInvalidIdList());
    }

    @Test
    public void testFindInvalidIdentifiers_MultipleIdsGiven_SomeInValidInputSpecialChars() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add("QWERTY");
        identifiers.addAll(getMultipleInvalidIdList());
        assertThat(ValidationUtils.findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(getMultipleInvalidIdList());
    }

    @Test
    public void testFindInvalidIdentifiers_MultipleIdsGiven_AllInvalidIdsSpecialChars() {
        List<String> identifiers = new ArrayList<>();
        identifiers.addAll(getMultipleInvalidIdList());
        assertThat(ValidationUtils.findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(getMultipleInvalidIdList());
    }

    @Test
    public void testIsListContainsText_NoAllProvided_ShouldReturnFalse() {
        assertThat(ValidationUtils.isListContainsTextIgnoreCase(getMultipleValidIdList(), "ALL")).isFalse();
    }

    @Test
    public void testIsListContainsText_AllProvidedInCaps_ShouldReturnTrue() {
        List<String> idList = new ArrayList<>();
        idList.addAll(getMultipleValidIdList());
        idList.add("ALL");
        assertThat(ValidationUtils.isListContainsTextIgnoreCase(idList, "ALL")).isTrue();
    }

    @Test
    public void testIsListContainsText_AllProvidedInSmallCase_ShouldReturnTrue() {
        List<String> idList = new ArrayList<>();
        idList.addAll(getMultipleValidIdList());
        idList.add("ALL");
        assertThat(ValidationUtils.isListContainsTextIgnoreCase(idList, "all")).isTrue();
    }

    @Test
    public void testIsListContainsText_AllProvidedInMixedCase_ShouldReturnTrue() {
        List<String> idList = new ArrayList<>();
        idList.addAll(getMultipleValidIdList());
        idList.add("ALL");
        assertThat(ValidationUtils.isListContainsTextIgnoreCase(idList, "All")).isTrue();
    }

    private List<String> getSingleInvalidIdList() {
        List<String> invalidIdList = new ArrayList<>();
        invalidIdList.add("!@£$");
        return invalidIdList;
    }

    private List<String> getMultipleInvalidIdList() {
        List<String> invalidIdList = new ArrayList<>();
        invalidIdList.addAll(getSingleInvalidIdList());
        invalidIdList.add("@£$%");
        return invalidIdList;
    }

    private List<String> getMultipleValidIdList() {
        List<String> invalidIdList = new ArrayList<>();
        invalidIdList.add("qwerty");
        invalidIdList.add("1234");
        invalidIdList.add("qwerty_12343");
        return invalidIdList;
    }


}
