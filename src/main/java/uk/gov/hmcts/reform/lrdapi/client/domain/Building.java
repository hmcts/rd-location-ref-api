package uk.gov.hmcts.reform.lrdapi.client.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.lib.audit.domain.RowDomain;
import uk.gov.hmcts.reform.lib.util.MappingField;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.BUILDING_ADDRESS_MISSING;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.BUILDING_LOCATION_NAME_MISSING;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EPIMS_ID_MISSING;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.POSTCODE_MISSING;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Building extends RowDomain implements Serializable {

    @MappingField(columnName = "ePIMS_ID")
    @NotEmpty(message = EPIMS_ID_MISSING)
    private String epimsId;

    @MappingField(columnName = "Building_Location_Name")
    @NotEmpty(message = BUILDING_LOCATION_NAME_MISSING)
    private String buildingLocationName;

    @MappingField(columnName = "Status ID")
    private String statusId;

    @MappingField(columnName = "Area")
    private String area;

    @MappingField(columnName = "Region_ID")
    private String regionId;

    @MappingField(columnName = "Cluster_ID")
    private String clusterId;

    @MappingField(columnName = "Court_Finder_URL")
    private String courtFinderUrl;

    @MappingField(columnName = "Postcode")
    @NotEmpty(message = POSTCODE_MISSING)
    private String postcode;

    @MappingField(columnName = "Address")
    @NotEmpty(message = BUILDING_ADDRESS_MISSING)
    private String address;

}
