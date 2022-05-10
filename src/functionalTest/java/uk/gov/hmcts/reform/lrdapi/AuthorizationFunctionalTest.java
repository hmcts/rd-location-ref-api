package uk.gov.hmcts.reform.lrdapi;

import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.lib.client.response.S2sClient;
import uk.gov.hmcts.reform.lrdapi.client.LrdApiClient;
import uk.gov.hmcts.reform.lrdapi.config.Oauth2;
import uk.gov.hmcts.reform.lrdapi.config.TestConfigProperties;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ErrorResponse;
import uk.gov.hmcts.reform.lrdapi.idam.IdamOpenIdClient;

import javax.annotation.PostConstruct;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {TestConfigProperties.class, Oauth2.class})
@ComponentScan("uk.gov.hmcts.reform.lrdapi")
@TestPropertySource("classpath:application-functional.yaml")
@Slf4j
public abstract class AuthorizationFunctionalTest {

    protected  LrdApiClient lrdApiClient;

    protected static IdamOpenIdClient idamOpenIdClient;

    public static final String EMAIL_TEMPLATE = "freg-test-user-%s@prdfunctestuser.com";

    @Autowired
    protected TestConfigProperties testConfigProperties;

    protected static  String  s2sToken;

    public static final String EMAIL = "EMAIL";

    public static final String CREDS = "CREDS";

    @PostConstruct
    public void setup() {

        SerenityRest.useRelaxedHTTPSValidation();
        SerenityRest.setDefaultParser(Parser.JSON);

        log.info("Configured S2S secret: " + testConfigProperties.getS2sSecret().substring(0, 2) + "************"
                     + testConfigProperties.getS2sSecret().substring(14));
        log.info("Configured S2S microservice: " + testConfigProperties.getS2sName());
        log.info("Configured S2S URL: " + testConfigProperties.getS2sSecret());

        /*SerenityRest.proxy("proxyout.reform.hmcts.net", 8080);
        RestAssured.proxy("proxyout.reform.hmcts.net", 8080);*/

        if (null == s2sToken) {
            s2sToken = new S2sClient(
                testConfigProperties.getS2sUrl(),
                testConfigProperties.getS2sName(),
                testConfigProperties.getS2sSecret())
                .signIntoS2S();
        }
        if (null == idamOpenIdClient) {
            idamOpenIdClient = new IdamOpenIdClient(testConfigProperties);
        }

        lrdApiClient = new LrdApiClient(testConfigProperties.getLrdApiUrl(), s2sToken, idamOpenIdClient);

    }

    public static String generateRandomEmail() {
        return String.format(EMAIL_TEMPLATE, randomAlphanumeric(10));
    }

    public void validateErrorResponse(ErrorResponse errorResponse, String expectedErrorMessage,
                               String expectedErrorDescription) {
        assertEquals(expectedErrorDescription, errorResponse.getErrorDescription());
        assertEquals(expectedErrorMessage, errorResponse.getErrorMessage());
    }

}
