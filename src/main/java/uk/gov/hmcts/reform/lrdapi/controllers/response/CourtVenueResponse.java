package uk.gov.hmcts.reform.lrdapi.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;

import java.io.Serializable;

import static org.springframework.util.ObjectUtils.isEmpty;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CourtVenueResponse implements Serializable {

    @JsonProperty("court_venue_id")
    private String courtVenueId;

    @JsonProperty("site_name")
    private String siteName;

    @JsonProperty("court_name")
    private String courtName;

    @JsonProperty("epims_id")
    private String epimsId;

    @JsonProperty("open_for_public")
    private String openForPublic;

    @JsonProperty("court_type_id")
    private String courtType;

    @JsonProperty("region_id")
    private String regionId;

    private String region;

    @JsonProperty("court_status")
    private String courtStatus;

    @JsonProperty("court_open_date")
    private String courtOpenDate;

    @JsonProperty("cluster_id")
    private String clusterId;

    @JsonProperty("cluster_name")
    private String clusterName;

    @JsonProperty("closed_date")
    private String closedDate;

    private String postcode;

    @JsonProperty("court_address")
    private String courtAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("court_location_code")
    private String courtLocationCode;

    @JsonProperty("dx_address")
    private String dxAddress;

    @JsonProperty("welsh_site_name")
    private String welshSiteName;

    @JsonProperty("welsh_court_address")
    private String welshCourtAddress;

    public CourtVenueResponse(CourtVenue court) {

        if (!isEmpty(court)) {
            this.courtVenueId = court.getCourtVenueId().toString();
            court.getClosedDate().ifPresent(date -> closedDate = date.toString());
            this.courtAddress = court.getCourtAddress();
            this.courtLocationCode = court.getCourtLocationCode();
            this.courtName = court.getCourtName();
            this.courtStatus = court.getCourtStatus();
            court.getCourtOpenDate().ifPresent(date -> courtOpenDate = date.toString());
            court.getCluster().ifPresent(cluster -> {
                clusterId = cluster.getClusterId();
                clusterName = cluster.getClusterName();
            });
            this.courtType = court.getCourtTypeId();
            this.dxAddress = court.getDxAddress();
            this.epimsId = court.getEpimmsId();
            this.openForPublic = (court.getOpenForPublic()) ? "YES" : "NO";
            this.phoneNumber = court.getPhoneNumber();
            this.welshCourtAddress = court.getWelshCourtAddress();
            this.siteName = court.getSiteName();
            this.postcode = court.getPostcode();
            this.welshSiteName = court.getWelshSiteName();
            court.getRegion().ifPresent(reg -> {
                region = reg.getDescription();
                regionId = reg.getRegionId();
            });
        }

    }

}
