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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
public class RetrieveRegionDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    public static final String HTTP_STATUS = "http_status";

    @Test
    public void returnsRegionDetailsByDescriptionWithStatusCode200() throws JsonProcessingException {

        LrdRegionResponse response = (LrdRegionResponse)
            lrdApiClient.findRegionDetailsByDescription("London", LrdRegionResponse.class);

        assertThat(response).isNotNull();
        responseVerification(response);
    }

    @Test
    public void doesNotReturnRegionDetailsByInvalidDescriptionWithStatusCode_404() throws JsonProcessingException {

        Map<String, Object> errorResponseMap = (Map<String, Object>)
            lrdApiClient.findRegionDetailsByDescription("Invalid Description", ErrorResponse.class);


        assertThat(errorResponseMap).containsEntry(HTTP_STATUS, HttpStatus.NOT_FOUND);
    }

    private void responseVerification(LrdRegionResponse response) {
        assertThat(response.getRegionId()).isEqualTo("2");
        assertThat(response.getDescription()).isEqualTo("London");
        assertThat(response.getWelshDescription()).isNull();
    }

}
