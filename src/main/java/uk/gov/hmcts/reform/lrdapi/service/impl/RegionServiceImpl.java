package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdRegionResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Region;
import uk.gov.hmcts.reform.lrdapi.repository.RegionRepository;
import uk.gov.hmcts.reform.lrdapi.service.RegionService;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
public class RegionServiceImpl implements RegionService {

    @Autowired
    RegionRepository regionRepository;

    public LrdRegionResponse retrieveRegionByRegionDescription(String description) {
        Region region = regionRepository.findByDescriptionIgnoreCase(description);

        if (isEmpty(region)) {
            throw new ResourceNotFoundException("No Region found with the given description: " + description);
        }

        return new LrdRegionResponse(region);
    }
}
