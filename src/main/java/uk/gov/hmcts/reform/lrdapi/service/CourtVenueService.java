package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;

import java.util.Set;

public interface CourtVenueService {

    LrdCourtVenuesByServiceCodeResponse retrieveCourtVenuesByServiceCode(String serviceCode);

    Set<LrdCourtVenueResponse> retrieveCourtVenueDetails(String epimmsId);
}
