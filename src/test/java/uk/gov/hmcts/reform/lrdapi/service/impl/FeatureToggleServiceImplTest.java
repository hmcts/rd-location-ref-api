package uk.gov.hmcts.reform.lrdapi.service.impl;

import com.launchdarkly.sdk.server.LDClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class FeatureToggleServiceImplTest {

    LDClient ldClient = mock(LDClient.class);
    FeatureToggleServiceImpl flaFeatureToggleService = mock(FeatureToggleServiceImpl.class);

    @Test
    void testIsFlagEnabled() {
        flaFeatureToggleService = new FeatureToggleServiceImpl(ldClient, "rd");
        assertFalse(flaFeatureToggleService.isFlagEnabled("lrd", "lrd-get-org-services-flag-mock"));
    }

    @Test
    void mapServiceToFlagTest() {
        flaFeatureToggleService = new FeatureToggleServiceImpl(ldClient, "rd");
        flaFeatureToggleService.mapServiceToFlag();
        assertTrue(flaFeatureToggleService.getLaunchDarklyMap().size() >= 1);
    }
}
