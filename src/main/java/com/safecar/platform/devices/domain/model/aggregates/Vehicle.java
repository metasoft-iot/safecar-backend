package com.safecar.platform.devices.domain.model.aggregates;

import com.safecar.platform.devices.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.devices.domain.model.valueobjects.DriverId;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Vehicle Aggregate - represents a vehicle in the system.
 */
@Getter
@Entity
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {

    /**
     * Driver Identifier - The identifier of the driver who owns the vehicle.
     */
    @Embedded
    private DriverId driverId;

    /**
     * License Plate - The license plate of the vehicle.
     */
    @NotNull
    private String licensePlate;

    /**
     * Brand - The brand of the vehicle.
     */
    @NotNull
    private String brand;

    /**
     * Model - The model of the vehicle.
     */
    @NotNull
    private String model;

    /**
     * Protected constructor for JPA
     */
    protected Vehicle() {
    }

    /**
     * Constructor to create a Vehicle from a CreateVehicleCommand
     * 
     * @param command the command containing vehicle creation data
     */
    public Vehicle(CreateVehicleCommand command) {
        this.driverId = new DriverId(command.driverId());
        this.licensePlate = command.licensePlate();
        this.brand = command.brand();
        this.model = command.model();
    }

    /**
     * Update vehicle details.
     * 
     * @param licensePlate the new license plate
     * @param brand        the new brand
     * @param model        the new model
     */
    public void updateVehicle(String licensePlate, String brand, String model) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
    }

    /**
     * Get Driver ID
     * 
     * @return the driver ID as Long
     */
    public Long getDriverId() {
        return this.driverId.driverId();
    }
}
