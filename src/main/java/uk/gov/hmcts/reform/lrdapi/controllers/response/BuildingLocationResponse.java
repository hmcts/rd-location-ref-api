package uk.gov.hmcts.reform.lrdapi.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.lrdapi.domain.BuildingLocation;
import uk.gov.hmcts.reform.lrdapi.domain.Cluster;
import uk.gov.hmcts.reform.lrdapi.domain.Region;

@NoArgsConstructor
@Getter
public class BuildingLocationResponse {

    @JsonProperty
    private String buildingLocationId;
    @JsonProperty
    private String buildingLocationName;
    @JsonProperty
    private String epimmsId;
    @JsonProperty
    private String buildingLocationStatusId;
    @JsonProperty
    private String area;
    @JsonProperty
    private String region;
    @JsonProperty
    private String regionId;
    @JsonProperty
    private String clusterName;
    @JsonProperty
    private String clusterId;
    @JsonProperty
    private String courtFinderUrl;
    @JsonProperty
    private String postcode;
    @JsonProperty
    private String address;

    public BuildingLocationResponse (BuildingLocation buildingLocation, Cluster cluster, Region region) {
        this.buildingLocationId = buildingLocation.getBuildingLocationId();
        this.buildingLocationName = buildingLocation.getBuildingLocationId();
        this.epimmsId = buildingLocation.getEpimmsId();
        this.buildingLocationStatusId = buildingLocation.getBuildingLocationStatusId();
        this.area = buildingLocation.getArea();
        this.region = region.getDescription();
        this.regionId = buildingLocation.getRegionId();
        this.clusterName = cluster.getClusterName();
        this.clusterId = buildingLocation.getClusterId();
        this.postcode = buildingLocation.getPostcode();
        this.address = buildingLocation.getAddress();
    }


}
