package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;

import java.util.List;


public interface RegionService {

    /**
     * Method to retrieve particular region(s) based on the given param (ID or Description).
     *
     * @param regionId    A String with the value of the Region ID(s).
     * @param description A String with the value of the Region description(s).
     * @return The the response object containing the details of the requested region(s).
     */
    List<LrdRegionResponse> retrieveRegionDetails(String regionId, String description);

    /**
     * Method to retrieve particular region(s) based on the given region description(s).
     *
     * @param description A String with the value of the Region description(s).
     * @return The the response object containing the details of the requested region(s).
     */
    List<LrdRegionResponse> retrieveRegionByRegionDescription(String description);

    /**
     * Method to retrieve particular region(s) based on the given region id(s).
     *
     * @param regionId A String with the value of the Region ID(s).
     * @return The the response object containing the details of the requested region(s).
     */
    List<LrdRegionResponse> retrieveRegionByRegionId(String regionId);
}
