package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;

import java.util.List;


public interface RegionService {

    Object retrieveRegionDetails(String regionId, String description);

    /**
     * Method to retrieve a particular region based on the given region description.
     *
     * @param description A String with the value of the Region description.
     * @return The the response object containing the details of the requested region.
     */
    List<LrdRegionResponse> retrieveRegionByRegionDescription(String description);

    /**
     * Method to retrieve a particular region based on the given region id.
     *
     * @param regionId A String with the value of the Region ID.
     * @return The the response object containing the details of the requested region.
     */
    List<LrdRegionResponse> retrieveRegionByRegionId(String regionId);
}
