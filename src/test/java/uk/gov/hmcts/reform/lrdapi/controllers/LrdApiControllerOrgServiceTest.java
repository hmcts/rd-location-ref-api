package uk.gov.hmcts.reform.lrdapi.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.lrdapi.controllers.advice.InvalidRequestException;
import uk.gov.hmcts.reform.lrdapi.controllers.response.LrdOrgInfoServiceResponse;
import uk.gov.hmcts.reform.lrdapi.oidc.JwtGrantedAuthoritiesConverter;
import uk.gov.hmcts.reform.lrdapi.service.ILrdBuildingLocationService;
import uk.gov.hmcts.reform.lrdapi.service.LrdService;
import uk.gov.hmcts.reform.lrdapi.service.RegionService;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class LrdApiControllerOrgServiceTest {

    @InjectMocks
    private LrdApiControllerOrgServiceTest lrdApiController;

    @Mock
    ILrdBuildingLocationService lrdBuildingLocationService;

    @Mock
    LrdService lrdServiceMock;

    @Mock
    RegionService regionServiceMock;

    List<LrdOrgInfoServiceResponse> lrdOrgInfoServiceResponse = new ArrayList<>();
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverterMock;
    private UserInfo userInfoMock;
    HttpServletRequest httpRequest = mock(HttpServletRequest.class);
    String serviceCode;
    String ccdCaseType;
    String ccdServiceName;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveOrgServiceDetailsByServiceCode() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        serviceCode = "AAA1";
        when(lrdServiceMock.retrieveOrgServiceDetails(any(), any(), any())).thenReturn(lrdOrgInfoServiceResponse);
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
        verify(lrdServiceMock, times(1)).retrieveOrgServiceDetails(any(), any(), any());
    }

    @Test
    public void testRetrieveOrgServiceDetailsByCcdCaseType() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        ccdCaseType = "ccdCaseType1";
        when(lrdServiceMock.retrieveOrgServiceDetails(any(), any(), any())).thenReturn(lrdOrgInfoServiceResponse);
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
        verify(lrdServiceMock, times(1)).retrieveOrgServiceDetails(any(), any(), any());
    }

    @Test
    public void testRetrieveOrgServiceDetailsByDefaultRequestParamsNull() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        when(lrdServiceMock.retrieveOrgServiceDetails(any(), any(), any())).thenReturn(lrdOrgInfoServiceResponse);
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
        verify(lrdServiceMock, times(1)).retrieveOrgServiceDetails(any(), any(), any());
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveOrgServiceDetailsShouldThrowExceptionWhenBothParamValuesPresent() {
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        serviceCode = "AAA1";
        ccdCaseType = "ccdCaseType1";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
    }

    //1. Validate that only 1 out of 3 query params are being passed. Use null and empty values for testing
    @Test(expected = InvalidRequestException.class)
    public void testRetrieveOrgServiceDetailsShouldThrowExceptionWhenMultipleParamValuesPresent() {
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        serviceCode = "AAA1";
        ccdCaseType = "ccdCaseType1";
        ccdServiceName = "fpla";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
    }

    @Test
    public void testRetrieveOrgServiceDetailsShouldPassForNullAndBlankValuesScenario1() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        serviceCode = "";
        ccdCaseType = null;
        ccdServiceName = "fpla";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
    }

    @Test
    public void testRetrieveOrgServiceDetailsShouldPassForNullAndBlankValuesScenario2() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        serviceCode = "";
        ccdCaseType = null;
        ccdServiceName = "";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveOrgServiceDetailsbyPassingSpecialCharInInput() {
        serviceCode = "abcd@£";
        ccdCaseType = "";
        ccdServiceName = "";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveOrgServiceDetailsbyPassingWhiteSpaceInInput() {
        serviceCode = " Select from employee ";
        ccdCaseType = "";
        ccdServiceName = "";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
    }

    @Test
    public void testRetrieveOrgServiceDetailsbyPassingUnderScoreInInput() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        serviceCode = " abcd_jgh ";
        ccdCaseType = "";
        ccdServiceName = "";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
    }

    @Test
    public void testRetrieveOrgServiceDetailsbyPassingcommaSeperatedServiceNameInInput() {
        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        serviceCode = "";
        ccdCaseType = "";
        ccdServiceName = "abcd, cdef";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveOrgServiceDetailsbyPassingcommaSeperatedServiceNameInInput_fail() {
        serviceCode = "";
        ccdCaseType = "";
        ccdServiceName = "abcd,, cdef";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveOrgServiceDetailsbyPassingcommaSeperatedServiceNameInInput_fail_1() {
        serviceCode = "";
        ccdCaseType = "";
        ccdServiceName = "abcd, cdef,";
        ResponseEntity<?> actual = lrdApiController
            .retrieveOrgServiceDetails(serviceCode, ccdCaseType, ccdServiceName);
    }

    @Test
    public void testGetBuildingLocations_ValidSingleEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("111",""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("111", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("111", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_AllEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("ALL", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("ALL", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("ALL", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_lowerAllEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("all", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("all", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("all", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_EpimmsIdWithASpaceGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails(" 111 ", ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails(" 111 ", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails(" 111 ", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_ValidMultipleEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("111,qwerty, qwerty_123",
                                                                        ""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("111,qwerty, qwerty_123",
                                                             "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("111,qwerty, qwerty_123", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_1Valid1InvalidEpimmsIdGiven_ShouldReturnStatusCode200() {
        when(lrdBuildingLocationService.retrieveBuildingLocationDetails("!@£$%,qwerty",""))
            .thenReturn(new ArrayList<>());
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("!@£$%,qwerty", "");
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("!@£$%,qwerty", "");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetBuildingLocations_StringEpimsIdGiven_ShouldReturnStatusCode200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("QWERTY", "");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("QWERTY", "");
    }

    @Test
    public void testGetBuildingLocations_AlphaNumericEpimsIdGiven_ShouldReturnStatusCode200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveBuildingLocationDetails("1234QWERTY", "");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lrdBuildingLocationService, times(1))
            .retrieveBuildingLocationDetails("1234QWERTY", "");
    }

    @Test
    public void testGetRegionDetails_ByDescription_Returns200() {
        ResponseEntity<Object> responseEntity =
            lrdApiController.retrieveRegionDetails("London", "");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(regionServiceMock, times(1)).retrieveRegionByRegionDescription("London");
    }

//    @Test(expected = InvalidRequestException.class)
//    public void testGetRegionDetails_ByMissingDescription_Returns400() {
//        ResponseEntity<Object> responseEntity =
//            lrdApiController.retrieveRegionDetails("");
//
//        assertThat(responseEntity).isNotNull();
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//    }

}
