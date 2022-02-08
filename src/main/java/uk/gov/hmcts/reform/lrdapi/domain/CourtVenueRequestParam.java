package uk.gov.hmcts.reform.lrdapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class CourtVenueRequestParam {

    private String isCaseManagementLocation;
    private String isHearingLocation;
    private String locationType;
    private String isTemporaryLocation;



}
