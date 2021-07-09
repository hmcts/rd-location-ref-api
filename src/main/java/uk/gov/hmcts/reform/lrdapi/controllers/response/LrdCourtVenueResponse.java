package uk.gov.hmcts.reform.lrdapi.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;

import static java.util.Objects.nonNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LrdCourtVenueResponse {

    @JsonProperty("court_venue_id")
    private String courtVenueId;

    @JsonProperty("epims_id")
    private String epimsId;

    @JsonProperty("site_name")
    private String siteName;

    @JsonProperty("region_id")
    private String regionId;

    @JsonProperty("region")
    private String region;

    @JsonProperty("court_type")
    private String courtType;

    @JsonProperty("court_type_id")
    private String courtTypeId;

    @JsonProperty("cluster_id")
    private String clusterId;

    @JsonProperty("cluster_name")
    private String clusterName;

    @JsonProperty("open_for_public")
    private String openForPublic;

    @JsonProperty("court_address")
    private String courtAddress;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("closed_date")
    private String closedDate;

    @JsonProperty("court_location_code")
    private String courtLocationCode;

    @JsonProperty("dx_address")
    private String dxAddress;

    @JsonProperty("welsh_site_name")
    private String welshSiteName;

    @JsonProperty("welsh_court_address")
    private String welshCourtAddress;

    @JsonProperty("court_status")
    private String courtStatus;

    @JsonProperty("court_open_date")
    private String courtOpenDate;

    @JsonProperty("court_name")
    private String courtName;

    public LrdCourtVenueResponse(CourtVenue courtVenue) {

        if (nonNull(courtVenue)) {
            this.courtVenueId = courtVenue.getCourtVenueId().toString();
            courtVenue.getClosedDate().ifPresent(date -> closedDate = date.toString());
            this.courtAddress = courtVenue.getCourtAddress();
            this.courtLocationCode = courtVenue.getCourtLocationCode();
            this.courtName = courtVenue.getCourtName();
            this.courtStatus = courtVenue.getCourtStatus();
            courtVenue.getCourtOpenDate().ifPresent(date -> courtOpenDate = date.toString());
            courtVenue.getCluster().ifPresent(cluster -> {
                clusterId = cluster.getClusterId();
                clusterName = cluster.getClusterName();
            });
            this.courtTypeId = courtVenue.getCourtType().getCourtTypeId();
            this.courtType = courtVenue.getCourtType().getCourtType();
            this.dxAddress = courtVenue.getDxAddress();
            this.epimsId = courtVenue.getBuildingLocation().getEpimmsId();
            this.openForPublic = (courtVenue.getOpenForPublic()) ? "YES" : "NO";
            this.phoneNumber = courtVenue.getPhoneNumber();
            this.welshCourtAddress = courtVenue.getWelshCourtAddress();
            this.siteName = courtVenue.getSiteName();
            this.postcode = courtVenue.getPostcode();
            this.welshSiteName = courtVenue.getWelshSiteName();
            courtVenue.getRegion().ifPresent(reg -> {
                region = reg.getDescription();
                regionId = reg.getRegionId();
            });
        }
    }

}
