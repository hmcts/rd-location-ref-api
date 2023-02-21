package uk.gov.hmcts.reform.lrdapi.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_WITH_SPECIAL_CHAR_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COMMA;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COURT_TYPE_ID_START_END_WITH_COMMA;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_ONLY_ONE_OF_GIVEN_PARAM;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FILTER_IS_CASE_MANAGEMENT_LOCATION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FILTER_IS_HEARING_LOCATION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FILTER_IS_TEMPORARY_LOCATION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INVALID_ADDITIONAL_FILTER;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.IS_N;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.IS_Y;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ONLY_ONE_PARAM_ORG_SERVICES;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REGEX_FOR_BUILDING_LOCATION_SEARCH;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REG_EXP_COMMA_DILIMETER;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REG_EXP_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REG_EXP_WHITE_SPACE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.SEARCH_STRING_VALUE_ERROR_MESSAGE;

public class ValidationUtils {

    private ValidationUtils() {

    }

    public static void validateInputParameters(String serviceCode, String ccdCaseType, String ccdServiceNames) {

        checkIfSingleValuePresent(ONLY_ONE_PARAM_ORG_SERVICES,serviceCode, ccdCaseType, ccdServiceNames);
        if (StringUtils.isNotBlank(ccdServiceNames)) {
            ccdServiceNames = ccdServiceNames.trim();
            checkIfStringStartsAndEndsWithComma(ccdServiceNames, EXCEPTION_MSG_SPCL_CHAR);
            for (String str : ccdServiceNames.trim().split(REG_EXP_COMMA_DILIMETER)) {
                if (StringUtils.isEmpty(str.trim())) {
                    throw new InvalidRequestException(EXCEPTION_MSG_SPCL_CHAR);
                }
                checkSpecialCharacters(str);
            }
        } else {
            String inputValue = Stream.of(serviceCode, ccdCaseType, ccdServiceNames)
                .filter(StringUtils::isNotBlank).collect(Collectors.joining());
            checkSpecialCharacters(inputValue);
        }
    }

    public static void validateSearchString(String searchString) {
        if (!isRegexSatisfied(searchString, ALPHA_NUMERIC_WITH_SPECIAL_CHAR_REGEX)) {
            throw new InvalidRequestException(String.format(SEARCH_STRING_VALUE_ERROR_MESSAGE, searchString));
        }
    }

    public static void validateSearchStringForBuildingLocationDetails(String searchString) {
        if (!isRegexSatisfied(searchString, REGEX_FOR_BUILDING_LOCATION_SEARCH)) {
            throw new InvalidRequestException(String.format(SEARCH_STRING_VALUE_ERROR_MESSAGE, searchString));
        }
    }

    public static void validateCourtTypeId(String courtTypeId) {
        checkIfStringStartsAndEndsWithComma(courtTypeId, COURT_TYPE_ID_START_END_WITH_COMMA);
        Arrays.stream(courtTypeId.strip().split(REG_EXP_COMMA_DILIMETER)).forEach(c -> {
            if (!isRegexSatisfied(c.trim(), ALPHA_NUMERIC_REGEX)) {
                throw new InvalidRequestException(String.format(COURT_TYPE_ID_START_END_WITH_COMMA, courtTypeId));
            }
        });
    }

    /**
     * Method to check if the passed {@link String} varargs contains a single value.
     * If more than one value is present, this method throws an {@link InvalidRequestException}
     *
     * @param params A java varargs that would contain the list string values. Ideally, it is expected
     *               to have single value.
     */
    public static void checkIfSingleValuePresent(final String oneMandatory,String... params) {
        long requestParamSize = Arrays.stream(params)
            .filter(p -> StringUtils.isNotBlank(p) && !p.equals("null"))
            .count();
        if (requestParamSize > 1) {
            throw new InvalidRequestException(String.format(EXCEPTION_MSG_ONLY_ONE_OF_GIVEN_PARAM, params.length,
                                                            oneMandatory
            ));
        }
    }

