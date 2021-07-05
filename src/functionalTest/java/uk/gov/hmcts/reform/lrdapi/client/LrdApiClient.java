package uk.gov.hmcts.reform.lrdapi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.idam.IdamOpenIdClient;

import java.io.IOException;
import java.util.Arrays;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class LrdApiClient {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String SERVICE_HEADER = "ServiceAuthorization";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASE_URL = "/refdata/location";

    private final String lrdApiUrl;
    private  String s2sToken;
    private  IdamOpenIdClient idamOpenIdClient;


    public LrdApiClient(
        String lrdApiUrl,
        String s2sToken,
        IdamOpenIdClient idamOpenIdClient) {
        this. lrdApiUrl = lrdApiUrl;
        this.idamOpenIdClient = idamOpenIdClient;
        this.s2sToken = s2sToken;
    }

    public Object retrieveOrgServiceInfo(HttpStatus expectedStatus, String param) {
        Response response = getMultipleAuthHeaders()
            .get(BASE_URL + "/orgServices" + param)
            .andReturn();

        response.then()
            .assertThat()
            .statusCode(expectedStatus.value());
        if (expectedStatus.is2xxSuccessful()) {
            return Arrays.asList(response.getBody().as(LrdOrgInfoServiceResponse[].class));
        } else {
            return response.getBody().as(ErrorResponse.class);
        }
    }

    public Object retrieveBuildingLocationDetailsByGivenQueryParam(HttpStatus expectedStatus, String param,
                                                                   Class<?> clazz) {
        String queryParam = "";
        if (!isEmpty(param)) {
            queryParam = param;
        }
        Response response = getMultipleAuthHeaders()
            .get(BASE_URL + "/building-locations" + queryParam)
            .andReturn();

        response.then()
            .assertThat()
            .statusCode(expectedStatus.value());
        if (expectedStatus.is2xxSuccessful()) {
            if (clazz.isArray()) {
                return Arrays.asList(response.getBody().as(clazz));
            } else {
                return response.getBody().as(clazz);
            }
        } else {
            return response.getBody().as(ErrorResponse.class);
        }
    }

    public Object retrieveRegionInfoByRegionDescription(HttpStatus expectedStatus, String param) {
        Response response = getMultipleAuthHeaders()
            .get(BASE_URL + "/regions?region=" + param)
            .andReturn();

        response.then()
            .assertThat()
            .statusCode(expectedStatus.value());
        if (expectedStatus.is2xxSuccessful()) {
            return response.getBody().as(LrdRegionResponse[].class);
        } else {
            return response.getBody().as(ErrorResponse.class);
        }
    }

    public Object retrieveRegionInfoByRegionId(HttpStatus expectedStatus, String param) {
        Response response = getMultipleAuthHeaders()
            .get(BASE_URL + "/regions?regionId=" + param)
            .andReturn();

        response.then()
            .assertThat()
            .statusCode(expectedStatus.value());
        if (expectedStatus.is2xxSuccessful()) {
            return response.getBody().as(LrdRegionResponse.class);
        } else {
            return response.getBody().as(ErrorResponse.class);
        }
    }

    public Response retrieveBuildingLocationDetailsByGivenQueryParam_NoBearerToken(String param) {
        Response response = withUnauthenticatedRequest_NoBearerToken()
            .get(BASE_URL + "/building-locations" + param)
            .andReturn();

        return response;
    }

    public Response retrieveBuildingLocationDetailsByGivenQueryParam_NoS2SToken(String param) {
        Response response = withUnauthenticatedRequest_NoS2SToken()
            .get(BASE_URL + "/building-locations" + param)
            .andReturn();

        return response;
    }

    public String getWelcomePage() {
        return withUnauthenticatedRequest()
            .get("/")
            .then()
            .statusCode(OK.value())
            .and()
            .extract()
            .response()
            .body()
            .asString();
    }

    public RequestSpecification withUnauthenticatedRequest() {
        return SerenityRest.given()
            .relaxedHTTPSValidation()
            .baseUri(lrdApiUrl)
            .header("Content-Type", APPLICATION_JSON_VALUE)
            .header("Accepts", APPLICATION_JSON_VALUE);
    }

    public RequestSpecification withUnauthenticatedRequest_NoBearerToken() {
        return SerenityRest.given()
            .relaxedHTTPSValidation()
            .baseUri(lrdApiUrl)
            .header("Content-Type", APPLICATION_JSON_VALUE)
            .header("Accepts", APPLICATION_JSON_VALUE)
            .header(SERVICE_HEADER, "Bearer " + s2sToken);
    }

    private RequestSpecification withUnauthenticatedRequest_NoS2SToken() {
        return SerenityRest.with()
            .relaxedHTTPSValidation()
            .baseUri(lrdApiUrl)
            .header("Content-Type", APPLICATION_JSON_VALUE)
            .header("Accepts", APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION_HEADER, "Bearer " + idamOpenIdClient.getOpenIdToken());
    }

    public RequestSpecification getMultipleAuthHeaders() {
        return SerenityRest.with()
            .relaxedHTTPSValidation()
            .baseUri(lrdApiUrl)
            .header("Content-Type", APPLICATION_JSON_VALUE)
            .header("Accepts", APPLICATION_JSON_VALUE)
            .header(SERVICE_HEADER, "Bearer " + s2sToken)
            .header(AUTHORIZATION_HEADER, "Bearer " + idamOpenIdClient.getOpenIdToken());
    }

    @SuppressWarnings("unused")
    private JsonNode parseJson(String jsonString) throws IOException {
        return mapper.readTree(jsonString);
    }
}
