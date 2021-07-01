package uk.gov.hmcts.reform.lrdapi.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

@NoArgsConstructor
@Getter
public class LrdRegionResponse {

    @JsonProperty("region_id")
    private String regionId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("welsh_description")
    private String welshDescription;

    public LrdRegionResponse(Region region) {
        this.regionId = region.getRegionId();
        this.description = region.getDescription();
        this.welshDescription = region.getWelshDescription();
    }

}
