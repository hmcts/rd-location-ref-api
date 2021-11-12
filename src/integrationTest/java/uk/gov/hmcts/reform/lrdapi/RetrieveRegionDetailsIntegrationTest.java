package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
class RetrieveRegionDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    public static final String HTTP_STATUS = "http_status";

    List<LrdRegionResponse> expectedListAll = List.of(
        new LrdRegionResponse(new Region("1", "National", null)),
        new LrdRegionResponse(new Region("2", "London", null)),
        new LrdRegionResponse(new Region("3", "Midlands", null)),
        new LrdRegionResponse(new Region("4", "North East", null)),
        new LrdRegionResponse(new Region("5", "North West", null)),
        new LrdRegionResponse(new Region("6", "South East", null)),
        new LrdRegionResponse(new Region("7", "South West", null)),
        new LrdRegionResponse(new Region("8", "Wales", null)),
        new LrdRegionResponse(new Region("9", "Scotland", null))
    );

    @Test
    void returnsRegionDetailsByDescriptionWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("London", LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerification(response, 1);
    }

    @Test
    void returnsRegionDetailsByDescriptionsWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("London, Midlands", LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerification(response, 2);
    }

    @Test
    void returnsRegionDetailsByDescriptionCaseInsensitiveWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("LoNdOn", LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerification(response, 1);
    }

    @Test
    void returnsRegionDetailsByIdWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("2", LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerification(response, 1);
    }

    @Test
    void returnsRegionDetailsByIdsWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("2, 3", LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerification(response, 2);
    }

    @Test
    void returnsRegionDetailsByIdAllWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("ALL", LrdRegionResponse[].class);

        assertNotNull(response);
        responseVerificationForAll(response, expectedListAll);
    }

    @Test
    void returnsRegionDetailsById1AndAllWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("1, ALL", LrdRegionResponse[].class);

        assertNotNull(response);
        assertEquals(9, response.size());
        assertEquals("1", response.get(0).getRegionId());
        assertEquals("National", response.get(0).getDescription());
        assertNull(response.get(0).getWelshDescription());
    }

    @Test
    void returnsRegionDetailsNoParamWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("", LrdRegionResponse[].class);

        assertNotNull(response);
        assertEquals(8, response.size());
    }

    @Test
    void doesNotReturnRegionDetailsByInvalidDescriptionWithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("Invalid Description", ErrorResponse.class);


        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
    }

    @Test
    void doesNotReturnRegionByInvalidDescriptionSpecialCharWithStatusCode_400() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("Lo£nd*on", ErrorResponse.class);


        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
    }

    @Test
    void doesNotReturnRegionByInvalidIdnSpecialCharWithStatusCode_400() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("1*", ErrorResponse.class);


        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
    }

    private void responseVerification(List<LrdRegionResponse> response, int expectedRegions) {
        assertThat(response.size()).isEqualTo(expectedRegions);
        assertThat(response.get(0).getRegionId()).isEqualTo("2");
        assertThat(response.get(0).getDescription()).isEqualTo("London");
        assertThat(response.get(0).getWelshDescription()).isNull();
        if (expectedRegions == 2) {
            assertThat(response.get(1).getRegionId()).isEqualTo("3");
            assertThat(response.get(1).getDescription()).isEqualTo("Midlands");
            assertThat(response.get(1).getWelshDescription()).isNull();
        }
    }

    private void responseVerificationForAll(List<LrdRegionResponse> actual, List<LrdRegionResponse> expected) {
        assertThat(actual).hasSize(expected.size()).usingRecursiveComparison().isEqualTo(expected);
    }

}
