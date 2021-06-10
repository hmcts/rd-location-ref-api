package uk.gov.hmcts.reform.lrdapi.controllers.constants;

public class LocationRefConstants {

    private LocationRefConstants() {
    }

    public static final String ALL = "ALL";
    public static final String COMMA = ",";
    public static final String NO_DATA_FOUND = "The Caseworker data could not be found";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String UNAUTHORIZED_ERROR =
        "Unauthorized Error : The requested resource is restricted and requires authentication";
    public static final String FORBIDDEN_ERROR = "Forbidden Error: Access denied for invalid permissions";
    public static final String FILE = "file";
    public static final String USER_NAME_PATTERN = "^[A-Za-z0-9]+[\\w!#$%&'’.*+/=?`{|}~^-]+"
        + "(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*";
    public static final String DOMAIN_JUSTICE_GOV_UK = "justice.gov.uk";
    public static final String INVALID_EMAIL = "You must add a valid justice.gov.uk email"
        + " address and upload the file again";
    public static final String NAME_REGEX = "^[a-zA-Z' .-]{1,127}$";
    public static final String FIRST_NAME_INVALID = "First Name is invalid - can only contain Alphabetic,"
        + " empty space, ', - characters and must be less than 128 characters";
    public static final String LAST_NAME_INVALID = "Last Name is invalid - can only contain Alphabetic,"
        + " empty space, ', - characters and must be less than 128 characters";
    public static final String FIRST_NAME_MISSING = "You must add a first name and upload the file again";
    public static final String LAST_NAME_MISSING = "You must add a last name and upload the file again";
    public static final String MISSING_REGION = "You must add a region and upload the file again";
    public static final String NO_ROLE_PRESENT = "You must add role details and upload the file again";
    public static final String NO_WORK_AREA_PRESENT = "You must add details of at least one area of work"
        + " and upload the file again";
    public static final String NO_USER_TYPE_PRESENT = "You must add a user type and upload the file again";
    public static final String REQUIRED_CW_SHEET_NAME = "Case Worker Data";


}
