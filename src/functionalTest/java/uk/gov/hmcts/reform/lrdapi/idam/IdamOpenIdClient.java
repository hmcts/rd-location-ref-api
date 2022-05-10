package uk.gov.hmcts.reform.lrdapi.idam;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.lib.idam.IdamOpenId;
import uk.gov.hmcts.reform.lrdapi.config.TestConfigProperties;

@Slf4j
public class IdamOpenIdClient extends IdamOpenId {

    private TestConfigProperties testConfig;

    private Gson gson = new Gson();

    private static String openIdTokenLrdAdmin;

    private static String sidamPassword;

    public IdamOpenIdClient(TestConfigProperties testConfig) {
        super(testConfig);
    }
}
