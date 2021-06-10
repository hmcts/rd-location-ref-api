package uk.gov.hmcts.reform.lrdapi.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_ValidCsvIdsGiven_ShouldReturnList() {
        assertThat(ValidationUtils
                       .checkIfValidCsvIdentifiersAndReturnList("qwerty,1234,qwerty_12343"
                           , anyString())).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_ComboCsvIdsGiven_ShouldReturnList() {
        assertThat(ValidationUtils
                       .checkIfValidCsvIdentifiersAndReturnList("qwerty,1234,qwerty_12343,, ,"
                           , anyString())).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_InvalidCsvIdsGiven_ShouldThrowException() {
        assertThrows(InvalidRequestException.class, () -> ValidationUtils
            .checkIfValidCsvIdentifiersAndReturnList(",,"
                , anyString()));
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_InvalidCsvIdsGiven_ShouldThrowException_2() {
        assertThrows(InvalidRequestException.class, () -> ValidationUtils
            .checkIfValidCsvIdentifiersAndReturnList(",, ,   "
                , anyString()));
    }

    @Test
    public void testCheckForInvalidIdentifiersAndRemoveFromIdList_NoInvalidIdsGiven_ShouldNotRemoveAnyFromList() {
        List<String> idList = getMultipleValidIdList();
        ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList(idList, AlphaNumericRegex, any(),
                                                                      anyString(), anyString());
        assertThat(idList).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    public void testCheckForInvalidIdentifiersAndRemoveFromIdList_ComboIdsGiven_ShouldRemoveInvalidIdsFromList() {
        Logger logger = LoggerFactory.getLogger(ValidationUtilsTest.class);
        List<String> idList = getMultipleValidIdList();
        idList.addAll(getMultipleInvalidIdList()); //Total of 5 ids in the list, 3 Valid and 2 invalid
        ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList(idList, AlphaNumericRegex, logger,
                                                                      anyString(), anyString());
        assertThat(idList).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    public void testCheckForInvalidIdentifiersAndRemoveFromIdList_AllInvalidIdsGiven_ShouldThrowException() {
        Logger logger = LoggerFactory.getLogger(ValidationUtilsTest.class);
        assertThrows(InvalidRequestException.class, () ->
            ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList(getMultipleInvalidIdList(), AlphaNumericRegex,
                                                                          logger, anyString(),
                                                                          "Bad Request - "
                                                                              + "Invalid epims id(s): %s  passed."));
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
