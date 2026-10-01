package uk.gov.hmcts.reform.lrdapi.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.lrdapi.domain.CourtVenue;

import java.io.Serializable;

import static java.util.Objects.nonNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LrdCourtVenueResponse implements Serializable {

    @JsonProperty("court_venue_id")
    private String courtVenueId;

    @JsonProperty("epimms_id")
    private String epimmsId;

    @JsonProperty("service_code")
    private String serviceCode;

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

    @JsonProperty("venue_name")
    private String venueName;

    @JsonProperty("is_case_management_location")
    private String isCaseManagementLocation;

    @JsonProperty("is_hearing_location")
    private String isHearingLocation;

    @JsonProperty("welsh_venue_name")
    String welshVenueName;

    @JsonProperty("is_temporary_location")
    String isTemporaryLocation;

    @JsonProperty("is_nightingale_court")
    String isNightingaleCourt;

    @JsonProperty("location_type")
    String locationType;

    @JsonProperty("parent_location")
    String parentLocation;

    @JsonProperty("welsh_court_name")
    private String welshCourtName;

    @JsonProperty("uprn")
    private String uprn;

    @JsonProperty("venue_ou_code")
    private String venueOuCode;

    @JsonProperty("mrd_building_location_id")
    private String mrdBuildingLocationId;

    @JsonProperty("mrd_venue_id")
    private String mrdVenueId;

    @JsonProperty("service_url")
    private String serviceUrl;

    @JsonProperty("fact_url")
    private String factUrl;

    @JsonProperty("external_short_name")
    private String externalShortName;

    @JsonProperty("welsh_external_short_name")
    private String welshExternalShortName;

    @JsonProperty("court_status_code")
    private String courtStatusCode;

    @JsonProperty("open_date")
    private String openDate;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("district_registry_venue_id")
    private String districtRegistryVenueId;

    @JsonProperty("appeal_centre_venue_id")
    private String appealCentreVenueId;

    @JsonProperty("effective_from_date")
    private String effectiveFromDate;

    @JsonProperty("effective_to_date")
    private String effectiveToDate;

    @JsonProperty("mrd_created_time")
    private String mrdCreatedTime;

    @JsonProperty("mrd_updated_time")
    private String mrdUpdatedTime;

    @JsonProperty("mrd_deleted_time")
    private String mrdDeletedTime;

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
            this.courtType = courtVenue.getCourtType().getTypeOfCourt();
            this.dxAddress = courtVenue.getDxAddress();
            this.epimmsId = courtVenue.getEpimmsId();
            this.serviceCode = courtVenue.getServiceCode();
            this.openForPublic = Boolean.TRUE.equals(courtVenue.getOpenForPublic()) ? "YES" : "NO";
            this.phoneNumber = courtVenue.getPhoneNumber();
            this.welshCourtAddress = courtVenue.getWelshCourtAddress();
            this.siteName = courtVenue.getSiteName();
            this.postcode = courtVenue.getPostcode();
            this.welshSiteName = courtVenue.getWelshSiteName();
            this.venueName = courtVenue.getVenueName();
            this.isCaseManagementLocation = courtVenue.getIsCaseManagementLocation();
            this.isHearingLocation = courtVenue.getIsHearingLocation();
            this.welshVenueName = courtVenue.getWelshVenueName();
            this.isTemporaryLocation = courtVenue.getIsTemporaryLocation();
            this.isNightingaleCourt = courtVenue.getIsNightingaleCourt();
            this.locationType = courtVenue.getLocationType();
            this.parentLocation = courtVenue.getParentLocation();
            this.welshCourtName = courtVenue.getWelshCourtName();
            this.uprn = courtVenue.getUprn();
            this.venueOuCode = courtVenue.getVenueOuCode();
            this.mrdBuildingLocationId = courtVenue.getMrdBuildingLocationId();
            this.mrdVenueId = courtVenue.getMrdVenueId();
            this.serviceUrl = courtVenue.getServiceUrl();
            this.factUrl = courtVenue.getFactUrl();
            this.externalShortName = courtVenue.getExternalShortName();
            this.welshExternalShortName = courtVenue.getWelshExternalShortName();
            this.courtStatusCode = courtVenue.getCourtStatusCode();
            if (nonNull(courtVenue.getOpenDate())) {
                this.openDate = courtVenue.getOpenDate().toString();
            }
            this.parentId = courtVenue.getParentId();
            this.districtRegistryVenueId = courtVenue.getDistrictRegistryVenueId();
            this.appealCentreVenueId = courtVenue.getAppealCentreVenueId();
            if (nonNull(courtVenue.getEffectiveFromDate())) {
                this.effectiveFromDate = courtVenue.getEffectiveFromDate().toString();
            }
            if (nonNull(courtVenue.getEffectiveToDate())) {
                this.effectiveToDate = courtVenue.getEffectiveToDate().toString();
            }
            if (nonNull(courtVenue.getMrdCreatedTime())) {
                this.mrdCreatedTime = courtVenue.getMrdCreatedTime().toString();
            }
            if (nonNull(courtVenue.getMrdUpdatedTime())) {
                this.mrdUpdatedTime = courtVenue.getMrdUpdatedTime().toString();
            }
            if (nonNull(courtVenue.getMrdDeletedTime())) {
                this.mrdDeletedTime = courtVenue.getMrdDeletedTime().toString();
            }
            courtVenue.getRegion().ifPresent(reg -> {
                region = reg.getDescription();
                regionId = reg.getRegionId();
            });
        }
    }

}
