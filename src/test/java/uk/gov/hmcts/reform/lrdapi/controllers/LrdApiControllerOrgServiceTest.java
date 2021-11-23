package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.oidc.JwtGrantedAuthoritiesConverter;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LrdApiControllerOrgServiceTest {

    @InjectMocks
    private LrdApiController lrdApiController;

    @Mock
    ILrdBuildingLocationService lrdBuildingLocationService;

    @Mock
    LrdService lrdServiceMock;

    List<LrdOrgInfoServiceResponse> lrdOrgInfoServiceResponse = new ArrayList<>();
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverterMock;
    private UserInfo userInfoMock;
    HttpServletRequest httpRequest = mock(HttpServletRequest.class);
    String serviceCode;
    String ccdCaseType;
    String ccdServiceName;

    @Test
    void testRetrieveOrgServiceDetailsByServiceCode() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        serviceCode = "AAA1";
        when(lrdServiceMock.retrieveOrgServiceDetails(any(), any(), any())).thenReturn(lrdOrgInfoServiceResponse);
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertNotNull(actual);
        assertEquals(expectedHttpStatus, actual.getStatusCode());
        verify(lrdServiceMock, times(1)).retrieveOrgServiceDetails(any(), any(), any());
    }

    @Test
    void testRetrieveOrgServiceDetailsByCcdCaseType() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        ccdCaseType = "ccdCaseType1";
        when(lrdServiceMock.retrieveOrgServiceDetails(any(), any(), any())).thenReturn(lrdOrgInfoServiceResponse);
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertNotNull(actual);
        assertEquals(expectedHttpStatus, actual.getStatusCode());
        verify(lrdServiceMock, times(1)).retrieveOrgServiceDetails(any(), any(), any());
    }

    @Test
    void testRetrieveOrgServiceDetailsByDefaultRequestParamsNull() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        when(lrdServiceMock.retrieveOrgServiceDetails(any(), any(), any())).thenReturn(lrdOrgInfoServiceResponse);
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertNotNull(actual);
        assertEquals(expectedHttpStatus, actual.getStatusCode());
        verify(lrdServiceMock, times(1)).retrieveOrgServiceDetails(any(), any(), any());
    }

    @Test
    void testRetrieveOrgServiceDetailsShouldThrowExceptionWhenBothParamValuesPresent() {
        serviceCode = "AAA1";
        ccdCaseType = "ccdCaseType1";
        Exception ex = assertThrows(InvalidRequestException.class, () -> {
            lrdApiController.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        });
        assertNotNull(ex);
        assertEquals("Please provide only 1 of 3 values/params: [AAA1, ccdCaseType1, null]", ex.getMessage());
    }

    //1. Validate that only 1 out of 3 query params are being passed. Use null and empty values for testing
    @Test
    void testRetrieveOrgServiceDetailsShouldThrowExceptionWhenMultipleParamValuesPresent() {
        serviceCode = "AAA1";
        ccdCaseType = "ccdCaseType1";
        ccdServiceName = "fpla";
        Exception ex = assertThrows(InvalidRequestException.class, () -> {
            lrdApiController.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        });
        assertNotNull(ex);
        assertEquals("Please provide only 1 of 3 values/params: [AAA1, ccdCaseType1, fpla]", ex.getMessage());
    }

    @Test
    void testRetrieveOrgServiceDetailsShouldPassForNullAndBlankValuesScenarios() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        serviceCode = "";
        ccdCaseType = null;
        ccdServiceName = "fpla";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertNotNull(actual);
        assertEquals(expectedHttpStatus, actual.getStatusCode());

        // testRetrieveOrgServiceDetailsShouldPassForNullAndBlankValuesScenario2
        serviceCode = "";
        ccdCaseType = null;
        ccdServiceName = "";
        ResponseEntity<?> actual1 = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertNotNull(actual1);
        assertEquals(expectedHttpStatus, actual1.getStatusCode());

        // testRetrieveOrgServiceDetailsbyPassingUnderScoreInInput
        serviceCode = " abcd_jgh ";
        ccdCaseType = "";
        ccdServiceName = "";
        ResponseEntity<?> actual2 = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertNotNull(actual2);
        assertEquals(expectedHttpStatus, actual2.getStatusCode());

    }

    @Test
    void testRetrieveOrgServiceDetailsbyPassingSpecialCharInInput() {
        serviceCode = "abcd@£";
        ccdCaseType = "";
        ccdServiceName = "";
        assertThrows(InvalidRequestException.class, () -> {
            lrdApiController.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName); }
        );

        // testRetrieveOrgServiceDetailsbyPassingWhiteSpaceInInput
        serviceCode = " Select from employee ";
        ccdCaseType = "";
        ccdServiceName = "";
        assertThrows(InvalidRequestException.class, () -> {
            lrdApiController.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName); }
        );

        // testRetrieveOrgServiceDetailsbyPassingcommaSeperatedServiceNameInInput_fail
        serviceCode = "";
        ccdCaseType = "";
        ccdServiceName = "abcd,, cdef";
        assertThrows(InvalidRequestException.class, () -> {
            lrdApiController.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName); }
        );

        //testRetrieveOrgServiceDetailsbyPassingcommaSeperatedServiceNameInInput_fail_1
        serviceCode = "";
        ccdCaseType = "";
        ccdServiceName = "abcd, cdef,";
        assertThrows(InvalidRequestException.class, () -> {
            lrdApiController.retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName); }
        );
    }

    @Test
    void testRetrieveOrgServiceDetailsbyPassingcommaSeperatedServiceNameInInput() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        serviceCode = "";
        ccdCaseType = "";
        ccdServiceName = "abcd, cdef";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertNotNull(actual);
        assertEquals(expectedHttpStatus, actual.getStatusCode());
    }
}
