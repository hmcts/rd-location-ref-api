package uk.gov.hmcts.reform.lrdapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.domain.Service;
import uk.gov.hmcts.reform.lrdapi.domain.ServiceToCcdCaseTypeAssoc;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceRepository;
import uk.gov.hmcts.reform.lrdapi.repository.ServiceToCcdCaseTypeAssocRepositry;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.ALL;
import static uk.gov.hmcts.reform.lrdapi.controllers.constants.LocationRefConstants.COMMA;

@org.springframework.stereotype.Service
@Slf4j
public class LrdServiceImpl implements LrdService {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceToCcdCaseTypeAssocRepositry serviceToCcdCaseTypeAssocRepositry;

    @Override
    public List<LrdOrgInfoServiceResponse> retrieveOrgServiceDetails(String serviceCode,
                                                                     String ccdCaseType, String ccdServiceNames) {
        Service servicePojo;
        ServiceToCcdCaseTypeAssoc serToCcdCaseType;
        List<Service> services;
        final List<LrdOrgInfoServiceResponse> orgInfoServiceResponses = new ArrayList<>();
        if (StringUtils.isNotBlank(serviceCode)) {

            servicePojo = serviceRepository.findByServiceCode(serviceCode.trim().toUpperCase());
            ifServiceResponseNullThrowException(servicePojo);
            orgInfoServiceResponses.add(new LrdOrgInfoServiceResponse(servicePojo));

        } else if (StringUtils.isNotBlank(ccdCaseType)) {

            serToCcdCaseType = serviceToCcdCaseTypeAssocRepositry
                .findByCcdCaseTypeIgnoreCase(ccdCaseType.trim());
            servicePojo = serToCcdCaseType != null ? serToCcdCaseType.getService() : null;
            ifServiceResponseNullThrowException(servicePojo);
            orgInfoServiceResponses.add(new LrdOrgInfoServiceResponse(servicePojo));

        } else if (StringUtils.isNotBlank(ccdServiceNames) && !ccdServiceNames.trim().equalsIgnoreCase(ALL)
            && !ccdServiceNames.toUpperCase().trim().contains(ALL)) {
            List<String> serviceNameList = Arrays.asList(ccdServiceNames.split(COMMA));

            List<ServiceToCcdCaseTypeAssoc> serviceToCcdCaseTypeAssocs = serviceToCcdCaseTypeAssocRepositry
                .findByCcdServiceNameInIgnoreCase(serviceNameList
                                                      .stream()
                                                      .map(String::trim)
                                                      .collect(toList()));

            if (CollectionUtils.isEmpty(serviceToCcdCaseTypeAssocs)) {
                throw new EmptyResultDataAccessException(1);
            }

            List<ServiceToCcdCaseTypeAssoc> distinctAssociations =
                serviceToCcdCaseTypeAssocs.stream().distinct().collect(toList());

            distinctAssociations.forEach(association ->
                                                   orgInfoServiceResponses.add(
                                                       new LrdOrgInfoServiceResponse(association.getService())));

        } else {

            services = serviceRepository.findAll();

            if (null == services) {
                throw new EmptyResultDataAccessException(1);
            }
            services.forEach(service ->
                orgInfoServiceResponses.add(new LrdOrgInfoServiceResponse(service))
            );

        }
        return orgInfoServiceResponses;
    }

    public void ifServiceResponseNullThrowException(Service service) {
        if (null == service) {
            throw new EmptyResultDataAccessException(1);
        }
    }
}
