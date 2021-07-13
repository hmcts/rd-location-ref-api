package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;

public interface CourtVenueService {

    LrdCourtVenuesByServiceCodeResponse retrieveCourtVenuesByServiceCode(String serviceCode);
}
