package uk.gov.hmcts.reform.lrdapi.repository.projection;

public interface CourtVenueAddressProjection {

    String getMrdVenueId();

    String getType();

    String getAddress();

    String getPostCode();

    String getUprn();

    String getLanguage();
}
