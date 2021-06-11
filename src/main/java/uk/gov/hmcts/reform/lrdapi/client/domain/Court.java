package uk.gov.hmcts.reform.lrdapi.client.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.lib.audit.domain.RowDomain;
import uk.gov.hmcts.reform.lib.util.MappingField;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COURT_ADDRESS_MISSING;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COURT_LOCATION_NAME_MISSING;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EPIMS_ID_MISSING;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.OPEN_FOR_PUBLIC_MISSING;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.POSTCODE_MISSING;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Court extends RowDomain implements Serializable {

    @MappingField(columnName = "ePIMS_ID")
    @NotNull(message = EPIMS_ID_MISSING)
    private Integer epimsId;

    @MappingField(columnName = "Court_Location_Name")
    @NotEmpty(message = COURT_LOCATION_NAME_MISSING)
    private String courtLocationName;

    @MappingField(columnName = "Region_ID")
    private Integer regionId;

    @MappingField(columnName = "Court_Location_Category_ID")
    private Integer courtLocationCategoryId;

    @MappingField(columnName = "Cluster_ID")
    private Integer clusterId;

    @MappingField(columnName = "Open_For_Public")
    @NotEmpty(message = OPEN_FOR_PUBLIC_MISSING)
    private String openForPublic;

    @MappingField(columnName = "Court_Address")
    @NotEmpty(message = COURT_ADDRESS_MISSING)
    private String courtAddress;

    @MappingField(columnName = "Postcode")
    @NotEmpty(message = POSTCODE_MISSING)
    private String postcode;

    @MappingField(columnName = "Phone_Number")
    private String phoneNumber;

    @MappingField(columnName = "Closed_Date")
    private LocalDateTime closedDate;

    @MappingField(columnName = "Court_Location_Code")
    private String courtLocationCode;

    @MappingField(columnName = "Dx_Address")
    private String dxAddress;

    @MappingField(columnName = "Welsh_Court_Location_Name")
    private String welshCourtLocationName;

    @MappingField(columnName = "Welsh_Court_Address")
    private String welshCourtAddress;

}
