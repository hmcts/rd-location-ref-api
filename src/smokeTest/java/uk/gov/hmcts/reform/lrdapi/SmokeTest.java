package uk.gov.hmcts.reform.lrdapi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Smoke")})
@Slf4j
public class SmokeTest {

    // use this when testing locally - replace the below content with this line
    private final String targetInstance =
        StringUtils.defaultIfBlank(
            System.getenv("TEST_URL"),
            "http://localhost:8099"
        );

    @Test
    public void test_should_prove_app_is_running_and_healthy() {
        // local test
        /*SerenityRest.proxy("proxyout.reform.hmcts.net", 8080);
        RestAssured.proxy("proxyout.reform.hmcts.net", 8080);*/

        RestAssured.baseURI = targetInstance;
        RestAssured.useRelaxedHTTPSValidation();

        SerenityRest
            .given()
            .relaxedHTTPSValidation()
            .get("/health")
            .then()
            .statusCode(200);
    }
}
