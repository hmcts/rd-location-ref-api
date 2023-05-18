package uk.gov.hmcts.reform.lrdapi.service;

public interface ILrdBuildingLocationService {

    /**
     * Method to retrieve the details of the requested building location.
     *
     * @param epimsId A list of epimm id of the building location
     * @param buildingLocationName A building location name for which the building location details are retrieved
     * @param regionId A region for which the building location details are retrieved
     * @param clusterId A cluster for which the building location details are retrieved
     * @return The the response object containing the details of the requested building location
     */
    Object retrieveBuildingLocationDetails(String epimsId,
                                           String buildingLocationName,
                                           String regionId,
                                           String clusterId);

    Object searchBuildingLocationsBySearchString(String buildingLocationName);

}
