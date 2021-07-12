package uk.gov.hmcts.reform.lrdapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Integration")})
@SuppressWarnings("unchecked")
public class RetrieveCourtVenueDetailsIntegrationTest extends LrdAuthorizationEnabledIntegrationTest {

    @Test
    public void shouldReturn400WhenRegionIdIsString() throws
        JsonProcessingException {

        List<LrdCourtVenuenResponse> response = (List<LrdCourtVenuenResponse>)
            lrdApiClient.findCourtVenuesByGivenQueryParam("?region_id=123456789",
                                                               LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    public void shouldReturn400WhenClusterIdIsString() throws
        JsonProcessingException {

        List<LrdCourtVenuenResponse> response = (List<LrdCourtVenuenResponse>)
            lrdApiClient.findCourtVenuesByGivenQueryParam("?cluster_id=123456789",
                                                          LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }

    @Test
    public void shouldReturn400WhenCourtTypeIdIsString() throws
        JsonProcessingException {

        List<LrdCourtVenuenResponse> response = (List<LrdCourtVenuenResponse>)
            lrdApiClient.findCourtVenuesByGivenQueryParam("?cluster_id=123456789",
                                                          LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }


    @Test
    public void shouldReturn400WhenMultipleQueryParamsPassed() throws
        JsonProcessingException {

        List<LrdCourtVenuenResponse> response = (List<LrdCourtVenuenResponse>)
            lrdApiClient.findCourtVenuesByGivenQueryParam("?cluster_id=123456789",
                                                          LrdBuildingLocationResponse[].class);

        assertNotNull(response);
        responseVerification(response, ONE_STR);
    }
}
