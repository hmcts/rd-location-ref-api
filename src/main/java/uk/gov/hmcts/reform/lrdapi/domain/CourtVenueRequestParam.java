package uk.gov.hmcts.reform.lrdapi.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@Setter
@Builder
public class CourtVenueRequestParam {

    private String isCaseManagementLocation;
    private String isHearingLocation;
    private String locationType;
    private String isTemporaryLocation;


}
