package uk.gov.hmcts.reform.lrdapi.repository.projection;

public interface CourtVenueNameProjection {

    String getMrdVenueId();

    String getType();

    String getLanguage();

    String getName();
}
