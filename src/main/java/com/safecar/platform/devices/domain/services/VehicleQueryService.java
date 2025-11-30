package com.safecar.platform.devices.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByDriverIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehiclesByWorkshopIdQuery;

/**
 * Vehicle Query Service
 * <p>
 * This service provides methods to handle queries related to Vehicle entities.
 * </p>
 */
public interface VehicleQueryService {
    /**
     * Handles the GetVehicleByDriverIdQuery to retrieve vehicles by driver ID.
     *
     * @param query The query containing the driver ID.
     * @return A list of vehicles associated with the specified driver ID.
     */
    List<Vehicle> handle(GetVehicleByDriverIdQuery query);

    /**
     * Handles the GetVehicleByIdQuery to retrieve a vehicle by its ID.
     *
     * @param query The query containing the vehicle ID.
     * @return An Optional containing the vehicle if found, or empty if not found.
     */
    Optional<Vehicle> handle(GetVehicleByIdQuery query);

    /**
     * Handles the GetVehiclesByWorkshopIdQuery to retrieve vehicles by workshop ID.
     *
     * @param query The query containing the workshop ID.
     * @return A list of vehicles associated with the specified workshop ID.
     */
    List<Vehicle> handle(GetVehiclesByWorkshopIdQuery query);

    /**
     * Handles the GetVehicleByLicensePlateQuery to retrieve a vehicle by its
     * license plate.
     *
     * @param query The query containing the license plate.
     * @return An Optional containing the vehicle if found, or empty if not found.
     */
    Optional<Vehicle> handle(com.safecar.platform.devices.domain.model.queries.GetVehicleByLicensePlateQuery query);
}
