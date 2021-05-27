package uk.gov.hmcts.reform.lrdapi.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LrdBuildingLocationResponse {

    @JsonProperty("building_location_id")
    private String buildingLocationId;

    @JsonProperty("building_location_name")
    private String buildingLocationName;

    @JsonProperty("epims_id")
    private String epimmsId;

    @JsonProperty("building_location_status")
    private String buildingLocationStatus;

    @JsonProperty
    private String area;

    @JsonProperty
    private String region;

    @JsonProperty("region_id")
    private String regionId;

    @JsonProperty("cluster_name")
    private String clusterName;

    @JsonProperty("cluster_id")
    private String clusterId;

    @JsonProperty("court_finder_url")
    private String courtFinderUrl;

    @JsonProperty
    private String postcode;

    @JsonProperty
    private String address;

}
