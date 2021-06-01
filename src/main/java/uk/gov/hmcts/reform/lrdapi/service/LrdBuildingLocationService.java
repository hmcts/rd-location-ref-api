package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;

public interface LrdBuildingLocationService {

    /**
     * Method to retrieve the details of the requested building location.
     *
     * @param epimsId The epimm id of the building location
     * @return The the response object containing the details of the requested building location
     */
    LrdBuildingLocationResponse retrieveBuildingLocationByEpimsId(String epimsId);

}
