package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdCourtVenuesByServiceCodeResponse;
import uk.gov.hmcts.reform.lrdapi.domain.CourtType;
import uk.gov.hmcts.reform.lrdapi.domain.CourtTypeServiceAssoc;
import uk.gov.hmcts.reform.lrdapi.repository.CourtTypeServiceAssocRepository;
import uk.gov.hmcts.reform.lrdapi.service.CourtVenueService;

import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR;

@Slf4j
@Service
public class CourtVenueServiceImpl implements CourtVenueService {

    @Autowired
    CourtTypeServiceAssocRepository courtTypeServiceAssocRepository;

    @Override
    public LrdCourtVenuesByServiceCodeResponse retrieveCourtVenuesByServiceCode(String serviceCode) {

        String serviceCodeIgnoreCase = serviceCode.toUpperCase();

        CourtTypeServiceAssoc courtTypeServiceAssoc =
            courtTypeServiceAssocRepository.findByServiceCode(serviceCodeIgnoreCase);

        if (isNull(courtTypeServiceAssoc)) {
            throw new ResourceNotFoundException("No court types found for the given service code " + serviceCode);
        }

        CourtType courtType = courtTypeServiceAssoc.getCourtType();

        if (CollectionUtils.isEmpty(courtType.getCourtVenues())) {
            throw new ResourceNotFoundException("No court venues found for the given service code " + serviceCode);
        }

        return new LrdCourtVenuesByServiceCodeResponse(courtType,serviceCodeIgnoreCase);

    }

    public static void validateServiceCode(String serviceCode) {

        if (isBlank(serviceCode)) {
            throw new InvalidRequestException("No service code provided");
        }


        if (!Pattern.compile(ALPHA_NUMERIC_REGEX_WITHOUT_UNDERSCORE).matcher(serviceCode).matches()) {
            throw new InvalidRequestException(EXCEPTION_MSG_SERVICE_CODE_SPCL_CHAR);
        }
    }
}
