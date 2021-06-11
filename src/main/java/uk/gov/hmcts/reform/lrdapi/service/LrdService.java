package uk.gov.hmcts.reform.lrdapi.service;

import uk.gov.hmcts.reform.lib.audit.domain.RowDomain;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;

import java.util.List;

public interface LrdService {

    List<LrdOrgInfoServiceResponse> retrieveOrgServiceDetails(String serviceCode,
                                                              String ccdCode, String ccdServiceNames);

    void createLocations(List<RowDomain> lrdRecords, Boolean isBuildingLocation);
}
