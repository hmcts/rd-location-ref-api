package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FILTER_IS_CASE_MANAGEMENT_LOCATION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FILTER_IS_HEARING_LOCATION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.FILTER_IS_TEMPORARY_LOCATION;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INVALID_ADDITIONAL_FILTER;

@WithTags({@WithTag("testType:Integration")})
class RetrieveCourtVenuesBySearchStringIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {
    private static final String path = "/court-venues/venue-search";
    private static final String HTTP_STATUS_STR = "http_status";
    private static final String RESPONSE_BODY = "response_body";

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=Abe","?search-string=Abe&court-type-id=17,10,23"})
    @SuppressWarnings("unchecked")
    void shouldRetrieveCourtVenues_For_SearchString_WithStatusCodeCourtTypeCombination_200(String queryParams)
        throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                queryParams,
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(11);
        responseVerification(new ArrayList<>(Arrays.asList(response)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=zz&court-type-id=1000", "?search-string=AB$&court-type-id=1000",
        "?search-string=AB$&court-type-id=1,2,$,4"})
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenInvalidParamsPassed(String parameter) throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(parameter, ErrorResponse.class, path);

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnEmptyList_WhenCourtTypeIdIsInvalid() throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000",
                LrdCourtVenueResponse[].class,
                path
            );
        assertEquals(0,response.length);
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=ABC&court-type-id=100000000&is_hearing_location=Yes",
        "?search-string=ABC&court-type-id=100000000&is_hearing_location=$%$%"})
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsHearingLocationContainSpecialChar(String searchString) throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                searchString,
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_HEARING_LOCATION);
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=ABC&court-type-id=100000000&is_case_management_location=Yes",
        "?search-string=ABC&court-type-id=100000000&is_case_management_location=$%$%"})
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsCaseManagementLocation(String searchString) throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                searchString,
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_CASE_MANAGEMENT_LOCATION);
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=ABC&court-type-id=100000000&is_temporary_location=Yes",
        "?search-string=ABC&court-type-id=100000000&is_temporary_location=$%$%"})
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsTemporaryLocationContain(String searchString) throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                searchString,
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_TEMPORARY_LOCATION);
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=Abe&court-type-id=10,17,23&is_case_management_location=Y",
        "?search-string=Abe&court-type-id=10,17,23&is_case_management_location=y"})
    @SuppressWarnings("unchecked")
    void shouldReturn200_WhenIsCaseManagementLocation(String searchString) throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                searchString,
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(2);
        responseVerification(new ArrayList<>(Arrays.asList(response)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=Abe&court-type-id=10,17,23&location_type=NBC",
        "?search-string=Abe&court-type-id=10,17,23&location_type=nbc"})
    @SuppressWarnings("unchecked")
    void shouldReturn200_WhenLocationTypeContainValue(String searchString) throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                searchString,
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(1);
        responseVerification(new ArrayList<>(Arrays.asList(response)));
    }

    void responseVerification(ArrayList<LrdCourtVenueResponse> courtVenueResponse) {
        var courtNameVerified = courtVenueResponse
            .stream()
            .filter(venue -> venue.getCourtName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getSiteName().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getCourtAddress().strip().toLowerCase().contains("Abe".toLowerCase())
                || venue.getPostcode().strip().toLowerCase().contains("Abe".toLowerCase()))
            .collect(Collectors.toList());

        assertTrue(courtNameVerified
                       .stream()
                       .allMatch(venue -> venue.getCourtStatus().equals("Open"))
        );
        courtVenueResponse.removeAll(courtNameVerified);

        assertTrue(courtVenueResponse.isEmpty());
    }

    void errorResponseVerification(Map<String, Object> errorResponseMap, String expectedErrorDescription) {
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
        ErrorResponse reason = (ErrorResponse)errorResponseMap.get(RESPONSE_BODY);
        assertThat(reason.getErrorCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(reason.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(reason.getErrorDescription()).isEqualTo(String.format(INVALID_ADDITIONAL_FILTER,
                                                                         expectedErrorDescription));
    }

}
