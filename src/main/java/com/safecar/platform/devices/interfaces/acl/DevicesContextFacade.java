package com.safecar.platform.devices.interfaces.acl;

/**
 * DevicesContextFacade
 * <p>
 *     This interface provides the methods to interact with the Devices context.
 *     It provides methods to validate vehicles, retrieve vehicle information,
 *     and access driver-vehicle relationships.
 *     The implementation will be provided by the Devices module.
 *     This interface is used by other bounded contexts to interact with vehicle data.
 * </p>
 */
public interface DevicesContextFacade {

    /**
     * validateVehicleExists
     * <p>
     *     This method validates if a Vehicle exists by vehicleId.
     * </p>
     * @param vehicleId the vehicle ID to validate
     * @return true if Vehicle exists, false otherwise
     */
    boolean validateVehicleExists(Long vehicleId);

    /**
     * validateVehicleExistsByLicensePlate
     * <p>
     *     This method validates if a Vehicle exists by license plate.
     * </p>
     * @param licensePlate the license plate to validate
     * @return true if Vehicle exists, false otherwise
     */
    boolean validateVehicleExistsByLicensePlate(String licensePlate);

    /**
     * fetchVehicleDriverId
     * <p>
     *     This method fetches the driver ID associated with a vehicle.
     * </p>
     * @param vehicleId the vehicle ID
     * @return the driver ID if vehicle found, 0L otherwise
     */
    Long fetchVehicleDriverId(Long vehicleId);

    /**
     * fetchVehicleLicensePlate
     * <p>
     *     This method fetches the license plate of a vehicle by vehicle ID.
     * </p>
     * @param vehicleId the vehicle ID
     * @return the license plate if vehicle found, empty string otherwise
     */
    String fetchVehicleLicensePlate(Long vehicleId);

    /**
     * fetchVehicleDetails
     * <p>
     *     This method fetches basic vehicle details (brand and model).
     * </p>
     * @param vehicleId the vehicle ID
     * @return formatted vehicle details string "Brand Model" if found, "Unknown Vehicle" otherwise
     */
    String fetchVehicleDetails(Long vehicleId);

    /**
     * validateDriverOwnsVehicle
     * <p>
     *     This method validates if a specific driver owns the given vehicle.
     * </p>
     * @param vehicleId the vehicle ID
     * @param driverId the driver ID
     * @return true if driver owns the vehicle, false otherwise
     */
    boolean validateDriverOwnsVehicle(Long vehicleId, Long driverId);

    /**
     * createDriver
     * <p>
     *     This method creates a new driver associated with a profile.
     * </p>
     * @param profileId the profile ID from Profiles BC
     * @return the driver ID if creation successful, 0L otherwise
     */
    Long createDriver(Long profileId);

    /**
     * existsDriverByProfileId
     * <p>
     *     This method checks if a driver exists for the given profile ID.
     * </p>
     * @param profileId the profile ID from Profiles BC
     * @return true if driver exists, false otherwise
     */
    boolean existsDriverByProfileId(Long profileId);

    /**
     * getDriverIdByProfileId
     * <p>
     *     This method retrieves the driver ID associated with a profile ID.
     * </p>
     * @param profileId the profile ID from Profiles BC
     * @return the driver ID if found, 0L otherwise
     */
    Long getDriverIdByProfileId(Long profileId);
}