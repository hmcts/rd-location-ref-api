package uk.gov.hmcts.reform.lrdapi.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

@RequestMapping(
    path = "/refdata/location/v2/court-venues"
)
@RestController
@Slf4j
public class LrdCourtVenueV2Controller {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    CourtVenueService courtVenueService;


}
