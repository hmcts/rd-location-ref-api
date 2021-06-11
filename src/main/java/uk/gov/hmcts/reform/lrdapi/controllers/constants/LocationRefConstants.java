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
    public static final String BUILDING_LOCATION_PARAM_NAME = "building";
    public static final String COURT_LOCATION_PARAM_NAME = "court";
    public static final String USER_NAME_PATTERN = "^[A-Za-z0-9]+[\\w!#$%&'’.*+/=?`{|}~^-]+"
        + "(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*";
    public static final String NAME_REGEX = "^[a-zA-Z' .-]{1,127}$";
    public static final String FIRST_NAME_INVALID = "First Name is invalid - can only contain Alphabetic,"
        + " empty space, ', - characters and must be less than 128 characters";
    public static final String LAST_NAME_INVALID = "Last Name is invalid - can only contain Alphabetic,"
        + " empty space, ', - characters and must be less than 128 characters";
    public static final String UPLOAD_FILE_AGAIN = "and upload the file again";
    public static final String EPIMS_ID_MISSING = "You must add ePIMS id " + UPLOAD_FILE_AGAIN;
    public static final String BUILDING_LOCATION_NAME_MISSING = "You must add building location name "
        + UPLOAD_FILE_AGAIN;
    public static final String COURT_LOCATION_NAME_MISSING = "You must add court location name "
        + UPLOAD_FILE_AGAIN;
    public static final String OPEN_FOR_PUBLIC_MISSING = "You must add if the court is open for public or not "
        + UPLOAD_FILE_AGAIN;
    public static final String POSTCODE_MISSING = "You must add postcode and upload the file again";
    public static final String BUILDING_ADDRESS_MISSING = "You must add building address " + UPLOAD_FILE_AGAIN;
    public static final String COURT_ADDRESS_MISSING = "You must add court address " + UPLOAD_FILE_AGAIN;
    public static final String BUILDING_LOCATION_SHEET_NAME = "Building_Location";
    public static final String COURT_LOCATION_SHEET_NAME = "Court Location data seeding";

}
