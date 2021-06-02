package uk.gov.hmcts.reform.lrdapi.util;

import org.apache.commons.lang.StringUtils;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstraintValidation {

    private ConstraintValidation() {

    }

    private static final String REG_EXP_SPCL_CHAR = "^[^<>{}\"/|;:.~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©09+]*$";
    private static final String REG_EXP_WHITE_SPACE = "\\s";
    private static final String EXCEPTION_MSG_SPCL_CHAR = "Param contains special characters. "
        + "',' comma and '_' underscore allowed only";
    private static final String EXCEPTION_MSG_ONLY_ONE_OF_THREE_PARAM = "Please provide only 1 of 3 params: "
        + "serviceCode, ccdCaseType, ccdServiceNames.";
    private static final String REG_EXP_COMMA_DILIMETER = ",(?!\\\\s)";

    public static boolean validateInputParameters(String serviceCode, String ccdCaseType, String ccdServiceNames) {

        long requestParamSize = Stream.of(serviceCode, ccdCaseType, ccdServiceNames)
            .filter(StringUtils::isNotBlank)
            .count();
        if (requestParamSize > 1) {
            throw new InvalidRequestException(EXCEPTION_MSG_ONLY_ONE_OF_THREE_PARAM);
        }
        if (StringUtils.isNotBlank(ccdServiceNames)) {
            ccdServiceNames = ccdServiceNames.trim();
            if (StringUtils.startsWith(ccdServiceNames, ",") || StringUtils.endsWith(ccdServiceNames, ",")) {
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

    private static void checkSpecialCharacters(String inputValue) {
        inputValue = StringUtils.trim(inputValue);
        if (Pattern.compile(REG_EXP_WHITE_SPACE).matcher(inputValue).find()
            || !Pattern.compile(REG_EXP_SPCL_CHAR).matcher(inputValue).matches()) {
            throw new InvalidRequestException(EXCEPTION_MSG_SPCL_CHAR);
        }
    }
}
