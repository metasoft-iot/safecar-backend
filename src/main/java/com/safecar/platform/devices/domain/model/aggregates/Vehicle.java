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
     * Year - The manufacturing year of the vehicle.
     */
    private Integer year;

    /**
     * VIN - The Vehicle Identification Number.
     */
    private String vin;

    /**
     * Color - The color of the vehicle.
     */
    private String color;

    /**
     * Mileage - The current mileage/odometer reading of the vehicle.
     */
    private Integer mileage;

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
        this.year = command.year();
        this.vin = command.vin();
        this.color = command.color();
        this.mileage = command.mileage();
    }

    /**
     * Update vehicle details.
     * 
     * @param licensePlate the new license plate
     * @param brand        the new brand
     * @param model        the new model
     * @param year         the new year
     * @param vin          the new VIN
     * @param color        the new color
     * @param mileage      the new mileage
     */
    public void updateVehicle(String licensePlate, String brand, String model,
            Integer year, String vin, String color, Integer mileage) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.color = color;
        this.mileage = mileage;
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
