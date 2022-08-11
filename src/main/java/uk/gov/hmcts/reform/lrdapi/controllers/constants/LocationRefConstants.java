package uk.gov.hmcts.reform.lrdapi.controllers.constants;

public class LocationRefConstants {

    private LocationRefConstants() {
    }

    public static final String ALL = "ALL";
    public static final String COMMA = ",";
    public static final String REG_EXP_COMMA_DILIMETER = ",(?!\\\\s)";
    public static final String REG_EXP_SPCL_CHAR = "^[^<>{}\"/|;:.~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©+]*$";
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
    public static final String EXCEPTION_MSG_ONLY_ONE_OF_GIVEN_PARAM = "Please provide only 1 of %s "
        + "values of params: %s";

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

    public static final String IS_Y = "Y";
    public static final String IS_N = "N";

    public static final String ONLY_ONE_PARAM_REQUIRED_COURT_VENUE = "epimms_id, court_type_id,"
        + " region_id, cluster_id, court_venue_name";
    public static final String ONLY_ONE_PARAM_REQUIRED_REGION  = "region, regionId";
    public static final String ONLY_ONE_PARAM_REQUIRED_BUILDING_LOCATION =
        "epimms_id, building_location_name, region_id, cluster_id";
    public static final String ONLY_ONE_PARAM_ORG_SERVICES = "serviceCode, ccdCaseType, ccdServiceNames";

    public static final String RET_LOC_NOTES_1 = "Any valid IDAM role is sufficient to access this API \n";
    public static final String RET_LOC_NOTES_2 = "For the request param 'epimms_id', ";
    public static final String RET_LOC_NOTES_3 = "the value can be a single id for which single building "
        + "location object would be returned or ";
    public static final String RET_LOC_NOTES_4 = "a list of ids passed as comma separated values for which the "
        + "associated building location ";
    public static final String RET_LOC_NOTES_5 = "objects would be returned as a list.\nAdditionally, if 'ALL' is "
        + "passed as the epimms_id value, all the ";
    public static final String RET_LOC_NOTES_6 = "available building locations are returned as a list.";
    public static final String RET_LOC_NOTES_7 = "For the request param 'building_location_name', the value "
        + "needs to be a single building location name ";
    public static final String RET_LOC_NOTES_8 = "for which a single building location object would be returned.\n";
    public static final String RET_LOC_NOTES_9 = "For the request param 'region_id', the value needs to be a "
        + "single region_id ";
    public static final String RET_LOC_NOTES_10 = "for which all the associated building location objects would "
        + "be returned as a list.\n";
    public static final String RET_LOC_NOTES_11 = "For the request param 'cluster_id', the value needs to be a "
        + "single cluster_id ";
    public static final String RET_LOC_NOTES_12 = "for which all the associated building location objects would "
        + "be returned as a list.\n";
    public static final String RET_LOC_NOTES_13 = "If no params are passed, then all the available building "
        + "locations which have the ";
    public static final String RET_LOC_NOTES_14 = "building location status as 'OPEN' are returned as a list.\n";
    public static final String RET_LOC_NOTES_15 = "At a time only one param is allowed "
        + "from 'epimms_id','building_location_name','region_id','cluster_id'.";

    public static final String RET_LOC_VEN_NOTES_1 = "No roles required to access this API.\n";
    public static final String RET_LOC_VEN_NOTES_2 = "For the request param 'epimms_id', either a "
        + "single epimms_id or a list of epimms_ids separated by comas";
    public static final String RET_LOC_VEN_NOTES_3 = " can be passed. In any of these cases, a list of associated "
        + "court venues would be returned.\n";
    public static final String RET_LOC_VEN_NOTES_4 = "Additionally, if 'ALL' is passed as the epimms_id value, then "
        + "all the available court venues";
    public static final String RET_LOC_VEN_NOTES_5 = " associated with the available list of epimms_id are returned "
        + "as a list.\n";
    public static final String RET_LOC_VEN_NOTES_6 = "For the request param 'court_type_id', then all the court venues "
        + "that have the status as 'Open' ";
    public static final String RET_LOC_VEN_NOTES_7 = "with the requested court_type_id are returned as a list.\n";
    public static final String RET_LOC_VEN_NOTES_8 = "For the request param 'region_id', the value needs to be a "
        + "single region_id ";
    public static final String RET_LOC_VEN_NOTES_9 = "for which all the associated court venues with the status "
        + "as 'Open' would be returned as a list.\n";
    public static final String RET_LOC_VEN_NOTES_10 = "For the request param 'cluster_id', the value needs to be a "
        + "single cluster_id ";
    public static final String RET_LOC_VEN_NOTES_11 = "for which all the associated court venues with the status "
        + "as 'Open' would be returned as a list.\n";
    public static final String RET_LOC_VEN_NOTES_12 = "For the request param 'court_venue_name', all the associated "
        + "court venues that have the same site name ";
    public static final String RET_LOC_VEN_NOTES_13 = "or court name irrespective of the case are returned "
        + "as a list.\n";
    public static final String RET_LOC_VEN_NOTES_14 = "If no params are passed, then all the available court venues "
        + "which have the ";
    public static final String RET_LOC_VEN_NOTES_15 = "status as 'OPEN' are returned as a list.\n";
    public static final String RET_LOC_VEN_NOTES_16 = "Additional API filters are applied with request "
        + "params 'is_hearing_location', ";
    public static final String RET_LOC_VEN_NOTES_17 = "'is_case_management_location'\n";
    public static final String RET_LOC_VEN_NOTES_18 = "'location_type' and 'is_temporary_location'.\n";
    public static final String RET_LOC_VEN_NOTES_19 = "Optional param's "
        + "are 'is_hearing_location','is_case_management_location','location_type'";
    public static final String RET_LOC_VEN_NOTES_20 = "and 'is_temporary_location'.\n";
    public static final String RET_LOC_VEN_NOTES_21 = "At a time only one param is allowed "
        + "from 'epimms_id','court_type_id','region_id','cluster_id'";
    public static final String RET_LOC_VEN_NOTES_22 = "'court_venue_name'.";
}
