package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;

import java.util.List;

public interface ILrdBuildingLocationService {

    /**
     * Method to retrieve the details of the requested building location.
     *
     * @param epimsId A list of epimm id of the building location
     * @return The the response object containing the details of the requested building location
     */
    List<LrdBuildingLocationResponse> retrieveBuildingLocationByEpimsIds(List<String> epimsId);

    /**
     * Method to retrieve all the building locations.
     *
     * @return The the response object containing the list of all building locations
     */
    List<LrdBuildingLocationResponse> getAllBuildingLocations();
}
