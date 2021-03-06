package uk.gov.hmcts.reform.lrdapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceToCcdCaseTypeAssocRepositry;
import uk.gov.hmcts.reform.lrdapi.service.impl.FeatureToggleServiceImpl;
import uk.gov.hmcts.reform.lrdapi.util.KeyGenUtil;
import uk.gov.hmcts.reform.lrdapi.util.LrdApiClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.lrdapi.util.JwtTokenUtil.decodeJwtToken;
import static uk.gov.hmcts.reform.lrdapi.util.JwtTokenUtil.getUserIdAndRoleFromToken;

@Configuration
@TestPropertySource(properties = {"S2S_URL=http://127.0.0.1:8990", "IDAM_URL:http://127.0.0.1:5000"})
@DirtiesContext
public abstract class LrdAuthorizationEnabledIntegrationTest extends SpringBootIntegrationTest {

    @Autowired
    protected ServiceRepository serviceRepository;

    @Autowired
    protected ServiceToCcdCaseTypeAssocRepositry serviceToCcdCaseTypeAssocRepositry;

    @ClassRule
    public static WireMockRule s2sService = new WireMockRule(wireMockConfig().port(8990));

    @ClassRule
    public static WireMockRule idamService = new WireMockRule(5000);

    @ClassRule
    public static WireMockRule mockHttpServerForOidc = new WireMockRule(wireMockConfig().port(7000));


    protected LrdApiClient lrdApiClient;

    @MockBean
    protected FeatureToggleServiceImpl featureToggleService;

    @Value("${oidc.issuer}")
    private String issuer;

    @Value("${oidc.expiration}")
    private long expiration;

    @Before
    public void setUpClient() {
        when(featureToggleService.isFlagEnabled(anyString(), anyString())).thenReturn(true);
        lrdApiClient = new LrdApiClient(port, issuer, expiration);
    }

    @Before
    public void setUpIdamStubs() throws Exception {

        s2sService.stubFor(get(urlEqualTo("/details"))
                               .willReturn(aResponse()
                                               .withStatus(200)
                                               .withHeader("Content-Type", "application/json")
                                               .withBody("rd_location_ref_api")));

        idamService.stubFor(get(urlPathMatching("/o/userinfo"))
                                .willReturn(aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody("{"
                                                              + "  \"id\": \"%s\","
                                                              + "  \"uid\": \"%s\","
                                                              + "  \"forename\": \"Super\","
                                                              + "  \"surname\": \"User\","
                                                              + "  \"email\": \"super.user@hmcts.net\","
                                                              + "  \"accountStatus\": \"active\","
                                                              + "  \"roles\": ["
                                                              + "  \"%s\""
                                                              + "  ]"
                                                              + "}")
                                                .withTransformers("external_user-token-response")));

        mockHttpServerForOidc.stubFor(get(urlPathMatching("/jwks"))
                                          .willReturn(aResponse()
                                                          .withStatus(200)
                                                          .withHeader("Content-Type", "application/json")
                                                          .withBody(getDynamicJwksResponse())));
    }

    public static String getDynamicJwksResponse() throws JOSEException, JsonProcessingException {
        RSAKey rsaKey = KeyGenUtil.getRsaJwk();
        Map<String, List<JSONObject>> body = new LinkedHashMap<>();
        List<JSONObject> keyList = new ArrayList<>();
        keyList.add(rsaKey.toJSONObject());
        body.put("keys", keyList);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(body);
    }

    public static class ExternalTransformer extends ResponseTransformer {
        @Override
        public Response transform(Request request, Response response, FileSource files, Parameters parameters) {

            String formatResponse = response.getBodyAsString();

            String token = request.getHeader("Authorization");
            String tokenBody = decodeJwtToken(token.split(" ")[1]);
            LinkedList tokenInfo = getUserIdAndRoleFromToken(tokenBody);
            formatResponse = format(formatResponse, tokenInfo.get(1), tokenInfo.get(1), tokenInfo.get(0));

            return Response.Builder.like(response)
                .but().body(formatResponse)
                .build();
        }

        @Override
        public String getName() {
            return "external_user-token-response";
        }

        public boolean applyGlobally() {
            return false;
        }
    }


}

