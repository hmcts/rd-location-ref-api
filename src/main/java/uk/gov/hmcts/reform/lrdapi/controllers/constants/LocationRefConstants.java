package uk.gov.hmcts.reform.lrdapi.controllers.constants;

public class LocationRefConstants {

    private LocationRefConstants() {
    }

    public static final String ALL = "ALL";
    public static final String COMMA = ",";
    public static final String REG_EXP_COMMA_DILIMETER = ",(?!\\\\s)";
    public static final String REG_EXP_SPCL_CHAR = "^[^<>{}\"/|;:.~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©09+]*$";
    public static final String REG_EXP_WHITE_SPACE = "\\s";
    public static final String AlphaNumericRegex = "[0-9a-zA-Z_]+";
    public static final String EXCEPTION_MSG_NO_VALID_EPIM_ID_PASSED = "Bad Request - "
        + "Invalid epims id(s): %s  passed.";
}
