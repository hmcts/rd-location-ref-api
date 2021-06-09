package uk.gov.hmcts.reform.lrdapi.util;

import java.util.List;
import java.util.stream.Collectors;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * A util method to find all the invalid identifiers in the list of identifiers provided in the request.
     * @param idList The list of provided identifiers.
     * @param regex The regex to be used to test the validity of the identifiers in the provided list.
     * @return The list of identifiers that failed to satisfy the validity regex passed.
     */
    public static List<String> findInvalidIdentifiers(List<String> idList, String regex) {
        return idList.stream()
            .filter(id -> !id.matches(regex))
            .collect(Collectors.toList());
    }

    /**
     * A util method to find all the invalid identifiers in the list of identifiers provided in the request.
     * @param idList The list of provided identifiers.
     * @param searchText The regex to be used to test the validity of the identifiers in the provided list.
     * @return The list of identifiers that failed to satisfy the validity regex passed.
     */
    public static boolean isListContainsTextIgnoreCase(List<String> idList, String searchText) {
        return idList.stream().anyMatch(searchText::equalsIgnoreCase);
    }
}
