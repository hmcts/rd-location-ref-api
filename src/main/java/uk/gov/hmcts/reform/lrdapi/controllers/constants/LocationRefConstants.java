package uk.gov.hmcts.reform.lrdapi.controllers.constants;

public class LocationRefConstants {

    private LocationRefConstants() {
    }

    public static final String ALL = "ALL";
    public static final String COMMA = ",";
    public static final String REG_EXP_COMMA_DILIMETER = ",(?!\\\\s)";
    public static final String REG_EXP_SPCL_CHAR = "^[^<>{}\"/|;:.~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©09+]*$";
    public static final String REG_EXP_WHITE_SPACE = "\\s";
    public static final String ALPHA_NUMERIC_REGEX = "[0-9a-zA-Z_]+";
    public static final String ALPHA_NUMERIC_WITH_SPECICAL_CHAR_REGEX = "^[A-Za-z0-9_@.,'&-() ]{3,}$";
    public static final String ALPHABET_REGEX = "[a-zA-Z]";
    public static final String ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE = "[0-9a-zA-Z]+";
    public static final String REGION_NAME_REGEX = "^[a-zA-Z' -]+";
    public static final String NUMERIC_REGEX = "\\d+";
    public static final String BAD_REQUEST_STR = "Bad Request - ";
    public static final String EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED = BAD_REQUEST_STR
        + "Invalid epims id(s): %s  passed.";
    public static final String EXCEPTION_MSG_NO_VALID_REGION_ID_PASSED = BAD_REQUEST_STR
        + "Invalid Region ID(s): %s  passed.";
    public static final String EXCEPTION_MSG_NO_VALID_REGION_DESCRIPTION_PASSED = BAD_REQUEST_STR
        + "Invalid Region Description(s): %s  passed.";
    public static final String EXCEPTION_MSG_SPCL_CHAR = "Param contains special characters. "
        + "',' comma and '_' underscore allowed only";
    public static final String NON_NUMERIC_VALUE_ERROR_MESSAGE = "The only non-numeric value allowed is"
        + " 'ALL' (case insensitive)";
    public static final String EXCEPTION_MSG_REGION_SPCL_CHAR = "Param contains special characters. "
        + "Comma, apostrophe and whitespace allowed only";
    public static final String EXCEPTION_MSG_ONLY_ONE_OF_GIVEN_PARAM = "Please provide only 1 of %s values/params: %s";

    public static final String INVALID_REGION_ID = "Invalid region id passed - %s";
    public static final String NO_BUILDING_LOCATIONS_FOR_REGION_ID = "No building locations found for region id: - %s";
    public static final String INVALID_CLUSTER_ID = "Invalid cluster id passed - %s";
    public static final String NO_BUILDING_LOCATIONS_FOR_CLUSTER_ID =
        "No building locations found for cluster id: - %s";
    public static final String NO_BUILDING_LOCATIONS = "There are no building locations available at the moment.";
    public static final String NO_BUILDING_LOCATIONS_FOR_EPIMMS_ID =
        "No Building Locations found with the given epims ID: %s";
    public static final String NO_BUILDING_LOCATION_FOR_BUILDING_LOCATION_NAME =
        "No Building Location found for the given building location name: %s";
    public static final String EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR = "Invalid service code. "
        + "Please provide service code without special characters";

    public static final String LD_FLAG = "lrd_location_api";
    public static final String NO_COURT_VENUES_FOUND_FOR_REGION_ID =
        "No court venues found for region id: %s";

    public static final String NO_COURT_VENUES_FOUND_FOR_CLUSTER_ID =
        "No court venues found for cluster id: %s";

    public static final String NO_COURT_VENUES_FOUND_FOR_COURT_TYPE_ID =
        "No court venues found for court type id: %s";

    public static final String NO_COURT_VENUES_FOUND_FOR_FOR_EPIMMS_ID =
        "No court venues found for epimms id: %s";

    public static final String NO_COURT_VENUES_FOUND =
        "There are no court venues found";

    public static final String NO_COURT_VENUES_FOUND_FOR_COURT_VENUE_NAME =
        "No court venues found for court venue name: %s";

    public static final String SEARCH_STRING_VALUE_ERROR_MESSAGE =
        "Invalid search string: %s";

    public static final String COURT_TYPE_ID_START_END_WITH_COMMA =
        "Invalid court type ids: %s";

    public static final String IS_HEARING_LOCATION_Y = "Y";
    public static final String IS_HEARING_LOCATION_N = "N";
    public static final String IS_CASE_MANAGEMENT_LOCATION_Y = "Y";
    public static final String IS_CASE_MANAGEMENT_LOCATION_N = "N";
    public static final String IS_TEMPORARY_LOCATION_Y = "Y";
    public static final String IS_TEMPORARY_LOCATION_N = "N";

    public static final String FILTER_IS_HEARING_LOCATION =
        "is_hearing_location";

    public static final String FILTER_IS_CASE_MANAGEMENT_LOCATION =
        "is_case_management_location";
    public static final String FILTER_IS_TEMPORARY_LOCATION =
        "is_temporary_location";

    public static final String INVALID_ADDITIONAL_FILTER =
        "Invalid %s. Allowed values are Y OR N";

}