    private static void checkSpecialCharacters(String inputValue) {
        inputValue = StringUtils.trim(inputValue);
        if (Pattern.compile(REG_EXP_WHITE_SPACE).matcher(inputValue).find()
            || !Pattern.compile(REG_EXP_SPCL_CHAR).matcher(inputValue).matches()) {
            throw new InvalidRequestException(EXCEPTION_MSG_SPCL_CHAR);
        }
    }


    public static boolean checkBothValuesPresent(String... params) {
        long requestParamSize = Arrays.stream(params)
            .filter(p -> StringUtils.isNotBlank(p) && !p.equals("null"))
            .count();
        if (requestParamSize == 2) {
            return true;
        }
        return false;
    }


    /**
     * Method to check if the passed identifiers are a valid comma separated values.
     * The identifiers are passed as a string and this string value is considered valid only when there are proper
     * comma separated values.
     * An example of a valid value is "123456, 789,1123, 1234 "
     * Below are a few examples of invalid values:
     * "12345,,678"
     * ",12345"
     * "12345,"
     * If identified as valid, the comma separated identifier values are converted to list of strings and are returned.
     * If identified as invalid, it throws an {@InvalidRequestException} to mark the request as BAD_REQUEST.
     *
     * @param csvIds           The comma separated identifiers passed in the request.
     * @param exceptionMessage The message to be included in the {@InvalidRequestException} when thrown.
     *                         The message is expected to have a place holder - "%s" within the string, which is
     *                         later replaced by the list of passed comma separated identifiers.
     * @return The list of identifiers that are created from the passed comma separated identifiers.
     */
    public static List<String> checkIfValidCsvIdentifiersAndReturnList(String csvIds, String exceptionMessage) {
        checkIfStringStartsAndEndsWithComma(csvIds, exceptionMessage);
        var idList = new ArrayList<>(Arrays.asList(csvIds.split(REG_EXP_COMMA_DILIMETER)));
        idList.replaceAll(String::trim);
        if (isEmpty(idList) || isListContainsTextIgnoreCase(idList, StringUtils.EMPTY)) {
            throw new InvalidRequestException(String.format(exceptionMessage, csvIds));
        }
        return idList;
    }

    /**
     * Method to check for invalid identifiers in the passed idList using the passed regex.
     * If present, it prints a warning message in the log file with the identified list of invalid identifiers
     * If the number of invalid identifiers is equal to the actual identifiers passed,
     * it throws an {@InvalidRequestException}. If not, it just removes the identified invalid identifiers from
     * the actual identifier list passed in the request.
     *
     * @param idList           The actual identifier list passed in the request.
     * @param regex            The regex that is used to check for the validity of the passed list of identifiers.
     * @param log              The {@Slf4j} logger.
     * @param componentName    The LRD API logger name.
     * @param exceptionMessage The message to be included in the {@InvalidRequestException} when thrown.
     *                         The message is expected to have a place holder - "%s" within the string, which is
     *                         later replaced by the list of passed comma separated identifiers.
     */
    public static void checkForInvalidIdentifiersAndRemoveFromIdList(List<String> idList,
                                                                     String regex,
                                                                     Logger log,
                                                                     String componentName,
                                                                     String exceptionMessage) {

        List<String> invalidIdentifiers = findInvalidIdentifiers(idList, regex);
        if (!invalidIdentifiers.isEmpty()) {
            String errorDescription = String.format(exceptionMessage, Arrays.toString(invalidIdentifiers.toArray()));
            log.warn("{} : {}", componentName, errorDescription);

            if (idList.size() == invalidIdentifiers.size()) {
                throw new InvalidRequestException(errorDescription);
            }
            idList.removeAll(invalidIdentifiers);
        }
    }

    /**
     * A util method to find all the invalid identifiers in the list of identifiers provided in the request.
     *
     * @param idList The list of provided identifiers.
     * @param regex  The regex to be used to test the validity of the identifiers in the provided list.
     * @return The list of identifiers that failed to satisfy the validity regex passed.
     */
    public static List<String> findInvalidIdentifiers(List<String> idList, String regex) {
        return idList.stream()
            .filter(id -> !id.matches(regex))
            .toList();
    }

