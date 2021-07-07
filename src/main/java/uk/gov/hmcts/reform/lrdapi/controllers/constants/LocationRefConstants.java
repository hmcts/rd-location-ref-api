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
    public static final String REGION_NAME_REGEX = "^[a-zA-Z' -]+";
    public static final String EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED = "Bad Request - "
        + "Invalid epims id(s): %s  passed.";
    public static final String EXCEPTION_MSG_NO_VALID_REGION_ID_PASSED = "Bad Request - "
        + "Invalid Region ID(s): %s  passed.";
    public static final String EXCEPTION_MSG_NO_VALID_REGION_DESCRIPTION_PASSED = "Bad Request - "
        + "Invalid Region Description(s): %s  passed.";
    public static final String EXCEPTION_MSG_SPCL_CHAR = "Param contains special characters. "
        + "',' comma and '_' underscore allowed only";
    public static final String EXCEPTION_MSG_ONLY_ONE_OF_GIVEN_PARAM = "Please provide only 1 of %s params: %s";

}
