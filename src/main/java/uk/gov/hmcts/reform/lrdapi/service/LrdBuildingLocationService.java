package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdBuildingLocationResponse;

public interface LrdBuildingLocationService {

    /**
     *
     * @param epimsId
     * @return
     */
    LrdBuildingLocationResponse retrieveBuildingLocationByEpimsId(String epimsId);

}
