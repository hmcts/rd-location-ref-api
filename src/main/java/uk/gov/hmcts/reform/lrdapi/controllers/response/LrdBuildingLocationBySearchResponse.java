package uk.gov.hmcts.reform.lrdapi.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LrdBuildingLocationBySearchResponse implements Serializable {

    @JsonProperty("epimms_id")
    private String epimmsId;

    @JsonProperty
    private String area;

    @JsonProperty("building_location_name")
    private String buildingLocationName;

    @JsonProperty
    private String region;

    @JsonProperty("building_location_status")
    private String buildingLocationStatus;

    @JsonProperty("region_id")
    private String regionId;

    @JsonProperty("cluster_id")
    private String clusterId;

    @JsonProperty("cluster_name")
    private String clusterName;

    @JsonProperty("court_finder_url")
    private String courtFinderUrl;

    @JsonProperty
    private String address;

    @JsonProperty
    private String postcode;


}
