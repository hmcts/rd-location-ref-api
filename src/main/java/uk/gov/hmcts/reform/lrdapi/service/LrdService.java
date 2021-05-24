package uk.gov.hmcts.reform.lrdapi.service;

import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.lrdapi.controllers.response.BuildingLocationResponse;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;

import java.util.List;

public interface LrdService {

    List<LrdOrgInfoServiceResponse> retrieveOrgServiceDetails(String serviceCode,
                                                              String ccdCode, String ccdServiceNames);

    ResponseEntity<BuildingLocationResponse> retrieveBuildingLocationByEpimsId(String epimsId);
}
