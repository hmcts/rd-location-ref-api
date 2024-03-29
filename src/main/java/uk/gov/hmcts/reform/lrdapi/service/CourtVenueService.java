package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenueResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenueRequestParam;

import java.util.List;

public interface CourtVenueService {

    LrdCourtVenuesByServiceCodeResponse retrieveCourtVenuesByServiceCode(String serviceCode);

    /**
     * Method to retrieve the court venues for the request provided.
     *
     * @param epimmsId              list of epimm id of building location
     * @param courtTypeId           court type identifier
     * @param regionId              region identifier
     * @param clusterId             cluster identifier
     * @param epimmsIdwithCourtType api with epimmsId with court Type
     * @return list of court venues
     */
    List<LrdCourtVenueResponse> retrieveCourtVenueDetails(String epimmsId, Integer courtTypeId, Integer regionId,
                                                          Integer clusterId, String courtVenueName,
                                                          boolean epimmsIdwithCourtType,
                                                          CourtVenueRequestParam courtVenueRequestParam);

    List<LrdCourtVenueResponse> retrieveCourtVenuesBySearchString(String searchString, String courtTypeId,
                                                                  CourtVenueRequestParam requestParam);
}
