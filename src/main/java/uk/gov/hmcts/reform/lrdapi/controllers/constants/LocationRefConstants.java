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
    public static final String ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE = "[0-9a-zA-Z]+";
    public static final String REGION_NAME_REGEX = "^[a-zA-Z' -]+";
    public static final String NUMERIC_REGEX = "\\d+";
    public static final String EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED = "Bad Request - "
        + "Invalid epims id(s): %s  passed.";
    public static final String EXCEPTION_MSG_SPCL_CHAR = "Param contains special characters. "
        + "',' comma and '_' underscore allowed only";
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

}
