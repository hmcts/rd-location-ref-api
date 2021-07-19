package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;

import java.util.List;

public interface CourtVenueService {

    LrdCourtVenuesByServiceCodeResponse retrieveCourtVenuesByServiceCode(String serviceCode);

    List<LrdCourtVenueResponse> retrieveCourtVenueDetails(String epimmsId, Integer courtTypeId, Integer regionId,
                                                          Integer clusterId);
}
