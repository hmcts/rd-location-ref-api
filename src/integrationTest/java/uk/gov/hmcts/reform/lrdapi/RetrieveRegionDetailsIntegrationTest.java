package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
public class RetrieveRegionDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    public static final String HTTP_STATUS = "http_status";

    List<LrdRegionResponse> expectedListAll = new ArrayList<>(Arrays.asList(
        new LrdRegionResponse(new Region("2", "London", null)),
        new LrdRegionResponse(new Region("3", "Midlands", null)),
        new LrdRegionResponse(new Region("4", "North East", null)),
        new LrdRegionResponse(new Region("5", "North West", null)),
        new LrdRegionResponse(new Region("6", "South East", null)),
        new LrdRegionResponse(new Region("7", "South West", null)),
        new LrdRegionResponse(new Region("8", "Wales", null)),
        new LrdRegionResponse(new Region("9", "Scotland", null))
    ));

    @Test
    public void returnsRegionDetailsByDescriptionWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("London", LrdRegionResponse[].class);

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    public void returnsRegionDetailsByDescriptionsWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("London, Midlands", LrdRegionResponse[].class);

        assertThat(response).isNotNull();
        responseVerification(response, 2);
    }

    @Test
    public void returnsRegionDetailsByDescriptionCaseInsensitiveWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("LoNdOn", LrdRegionResponse[].class);

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    public void returnsRegionDetailsByIdWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("2", LrdRegionResponse[].class);

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    public void returnsRegionDetailsByIdsWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("2, 3", LrdRegionResponse[].class);

        assertThat(response).isNotNull();
        responseVerification(response, 2);
    }

    @Test
    public void returnsRegionDetailsByIdAllWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("ALL", LrdRegionResponse[].class);

        assertThat(response).isNotNull();
        responseVerificationForAll(response, expectedListAll);
    }

    @Test
    public void returnsRegionDetailsById1AndAllWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("1, ALL", LrdRegionResponse[].class);

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(9);
        assertThat(response.get(0).getRegionId()).isEqualTo("1");
        assertThat(response.get(0).getDescription()).isEqualTo("National");
        assertThat(response.get(0).getWelshDescription()).isNull();
    }

    @Test
    public void doesNotReturnRegionDetailsByInvalidDescriptionWithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("Invalid Description", ErrorResponse.class);


        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
    }

    @Test
    public void doesNotReturnRegionByInvalidDescriptionSpecialCharWithStatusCode_400() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("Lo£nd*on", ErrorResponse.class);


        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void doesNotReturnRegionByInvalidIdnSpecialCharWithStatusCode_400() throws JsonProcessingException {

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
