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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
public class RetrieveRegionDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    public static final String HTTP_STATUS = "http_status";

    @Test
    public void returnsRegionDetailsByDescriptionWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("London", LrdRegionResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    public void returnsRegionDetailsByDescriptionsWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("London, Midlands", LrdRegionResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response, 2);
    }

    @Test
    public void returnsRegionDetailsByDescriptionAllWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("ALL", LrdRegionResponse.class);

        assertThat(response).isNotNull();
        responseVerificationForAll(response);
    }

    @Test
    public void returnsRegionDetailsByDescriptionCaseInsensitiveWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsByDescription("LoNdOn", LrdRegionResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    public void returnsRegionDetailsByIdWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("1", LrdRegionResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response, 1);
    }

    @Test
    public void returnsRegionDetailsByIdsWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("1, 2", LrdRegionResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response, 2);
    }

    @Test
    public void returnsRegionDetailsByIdAllWithStatusCode_200() throws JsonProcessingException {

        List<LrdRegionResponse> response = (List<LrdRegionResponse>)
            lrdApiClient.findRegionDetailsById("ALL", LrdRegionResponse.class);

        assertThat(response).isNotNull();
        responseVerificationForAll(response);
    }

    @Test
    public void doesNotReturnRegionDetailsByInvalidDescriptionWithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("Invalid Description", ErrorResponse.class);


        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
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

    private void responseVerificationForAll(List<LrdRegionResponse> response) {
        assertThat(response.size()).isEqualTo(8);
        assertThat(response.get(0).getRegionId()).isEqualTo("2");
        assertThat(response.get(0).getDescription()).isEqualTo("London");
        assertThat(response.get(0).getWelshDescription()).isNull();
        assertThat(response.get(1).getRegionId()).isEqualTo("3");
        assertThat(response.get(1).getDescription()).isEqualTo("Midlands");
        assertThat(response.get(1).getWelshDescription()).isNull();
        assertThat(response.get(0).getRegionId()).isEqualTo("4");
        assertThat(response.get(0).getDescription()).isEqualTo("North East");
        assertThat(response.get(0).getWelshDescription()).isNull();
        assertThat(response.get(0).getRegionId()).isEqualTo("5");
        assertThat(response.get(0).getDescription()).isEqualTo("North West");
        assertThat(response.get(0).getWelshDescription()).isNull();
        assertThat(response.get(1).getRegionId()).isEqualTo("6");
        assertThat(response.get(1).getDescription()).isEqualTo("South East");
        assertThat(response.get(1).getWelshDescription()).isNull();
        assertThat(response.get(0).getRegionId()).isEqualTo("7");
        assertThat(response.get(0).getDescription()).isEqualTo("South West");
        assertThat(response.get(0).getWelshDescription()).isNull();
        assertThat(response.get(0).getRegionId()).isEqualTo("8");
        assertThat(response.get(0).getDescription()).isEqualTo("Wales");
        assertThat(response.get(0).getWelshDescription()).isNull();
        assertThat(response.get(1).getRegionId()).isEqualTo("9");
        assertThat(response.get(1).getDescription()).isEqualTo("Scotland");
        assertThat(response.get(1).getWelshDescription()).isNull();
    }

}
