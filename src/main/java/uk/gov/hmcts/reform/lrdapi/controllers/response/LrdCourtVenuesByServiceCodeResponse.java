package uk.gov.hmcts.reform.lrdapi.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;

import java.util.List;

import static java.util.Objects.nonNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LrdCourtVenuesByServiceCodeResponse {

    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("court_type_id")
    protected String courtTypeId;

    @JsonProperty("court_type")
    protected String courtType;

    @JsonProperty("welsh_court_type")
    protected String welshCourtType;

    @JsonProperty("court_venues")
    private List<LrdCourtVenueResponse> courtVenues;

    public LrdCourtVenuesByServiceCodeResponse(CourtType courtType, String serviceCode) {

        if (nonNull(courtType)) {
            this.serviceCode = serviceCode;
            this.courtTypeId = courtType.getCourtTypeId();
            this.courtType = courtType.getTypeOfCourt();
            this.welshCourtType = courtType.getWelshCourtType();
            this.courtVenues = courtType.getCourtVenues()
                                        .stream()
                                        .map(LrdCourtVenueResponse::new)
                                        .toList();
        }
    }

    public LrdCourtVenuesByServiceCodeResponse(List<LrdCourtVenueResponse> courtVenues, String serviceCode) {
        this.serviceCode = serviceCode;
        this.courtVenues = courtVenues;
    }

}
