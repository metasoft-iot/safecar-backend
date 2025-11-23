package com.safecar.platform.workshop.application.internal.outboundservices.acl;

import com.safecar.platform.devices.interfaces.acl.DevicesContextFacade;
import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * External Device Service
 * <p>
 *     This service provides access to Devices bounded context functionality from WorkshopOps.
 *     It acts as an adapter/wrapper around the DevicesContextFacade to provide domain-specific
 *     operations for the WorkshopOps bounded context.
 * </p>
 */
@Service("workshopExternalDeviceService")
public class ExternalDeviceService {

    
    private final DevicesContextFacade devicesContextFacade;

    /**
     * Constructor
     *
     * @param devicesContextFacade Devices Context Facade
     */
    public ExternalDeviceService(DevicesContextFacade devicesContextFacade) {
        this.devicesContextFacade = devicesContextFacade;
    }

    /**
     * Validates if a vehicle exists in the system.
     * Used before creating telemetry records or appointments.
     *
     * @param vehicleId the vehicle ID to validate
     * @return true if vehicle exists, false otherwise
     */
    public boolean validateVehicleExists(VehicleId vehicleId) {
        try {
            return devicesContextFacade.validateVehicleExists(vehicleId.vehicleId());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates if a vehicle exists by license plate.
     * Useful for telemetry ingestion from external systems.
     *
     * @param licensePlate the license plate to validate
     * @return true if vehicle exists, false otherwise
     */
    public boolean validateVehicleExistsByLicensePlate(String licensePlate) {
        try {
            return devicesContextFacade.validateVehicleExistsByLicensePlate(licensePlate);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fetches the driver ID associated with a vehicle.
     * Used for linking telemetry and appointments to drivers.
     *
     * @param vehicleId the vehicle ID
     * @return Optional containing driver ID if found
     */
    public Optional<Long> fetchVehicleDriverId(VehicleId vehicleId) {
        try {
            Long driverId = devicesContextFacade.fetchVehicleDriverId(vehicleId.vehicleId());
            return driverId != null && driverId > 0 ? Optional.of(driverId) : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Fetches vehicle license plate for display purposes.
     *
     * @param vehicleId the vehicle ID
     * @return license plate if found, empty string otherwise
     */
    public String fetchVehicleLicensePlate(VehicleId vehicleId) {
        try {
            return devicesContextFacade.fetchVehicleLicensePlate(vehicleId.vehicleId());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Fetches vehicle details (brand and model) for display purposes.
     *
     * @param vehicleId the vehicle ID
     * @return formatted vehicle details string
     */
    public String fetchVehicleDetails(VehicleId vehicleId) {
        try {
            return devicesContextFacade.fetchVehicleDetails(vehicleId.vehicleId());
        } catch (Exception e) {
            return "Unknown Vehicle";
        }
    }

    /**
     * Validates if a specific driver owns the given vehicle.
     * Used for authorization in appointment and work order creation.
     *
     * @param vehicleId the vehicle ID
     * @param driverId the driver ID
     * @return true if driver owns the vehicle, false otherwise
     */
    public boolean validateDriverOwnsVehicle(VehicleId vehicleId, Long driverId) {
        try {
            return devicesContextFacade.validateDriverOwnsVehicle(vehicleId.vehicleId(), driverId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Convenience method to validate vehicle exists using Long ID.
     *
     * @param vehicleId the vehicle ID as Long
     * @return true if vehicle exists, false otherwise
     */
    public boolean validateVehicleExists(Long vehicleId) {
        try {
            return devicesContextFacade.validateVehicleExists(vehicleId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Convenience method to fetch driver ID using Long vehicle ID.
     *
     * @param vehicleId the vehicle ID as Long
     * @return Optional containing driver ID if found
     */
    public Optional<Long> fetchVehicleDriverId(Long vehicleId) {
        try {
            Long driverId = devicesContextFacade.fetchVehicleDriverId(vehicleId);
            return driverId != null && driverId > 0 ? Optional.of(driverId) : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Convenience method to create VehicleId from Long ID.
     *
     * @param vehicleId the vehicle ID as Long
     * @return VehicleId value object
     */
    public VehicleId createVehicleId(Long vehicleId) {
        return new VehicleId(vehicleId);
    }
}