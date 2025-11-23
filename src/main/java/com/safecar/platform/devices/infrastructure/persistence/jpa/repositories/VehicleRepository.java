package com.safecar.platform.devices.infrastructure.persistence.jpa.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;

import java.util.List;

/**
 * Vehicle Repository - JPA Repository to manage Vehicle entities.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    /**
     * Finds all Vehicles for a given driver ID.
     * 
     * @param driverId The ID of the driver.
     * @return A list of Vehicles associated with the given driver ID.
     */
    List<Vehicle> findByDriverId_DriverId(Long driverId);
    
    /**
     * Checks if a vehicle exists with the given license plate.
     * 
     * @param licensePlate The license plate to check.
     * @return true if a vehicle with the license plate exists, false otherwise.
     */
    boolean existsByLicensePlate(String licensePlate);
    
    /**
     * Finds a vehicle by its license plate.
     * 
     * @param licensePlate The license plate to search for.
     * @return An Optional containing the vehicle if found.
     */
    java.util.Optional<Vehicle> findByLicensePlate(String licensePlate);
}
