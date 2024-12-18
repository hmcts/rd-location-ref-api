package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
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

    @Test
    @SuppressWarnings("unchecked")
    void shouldRetrieveCourtVenues_For_SearchString_WithStatusCode_200()
        throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(12);
        responseVerification(new ArrayList<>(Arrays.asList(response)));
        assertEquals("Aberdeen Tribunal External", response[11].getExternalShortName());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenSearchStringIsEmpty()
        throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "",
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=Stoke-on-Trent", "?search-string=Stoke-", "?search-string=stoke-on",
        "?search-string=stoke-on-"})
    @SuppressWarnings("unchecked")
    void shouldRetrieveCourtVenues_For_SearchStringWithHyphen_WithStatusCode_200(String parameter)
        throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(parameter, LrdCourtVenueResponse[].class, path);

        assertThat(response).isNotEmpty().hasSize(1);
        assertThat(response[0].getSiteName()).isEqualTo("Stoke-on-Trent Combined Court");
        assertThat(response[0].getCourtName()).isEqualTo("STOKE-ON-TRENT COMBINED COURT");
        assertThat(response[0].getCourtAddress()).isEqualTo("BETHESDA STREET");
        assertThat(response[0].getPostcode()).isEqualTo("ST1 3BP");
    }

    @Test
    void shouldRetrieveCourtVenues_For_SearchString_And_CourtTypeId_WithStatusCode_200()
        throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&court-type-id=17,10,23",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(12);
        responseVerification(new ArrayList<>(Arrays.asList(response)));
        assertEquals("Aberdeen Tribunal External", response[11].getExternalShortName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"?search-string=abc--", "?search-string=ab__c", "?search-string=___c",
        "?search-string=___", "?search-string=@@@", "?search-string=---", "?search-string='''",
        "?search-string=&&&", "?search-string=...", "?search-string=,,,", "?search-string=(((",
        "?search-string=)))", "?search-string=zz&court-type-id=1000", "?search-string=AB$&court-type-id=1000",
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

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsHearingLocationContainOtherYN() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000&is_hearing_location=Yes",
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_HEARING_LOCATION);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsCaseManagementLocationContainOtherYN() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000&is_case_management_location=Yes",
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_CASE_MANAGEMENT_LOCATION);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsTemporaryLocationContainOtherYN() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000&is_temporary_location=Yes",
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_TEMPORARY_LOCATION);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsHearingLocationContainSpecialChar() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000&is_hearing_location=$%$%",
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_HEARING_LOCATION);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsCaseManagementLocationContainSpecialChar() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000&is_case_management_location=$%$%",
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_CASE_MANAGEMENT_LOCATION);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn400_WhenIsTemporaryLocationContainSpecialChar() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000&is_temporary_location=$%$%",
                ErrorResponse.class,
                path
            );

        assertNotNull(errorResponseMap);
        errorResponseVerification(errorResponseMap, FILTER_IS_TEMPORARY_LOCATION);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn200_WhenIsCaseManagementLocationContainY() throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&court-type-id=10,17,23&is_case_management_location=Y",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(2);
        responseVerification(new ArrayList<>(Arrays.asList(response)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn200_WhenIsCaseManagementLocationContainY_lowerCase() throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&court-type-id=10,17,23&is_case_management_location=y",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(2);
        responseVerification(new ArrayList<>(Arrays.asList(response)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn200_WhenLocationTypeContainValue() throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&court-type-id=10,17,23&location_type=NBC",
                LrdCourtVenueResponse[].class,
                path
            );

        assertThat(response).isNotEmpty().hasSize(1);
        responseVerification(new ArrayList<>(Arrays.asList(response)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturn200_WhenLocationTypeContainValue_lowercase() throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&court-type-id=10,17,23&location_type=nbc",
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
