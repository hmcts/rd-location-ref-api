package uk.gov.hmcts.reform.lrdapi.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfValidCsvIdentifiersAndReturnList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkRegionDescriptionIsValid;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.findInvalidIdentifiers;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isListContainsTextIgnoreCase;

public class ValidationUtilsTest {

    private static final String AlphaNumericRegex = "[0-9a-zA-Z_]+";
    private static final String EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED = "Bad Request - "
        + "Invalid epims id(s): %s  passed.";

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_InvalidInputSpecialChars() {
        var identifiers = new ArrayList<String>();
        identifiers.add("!@£$");
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(identifiers);
    }

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_ValidInputChars() {
        var identifiers = new ArrayList<String>();
        identifiers.add("QWERTY");
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isZero();
    }

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_ValidInputNumeric() {
        var identifiers = new ArrayList<String>();
        identifiers.add("1234");
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isZero();
    }

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_ValidInputAlphaNumeric() {
        var identifiers = new ArrayList<String>();
        identifiers.add("qwerty1234");
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isZero();
    }

    @Test
    public void testFindInvalidIdentifiers_SingleIdGiven_ValidInputAlphaNumericUnderscore() {
        var identifiers = new ArrayList<String>();
        identifiers.add("qwerty_1234");
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isZero();
    }

    @Test
    public void testFindInvalidIdentifiers_MultipleIdsGiven_OneInvalidIdSpecialChars() {
        var identifiers = new ArrayList<String>();
        identifiers.add("1234");
        identifiers.addAll(getSingleInvalidIdList());
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(getSingleInvalidIdList());
    }

    @Test
    public void testFindInvalidIdentifiers_MultipleIdsGiven_SomeInValidInputSpecialChars() {
        var identifiers = new ArrayList<String>();
        identifiers.add("QWERTY");
        identifiers.addAll(getMultipleInvalidIdList());
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(getMultipleInvalidIdList());
    }

    @Test
    public void testFindInvalidIdentifiers_MultipleIdsGiven_AllInvalidIdsSpecialChars() {
        var identifiers = new ArrayList<String>();
        identifiers.addAll(getMultipleInvalidIdList());
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(getMultipleInvalidIdList());
    }

    @Test
    public void testIsListContainsText_NoAllProvided_ShouldReturnFalse() {
        assertThat(isListContainsTextIgnoreCase(getMultipleValidIdList(), "ALL")).isFalse();
    }

    @Test
    public void testIsListContainsText_AllProvidedInCaps_ShouldReturnTrue() {
        var idList = new ArrayList<String>();
        idList.addAll(getMultipleValidIdList());
        idList.add("ALL");
        assertThat(isListContainsTextIgnoreCase(idList, "ALL")).isTrue();
    }

    @Test
    public void testIsListContainsText_AllProvidedInSmallCase_ShouldReturnTrue() {
        var idList = new ArrayList<String>();
        idList.addAll(getMultipleValidIdList());
        idList.add("ALL");
        assertThat(isListContainsTextIgnoreCase(idList, "all")).isTrue();
    }

    @Test
    public void testIsListContainsText_AllProvidedInMixedCase_ShouldReturnTrue() {
        var idList = new ArrayList<String>();
        idList.addAll(getMultipleValidIdList());
        idList.add("ALL");
        assertThat(isListContainsTextIgnoreCase(idList, "All")).isTrue();
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_ValidCsvIdsGiven_ShouldReturnList() {
        assertThat(checkIfValidCsvIdentifiersAndReturnList(
            "qwerty,1234,qwerty_12343",
            "anyString"
        ))
            .hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_ComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234, , 4567",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_2ndComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234,,, 4567",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_3rdComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234,, , 4567",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_4thComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234,,",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_5thComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                ",1234",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_6thComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234,",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_7thComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                ",1234,",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_InvalidCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                ",,",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckIfValidCsvIdentifiersAndReturnList_InvalidCsvIdsGiven_ShouldThrowException_2() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                ",, ,   ",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    public void testCheckForInvalidIdentifiersAndRemoveFromIdList_NoInvalidIdsGiven_ShouldNotRemoveAnyFromList() {
        List<String> idList = getMultipleValidIdList();
        checkForInvalidIdentifiersAndRemoveFromIdList(idList, AlphaNumericRegex, getLogger(),
                                                      "anyString",
                                                      EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );
        assertThat(idList).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    public void testCheckForInvalidIdentifiersAndRemoveFromIdList_ComboIdsGiven_ShouldRemoveInvalidIdsFromList() {
        List<String> idList = getMultipleValidIdList();
        idList.addAll(getMultipleInvalidIdList()); //Total of 5 ids in the list, 3 Valid and 2 invalid
        checkForInvalidIdentifiersAndRemoveFromIdList(idList, AlphaNumericRegex, getLogger(),
                                                      "anyString",
                                                      EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );
        assertThat(idList).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    public void testCheckForInvalidIdentifiersAndRemoveFromIdList_AllInvalidIdsGiven_ShouldThrowException() {
        assertThrows(InvalidRequestException.class, () ->
            checkForInvalidIdentifiersAndRemoveFromIdList(getMultipleInvalidIdList(), AlphaNumericRegex,
                                                          getLogger(), "anyString",
                                                          EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            ));
    }

    @Test(expected = InvalidRequestException.class)
    public void testCheckRegionDescriptionIsValid_WhenRegionIsBlank_ThrowsException() {
        checkRegionDescriptionIsValid("");
    }

    @Test(expected = InvalidRequestException.class)
    public void testCheckRegionDescriptionIsValid_WhenRegionContainsInvalidCharacters_ThrowsException() {
        checkRegionDescriptionIsValid("L*nd@n");
    }

    private List<String> getSingleInvalidIdList() {
        var invalidIdList = new ArrayList<String>();
        invalidIdList.add("!@£$");
        return invalidIdList;
    }

    private List<String> getMultipleInvalidIdList() {
        var invalidIdList = new ArrayList<String>();
        invalidIdList.addAll(getSingleInvalidIdList());
        invalidIdList.add("@£$%");
        return invalidIdList;
    }

    private List<String> getMultipleValidIdList() {
        var invalidIdList = new ArrayList<String>();
        invalidIdList.add("qwerty");
        invalidIdList.add("1234");
        invalidIdList.add("qwerty_12343");
        return invalidIdList;
    }

    private Logger getLogger() {
        return LoggerFactory.getLogger(ValidationUtilsTest.class);
    }


}
