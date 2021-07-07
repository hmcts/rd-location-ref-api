package uk.gov.hmcts.reform.lrdapi.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COMMA;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_ONLY_ONE_OF_GIVEN_PARAM;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_REGION_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REGION_NAME_REGEX;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REG_EXP_COMMA_DILIMETER;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REG_EXP_SPCL_CHAR;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.REG_EXP_WHITE_SPACE;

public class ValidationUtils {

    private ValidationUtils() {

    }

    public static boolean validateInputParameters(String serviceCode, String ccdCaseType, String ccdServiceNames) {

        checkIfSingleValuePresent(serviceCode, ccdCaseType, ccdServiceNames);
        if (StringUtils.isNotBlank(ccdServiceNames)) {
            ccdServiceNames = ccdServiceNames.trim();
            if (StringUtils.startsWith(ccdServiceNames, COMMA)
                || StringUtils.endsWith(ccdServiceNames, COMMA)) {
                throw new InvalidRequestException(EXCEPTION_MSG_SPCL_CHAR);
            }
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
        return true;
    }

    public static void checkIfSingleValuePresent(String... params) {
        long requestParamSize = Arrays.stream(params)
            .filter(StringUtils::isNotBlank)
            .count();
        if (requestParamSize > 1) {
            throw new InvalidRequestException(String.format(EXCEPTION_MSG_ONLY_ONE_OF_GIVEN_PARAM, params.length,
                                                            Arrays.toString(params)
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

    /**
     * Method to check if the passed identifiers are a valid comma separated values.
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
        var idList = new ArrayList<>(Arrays.asList(csvIds.split(REG_EXP_COMMA_DILIMETER)));
        idList.replaceAll(String::trim);
        idList.removeAll(Collections.singleton(StringUtils.EMPTY));
        if (idList.isEmpty()) {
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
            .collect(Collectors.toList());
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

    public static void checkRegionDescriptionIsValid(String region) {

        if (isBlank(region)) {
            throw new InvalidRequestException("No Region Description provided");
        }

        if (!Pattern.compile(REGION_NAME_REGEX).matcher(region).matches()) {
            throw new InvalidRequestException(EXCEPTION_MSG_REGION_SPCL_CHAR);
        }
    }

    public static boolean isRegexSatisfied(String stringToEvaluate, String regex) {
        return Pattern.compile(regex).matcher(stringToEvaluate).matches();
    }

}
