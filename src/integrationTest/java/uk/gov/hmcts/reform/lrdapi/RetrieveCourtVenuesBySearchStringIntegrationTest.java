package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
public class RetrieveCourtVenuesBySearchStringIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {
    private static final String path = "/court-venues/venue-search";
    private static final String HTTP_STATUS_STR = "http_status";

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400_WhenSearchStringLessThan3Char() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString("?search-string=zz&court-type-id=1000",
                                                         ErrorResponse.class, path
            );
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400_WhenSearchStringContainSpecialChar() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=AB$&court-type-id=1000",
                ErrorResponse.class,
                path
            );
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturn400_WhenCourtTypeIdContainContainSpecialChar() throws JsonProcessingException {
        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=AB$&court-type-id=1,2,$,4",
                ErrorResponse.class,
                path
            );
        assertNotNull(errorResponseMap);
        assertThat(errorResponseMap).containsEntry(HTTP_STATUS_STR, HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnEmptyList_WhenCourtTypeIdIsInvalid() throws JsonProcessingException {
        final var response = (LrdCourtVenueResponse[])
            lrdApiClient.findCourtVenuesBySearchString(
                "?search-string=ABC&court-type-id=100000000",
                LrdCourtVenueResponse[].class,
                path
            );
        assertEquals(0,response.length);
    }
}
