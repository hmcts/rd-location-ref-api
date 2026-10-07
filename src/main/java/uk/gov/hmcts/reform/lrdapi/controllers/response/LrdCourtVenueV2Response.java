package uk.gov.hmcts.reform.lrdapi.controllers.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LrdCourtVenueV2Response implements Serializable {

    @JsonProperty("mrd_venue_id")
    private String mrdVenueId;

    @JsonProperty("epims_id")
    private String epimsId;

    @JsonProperty("mrd_building_id")
    private String mrdBuildingId;

    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("court_status")
    private String courtStatus;

    @JsonProperty("open_date")
    private String openDate;

    @JsonProperty("closed_date")
    private String closedDate;

    @JsonProperty("open_for_public")
    private String openForPublic;

    @JsonProperty("dx_address")
    private String dxAddress;

    @JsonProperty("region")
    private String region;

    @JsonProperty("cluster")
    private String cluster;

    @JsonProperty("location_type")
    private String locationType;

    @JsonProperty("court_use")
    private CourtUse courtUse;

    @JsonProperty("parent_location")
    private String parentLocation;

    @JsonProperty("parent_venue_id")
    private String parentVenueId;

    @JsonProperty("district_registry_venue_id")
    private String districtRegistryVenueId;

    @JsonProperty("appeal_centre_venue_id")
    private String appealCentreVenueId;

    @JsonProperty("venue_ou_code")
    private String venueOuCode;

    @JsonProperty("service_url")
    private String serviceUrl;

    @JsonProperty("fact_url")
    private String factUrl;

    @JsonProperty("names")
    private List<Name> names;

    @JsonProperty("addresses")
    private List<Address> addresses;

    @JsonProperty("contacts")
    private List<Contact> contacts;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class CourtUse implements Serializable {

        @JsonProperty("is_hearing_location")
        private boolean hearingLocation;

        @JsonProperty("is_case_mgmt_location")
        private boolean caseManagementLocation;

        @JsonProperty("is_district_registry")
        private boolean districtRegistry;

        @JsonProperty("is_temporary_location")
        private boolean temporaryLocation;

        @JsonProperty("is_nightingale_court")
        private boolean nightingaleCourt;

        @JsonProperty("is_appeal_centre")
        private boolean appealCentre;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Name implements Serializable {

        @JsonProperty("type")
        private String type;

        @JsonProperty("language")
        private String language;

        @JsonProperty("name")
        private String name;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Address implements Serializable {

        @JsonProperty("type")
        private String type;

        @JsonProperty("address")
        private String address;

        @JsonProperty("post_code")
        private String postCode;

        @JsonProperty("uprn")
        private String uprn;

        @JsonProperty("language")
        private String language;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Contact implements Serializable {

        @JsonProperty("method")
        private String method;

        @JsonProperty("type")
        private String type;

        @JsonProperty("value")
        private String value;
    }
}
