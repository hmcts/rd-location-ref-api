package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueV2Response;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.INVALID_ADDITIONAL_FILTER;

@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
class RetrieveCourtVenuesBySearchStringV2IntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    private static final String PATH = "/v2/court-venues/venue-search";
    private static final String HTTP_STATUS = "http_status";
    private static final String RESPONSE_BODY = "response_body";

    @Test
    void shouldRetrieveCourtVenuesByServiceCodeWithStatusCode_200() throws JsonProcessingException {
        LrdCourtVenueV2Response[] response = (LrdCourtVenueV2Response[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&service_code=AAA6",
                LrdCourtVenueV2Response[].class,
                PATH
            );

        assertThat(response).isNotEmpty();
        assertThat(response).extracting(LrdCourtVenueV2Response::getServiceCode).contains("AAA6");
        assertThat(response).extracting(LrdCourtVenueV2Response::getMrdVenueId).contains("MRD-123456789");
    }

    @Test
    void shouldRetrieveCourtVenuesByPartialSearchStringWithStatusCode_200() throws JsonProcessingException {
        LrdCourtVenueV2Response[] response = (LrdCourtVenueV2Response[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe",
                LrdCourtVenueV2Response[].class,
                PATH
            );

        assertThat(response).isNotEmpty();
        assertThat(response).extracting(LrdCourtVenueV2Response::getMrdVenueId).contains("MRD-123456789");
        assertThat(Arrays.stream(response).flatMap(venue -> venue.getNames().stream()))
            .anyMatch(name -> name.getName().contains("Aberdeen"));
    }

    @ParameterizedTest
    @CsvSource({
        "is_case_management_location,Y",
        "is_case_management_location,N",
        "is_hearing_location,Y",
        "is_hearing_location,N",
        "is_temporary_location,Y",
        "is_temporary_location,N",
        "is_nightingale_court,Y",
        "is_nightingale_court,N",
        "is_district_registry,Y",
        "is_district_registry,N",
        "is_appeal_centre,Y",
        "is_appeal_centre,N"
    })
    void shouldRetrieveCourtVenuesByCourtUseFilterWithStatusCode_200(String filterName, String filterValue)
        throws JsonProcessingException {
        LrdCourtVenueV2Response[] response = (LrdCourtVenueV2Response[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&" + filterName + "=" + filterValue,
                LrdCourtVenueV2Response[].class,
                PATH
            );

        assertThat(response).isNotEmpty();
        assertThat(response).extracting(LrdCourtVenueV2Response::getServiceCode).doesNotContainNull();
    }

    @Test
    void shouldRetrieveCourtVenuesByLocationTypeWithStatusCode_200() throws JsonProcessingException {
        LrdCourtVenueV2Response[] response = (LrdCourtVenueV2Response[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&location_type=CTSC",
                LrdCourtVenueV2Response[].class,
                PATH
            );

        assertThat(response).isNotEmpty();
        assertThat(response).extracting(LrdCourtVenueV2Response::getLocationType).contains("CTSC");
        assertThat(response).extracting(LrdCourtVenueV2Response::getServiceCode).doesNotContainNull();
    }

    @ParameterizedTest
    @CsvSource({
        "is_case_management_location,is_case_management_location",
        "is_hearing_location,is_hearing_location",
        "is_temporary_location,is_temporary_location",
        "is_nightingale_court,is_nightingale_court",
        "is_district_registry,is_district_registry",
        "is_appeal_centre,is_appeal_centre"
    })
    void shouldReturn400WhenCourtUseFilterIsInvalid(String filterName, String expectedDescription)
        throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=Abe&" + filterName + "=P",
                ErrorResponse.class,
                PATH
            );

        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
        ErrorResponse response = (ErrorResponse) errorResponseMap.get(RESPONSE_BODY);
        assertThat(response.getErrorDescription()).isEqualTo(String.format(
            INVALID_ADDITIONAL_FILTER,
            expectedDescription
        ));
    }
}
