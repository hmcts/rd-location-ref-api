package uk.gov.hmcts.reform.lrdapi.repository.projection;

public interface CourtVenueContactProjection {

    String getMrdVenueId();

    String getMethod();

    String getType();

    String getValue();
}
