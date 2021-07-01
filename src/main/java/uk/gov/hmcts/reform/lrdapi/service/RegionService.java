package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;


public interface RegionService {

    /**
     * Method to retrieve a particular region based on the given region description.
     *
     * @param description A String with the value of the Region description.
     * @return The the response object containing the details of the requested region.
     */
    LrdRegionResponse retrieveRegionByRegionDescription(String description);
}