    /**
     * A util method to find all the invalid identifiers in the list of identifiers provided in the request.
     *
     * @param idList     The list of provided identifiers.
     * @param searchText The regex to be used to test the validity of the identifiers in the provided list.
     * @return The list of identifiers that failed to satisfy the validity regex passed.
     */
    public static boolean isListContainsTextIgnoreCase(List<String> idList, String searchText) {
        return idList.stream().anyMatch(searchText::equalsIgnoreCase);
    }

    /**
     * A util method to check if the passed string satisfies the passed regex pattern.
     *
     * @param stringToEvaluate The string to be evaluated
     * @param regex The regex to be applied on the passed string
     * @return True if the passed string satisfies the passed regex pattern, else False
     */
    public static boolean isRegexSatisfied(String stringToEvaluate, String regex) {
        return Pattern.compile(regex).matcher(stringToEvaluate).matches();
    }

    /**
     * A util method to check if any part of the passed string matches the passed regex pattern.
     *
     * @param stringToEvaluate The string to be evaluated
     * @param regex The regex to be applied on the passed string
     * @return True if any part of the passed string matches the passed regex pattern, else False
     */
    public static boolean isPatternPresent(String stringToEvaluate, String regex) {
        return Pattern.compile(regex).matcher(stringToEvaluate).find();
    }

    private static void checkIfStringStartsAndEndsWithComma(String csvIds, String exceptionMessage) {
        if (StringUtils.startsWith(csvIds, COMMA) || StringUtils.endsWith(csvIds, COMMA)) {
            throw new InvalidRequestException(String.format(exceptionMessage, csvIds));
        }
    }

    /**
     * A util method to check additional filter validation.
     *
     * @param requestParam Additional parameters to validate.
     */


    public static void validateCourtVenueFilters(CourtVenueRequestParam requestParam) {
        validateSingleFilters(requestParam.getIsHearingLocation(), FILTER_IS_HEARING_LOCATION);
        validateSingleFilters(requestParam.getIsCaseManagementLocation(), FILTER_IS_CASE_MANAGEMENT_LOCATION);

        if (ObjectUtils.isNotEmpty(requestParam.getLocationType())) {
            checkSpecialCharacters(requestParam.getLocationType());
        }
        validateSingleFilters(requestParam.getIsTemporaryLocation(), FILTER_IS_TEMPORARY_LOCATION);
    }

    private static void validateSingleFilters(String value, String filterString) {
        if (value != null
            && !StringUtils.equalsIgnoreCase(value, IS_Y)
            && !StringUtils.equalsIgnoreCase(value, IS_N)) {
            throw new InvalidRequestException(String.format(INVALID_ADDITIONAL_FILTER, filterString));
        }
    }

    public static CourtVenueRequestParam trimCourtVenueRequestParam(CourtVenueRequestParam requestParam) {
        var result = new CourtVenueRequestParam();

        result.setIsHearingLocation(ObjectUtils.isNotEmpty(requestParam.getIsHearingLocation())
                                         ? StringUtils.strip(requestParam.getIsHearingLocation())
                                         : null);
        result.setIsCaseManagementLocation(ObjectUtils.isNotEmpty(requestParam.getIsCaseManagementLocation())
                                         ? StringUtils.strip(requestParam.getIsCaseManagementLocation())
                                         : null);
        result.setLocationType(ObjectUtils.isNotEmpty(requestParam.getLocationType())
                                         ? StringUtils.strip(requestParam.getLocationType())
                                         : null);
        result.setIsTemporaryLocation(ObjectUtils.isNotEmpty(requestParam.getIsTemporaryLocation())
                                         ? StringUtils.strip(requestParam.getIsTemporaryLocation())
                                         : null);
        return result;
    }


}
