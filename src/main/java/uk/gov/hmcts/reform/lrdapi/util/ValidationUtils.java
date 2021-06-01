package uk.gov.hmcts.reform.lrdapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isStringInExpectedFormat(String stringToEvaluate, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringToEvaluate);
        return matcher.matches();
    }

}
