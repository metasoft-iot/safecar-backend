package com.safecar.platform.deviceManagement.domain.model.aggregates;

import com.safecar.platform.deviceManagement.domain.model.commands.CreateDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.valueobjects.DeviceType;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "devices",
        uniqueConstraints = @UniqueConstraint(columnNames = "mac_address")) // Regla física
public class Device extends AuditableAbstractAggregateRoot<Device> {

    // NOTA: No necesitamos crear 'id', 'createdAt' ni 'updatedAt'.
    // Ya vienen heredados de AuditableAbstractAggregateRoot.
    // El 'id' heredado ES tu "Device ID" lógico.

    @NotNull
    @Column(name = "mac_address", nullable = false, unique = true, length = 20)
    private String macAddress; // Identificador Físico

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    // Relación con Vehicle
    // OJO: Como tu Vehicle ahora usa UUID (por el AuditableAbstract...),
    // aquí el vínculo debe ser UUID, no Long.
    @Column(name = "vehicle_id")
    private UUID vehicleId;

    // Estado del dispositivo
    private String status;

    // Constructor vacío protegido para JPA
    protected Device() {}

    // Constructor de Negocio (Command)
    public Device(CreateDeviceCommand command) {
        this.macAddress = command.macAddress();
        this.deviceType = command.deviceType();
        this.status = "ACTIVE";

        // Si el comando trae un vehículo, lo asignamos
        if (command.vehicleId() != null) {
            this.vehicleId = command.vehicleId();
        }
    }

    // Comportamiento de Negocio (Business Logic)

    public void assignToVehicle(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}