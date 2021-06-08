package uk.gov.hmcts.reform.lrdapi.util;

import java.util.List;
import java.util.stream.Collectors;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static List<String> findInvalidIdentifiers(List<String> epimsIdList, String regex) {
        return epimsIdList.stream()
            .filter(id -> !id.matches(regex))
            .collect(Collectors.toList());
    }

}
