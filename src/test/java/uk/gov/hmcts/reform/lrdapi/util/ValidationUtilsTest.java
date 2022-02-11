package uk.gov.hmcts.reform.lrdapi.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkForInvalidIdentifiersAndRemoveFromIdList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.checkIfValidCsvIdentifiersAndReturnList;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.findInvalidIdentifiers;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.isListContainsTextIgnoreCase;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.trimCourtVenueRequestParam;
import static uk.gov.hmcts.reform.lrdapi.util.ValidationUtils.validateCourtVenueFilters;

class ValidationUtilsTest {

    private static final String AlphaNumericRegex = "[0-9a-zA-Z_]+";
    private static final String EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED = "Bad Request - "
        + "Invalid epims id(s): %s  passed.";

    @Test
    void testFindInvalidIdentifiers_SingleIdGiven_InvalidInputSpecialChars() {
        var identifiers = new ArrayList<String>();
        identifiers.add("!@£$");
        assertEquals(identifiers, findInvalidIdentifiers(identifiers, AlphaNumericRegex));
    }

    @Test
    void testFindInvalidIdentifiers_SingleIdGiven_ValidInputChars() {
        var identifiers = new ArrayList<String>();
        identifiers.add("QWERTY");
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex).size())
            .isZero();

        // testFindInvalidIdentifiers_SingleIdGiven_ValidInputNumeric
        var identifiers1 = new ArrayList<String>();
        identifiers1.add("1234");
        assertThat(findInvalidIdentifiers(identifiers1, AlphaNumericRegex).size())
            .isZero();

        // testFindInvalidIdentifiers_SingleIdGiven_ValidInputAlphaNumeric
        var identifiers2 = new ArrayList<String>();
        identifiers2.add("qwerty1234");
        assertThat(findInvalidIdentifiers(identifiers2, AlphaNumericRegex).size())
            .isZero();

        // testFindInvalidIdentifiers_SingleIdGiven_ValidInputAlphaNumericUnderscore
        var identifiers3 = new ArrayList<String>();
        identifiers3.add("qwerty_1234");
        assertThat(findInvalidIdentifiers(identifiers3, AlphaNumericRegex).size())
            .isZero();
    }

    @Test
    void testFindInvalidIdentifiers_MultipleIdsGiven_OneInvalidIdSpecialChars() {
        var identifiers = new ArrayList<String>();
        identifiers.add("1234");
        identifiers.addAll(getSingleInvalidIdList());
        assertEquals(getSingleInvalidIdList(), findInvalidIdentifiers(identifiers, AlphaNumericRegex));
    }

    @Test
    void testFindInvalidIdentifiers_MultipleIdsGiven_SomeInValidInputSpecialChars() {
        var identifiers = new ArrayList<String>();
        identifiers.add("QWERTY");
        identifiers.addAll(getMultipleInvalidIdList());
        assertEquals(getMultipleInvalidIdList(), findInvalidIdentifiers(identifiers, AlphaNumericRegex));
    }

    @Test
    void testFindInvalidIdentifiers_MultipleIdsGiven_AllInvalidIdsSpecialChars() {
        var identifiers = new ArrayList<String>(getMultipleInvalidIdList());
        assertThat(findInvalidIdentifiers(identifiers, AlphaNumericRegex))
            .isEqualTo(getMultipleInvalidIdList());
    }

    @Test
    void testIsListContainsText_NoAllProvided_ShouldReturnFalse() {
        assertFalse(isListContainsTextIgnoreCase(getMultipleValidIdList(), "ALL"));
    }

    @Test
    void testIsListContainsText_AllProvidedInCaps_ShouldReturnTrue() {
        var idList = new ArrayList<String>(getMultipleValidIdList());
        idList.add("ALL");
        assertTrue(isListContainsTextIgnoreCase(idList, "ALL"));

        // testIsListContainsText_AllProvidedInSmallCase_ShouldReturnTrue
        var idList1 = new ArrayList<String>(getMultipleValidIdList());
        idList1.add("ALL");
        assertTrue(isListContainsTextIgnoreCase(idList1, "all"));

        // testIsListContainsText_AllProvidedInMixedCase_ShouldReturnTrue
        var idList2 = new ArrayList<String>(getMultipleValidIdList());
        idList2.add("ALL");
        assertTrue(isListContainsTextIgnoreCase(idList2, "All"));
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_ValidCsvIdsGiven_ShouldReturnList() {
        assertThat(checkIfValidCsvIdentifiersAndReturnList(
            "qwerty,1234,qwerty_12343",
            "anyString"
        )).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_ComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234, , 4567",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_2ndComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234,,, 4567",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_3rdComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234,, , 4567",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_4thComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234,,",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_5thComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                ",1234",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_6thComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                "1234,",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_7thComboCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                ",1234,",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_InvalidCsvIdsGiven_ShouldThrowException() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                ",,",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckIfValidCsvIdentifiersAndReturnList_InvalidCsvIdsGiven_ShouldThrowException_2() {
        assertThrows(
            InvalidRequestException.class,
            () -> checkIfValidCsvIdentifiersAndReturnList(
                ",, ,   ",
                EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            )
        );
    }

    @Test
    void testCheckForInvalidIdentifiersAndRemoveFromIdList_NoInvalidIdsGiven_ShouldNotRemoveAnyFromList() {
        List<String> idList = getMultipleValidIdList();
        checkForInvalidIdentifiersAndRemoveFromIdList(idList, AlphaNumericRegex, getLogger(),
                                                      "anyString",
                                                      EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );
        assertThat(idList).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    void testCheckForInvalidIdentifiersAndRemoveFromIdList_ComboIdsGiven_ShouldRemoveInvalidIdsFromList() {
        List<String> idList = getMultipleValidIdList();
        idList.addAll(getMultipleInvalidIdList()); //Total of 5 ids in the list, 3 Valid and 2 invalid
        checkForInvalidIdentifiersAndRemoveFromIdList(idList, AlphaNumericRegex, getLogger(),
                                                      "anyString",
                                                      EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
        );
        assertThat(idList).hasSize(3).hasSameElementsAs(getMultipleValidIdList());
    }

    @Test
    void testCheckForInvalidIdentifiersAndRemoveFromIdList_AllInvalidIdsGiven_ShouldThrowException() {
        List<String> idList = getMultipleInvalidIdList();
        Logger logger = LoggerFactory.getLogger(ValidationUtilsTest.class);
        assertThrows(InvalidRequestException.class, () ->
            checkForInvalidIdentifiersAndRemoveFromIdList(idList, AlphaNumericRegex, logger, "anyString",
                                                          EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED
            ));
    }

    @Test
    void testValidateCourtVenueRequestParamForAllValidValues() {
        CourtVenueRequestParam courtVenueRequestParam =
            new CourtVenueRequestParam();

        courtVenueRequestParam.setIsHearingLocation("Y");
        courtVenueRequestParam.setIsCaseManagementLocation("Y");
        courtVenueRequestParam.setLocationType("CTSC");
        courtVenueRequestParam.setIsTemporaryLocation("Y");


        validateCourtVenueFilters(courtVenueRequestParam);

        courtVenueRequestParam =
            new CourtVenueRequestParam();

        courtVenueRequestParam.setIsHearingLocation("y");
        courtVenueRequestParam.setIsCaseManagementLocation("y");
        courtVenueRequestParam.setLocationType("CTSC");
        courtVenueRequestParam.setIsTemporaryLocation("y");


        validateCourtVenueFilters(courtVenueRequestParam);

        courtVenueRequestParam =
            new CourtVenueRequestParam();

        courtVenueRequestParam.setIsHearingLocation("N");
        courtVenueRequestParam.setIsCaseManagementLocation("N");
        courtVenueRequestParam.setLocationType("CTSC");
        courtVenueRequestParam.setIsTemporaryLocation("N");


        validateCourtVenueFilters(courtVenueRequestParam);

        courtVenueRequestParam =
            new CourtVenueRequestParam();

        courtVenueRequestParam.setIsHearingLocation("n");
        courtVenueRequestParam.setIsCaseManagementLocation("n");
        courtVenueRequestParam.setLocationType("CTSC");
        courtVenueRequestParam.setIsTemporaryLocation("n");


        validateCourtVenueFilters(courtVenueRequestParam);

        assertTrue(true);
    }

    @Test
    void testValidateCourtVenueRequestParamInvalidValidYValues() {
        CourtVenueRequestParam courtVenueRequestParam1 =
            new CourtVenueRequestParam();

        courtVenueRequestParam1.setIsHearingLocation("");
        courtVenueRequestParam1.setIsCaseManagementLocation("y");
        courtVenueRequestParam1.setLocationType("ctsc");
        courtVenueRequestParam1.setIsTemporaryLocation("y");

        Throwable exception1 = assertThrows(
            InvalidRequestException.class,
            () -> validateCourtVenueFilters(courtVenueRequestParam1)
        );
        assertNotNull(exception1);

        CourtVenueRequestParam courtVenueRequestParam2 =
            new CourtVenueRequestParam();

        courtVenueRequestParam2.setIsHearingLocation("y");
        courtVenueRequestParam2.setIsCaseManagementLocation("");
        courtVenueRequestParam2.setLocationType("ctsc");
        courtVenueRequestParam2.setIsTemporaryLocation("y");

        Throwable exception2 = assertThrows(
            InvalidRequestException.class,
            () -> validateCourtVenueFilters(courtVenueRequestParam2)
        );
        assertNotNull(exception2);

        CourtVenueRequestParam courtVenueRequestParam3 =
            new CourtVenueRequestParam();

        courtVenueRequestParam3.setIsHearingLocation("y");
        courtVenueRequestParam3.setIsCaseManagementLocation("y");
        courtVenueRequestParam3.setLocationType("ctsc@£");
        courtVenueRequestParam3.setIsTemporaryLocation("y");

        Throwable exception3 = assertThrows(
            InvalidRequestException.class,
            () -> validateCourtVenueFilters(courtVenueRequestParam3)
        );
        assertNotNull(exception3);

        CourtVenueRequestParam courtVenueRequestParam4 =
            new CourtVenueRequestParam();

        courtVenueRequestParam4.setIsHearingLocation("y");
        courtVenueRequestParam4.setIsCaseManagementLocation("y");
        courtVenueRequestParam4.setLocationType("ctsc");
        courtVenueRequestParam4.setIsTemporaryLocation("");

        Throwable exception4 = assertThrows(
            InvalidRequestException.class,
            () -> validateCourtVenueFilters(courtVenueRequestParam4)
        );
        assertNotNull(exception4);

    }

    @Test
    void testTrimCourtVenueRequestParamValues() {
        CourtVenueRequestParam courtVenueRequestParam =
            new CourtVenueRequestParam();

        courtVenueRequestParam.setIsHearingLocation("    Y");
        courtVenueRequestParam.setIsCaseManagementLocation("    Y    ");
        courtVenueRequestParam.setLocationType("     CTSC    ");
        courtVenueRequestParam.setIsTemporaryLocation("Y    ");


        CourtVenueRequestParam response1 = trimCourtVenueRequestParam(courtVenueRequestParam);

        assertThat(response1.getIsHearingLocation()).isEqualTo("Y");
        assertThat(response1.getIsHearingLocation()).isEqualTo("Y");
        assertThat(response1.getIsHearingLocation()).isEqualTo("Y");
        assertThat(response1.getIsHearingLocation()).isEqualTo("Y");


    }

    private List<String> getSingleInvalidIdList() {
        var invalidIdList = new ArrayList<String>();
        invalidIdList.add("!@£$");
        return invalidIdList;
    }

    private List<String> getMultipleInvalidIdList() {
        var invalidIdList = new ArrayList<String>(getSingleInvalidIdList());
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
