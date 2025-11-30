package com.safecar.platform.deviceManagement.application.internal.commandservices;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Device;
import com.safecar.platform.deviceManagement.domain.model.commands.CreateDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.commands.UpdateDeviceMetricsCommand;
import com.safecar.platform.deviceManagement.domain.services.DeviceCommandService;
import com.safecar.platform.deviceManagement.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;
    private final com.safecar.platform.devices.domain.services.VehicleQueryService vehicleQueryService;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository,
            com.safecar.platform.devices.domain.services.VehicleQueryService vehicleQueryService) {
        this.deviceRepository = deviceRepository;
        this.vehicleQueryService = vehicleQueryService;
    }

    @Override
    public Optional<Device> handle(CreateDeviceCommand command) {
        // 1. Validar MAC única
        if (deviceRepository.existsByMacAddress(command.macAddress())) {
            throw new IllegalArgumentException("Device with MAC " + command.macAddress() + " already exists");
        }

        // 2. Crear
        var device = new Device(command);

        // 2.1 Si viene placa, buscar vehículo y asignar
        if (command.licensePlate() != null && !command.licensePlate().isBlank()) {
            var query = new com.safecar.platform.devices.domain.model.queries.GetVehicleByLicensePlateQuery(
                    command.licensePlate());
            var vehicle = vehicleQueryService.handle(query);
            if (vehicle.isPresent()) {
                device.assignToVehicle(vehicle.get().getId());
            } else {
                throw new IllegalArgumentException(
                        "Vehicle with license plate " + command.licensePlate() + " not found");
            }
        } else {
            // Optional: Decide if license plate is mandatory. For now, we can allow
            // creating a device without a vehicle.
            // Or throw exception if linking is required.
            // Based on user request, licensePlate seems to be the way to link.
        }

        // 3. Guardar y retornar envuelto en Optional
        deviceRepository.save(device);
        return Optional.of(device);
    }

    @Override
    public Optional<Device> handle(UpdateDeviceMetricsCommand command) {
        // CORRECCIÓN 1: Convertir el String que viene del comando a Long
        // (Asumiendo que en el command el deviceId es un String)
        var id = Long.parseLong(command.deviceId());

        // CORRECCIÓN 2: Usar 'findById' que ya viene gratis en JpaRepository
        var result = deviceRepository.findById(id);

        if (result.isPresent()) {
            Device device = result.get();

            // Aquí llamarías a los métodos de tu dominio para actualizar
            // Ej: device.updateMetrics(command.lat(), command.lng());

            deviceRepository.save(device);
            return Optional.of(device);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Device> handle(
            com.safecar.platform.deviceManagement.domain.model.commands.UpdateDeviceCommand command) {
        var result = deviceRepository.findById(command.deviceId());

        if (result.isPresent()) {
            Device device = result.get();

            if (command.licensePlate() != null && !command.licensePlate().isBlank()) {
                var query = new com.safecar.platform.devices.domain.model.queries.GetVehicleByLicensePlateQuery(
                        command.licensePlate());
                var vehicle = vehicleQueryService.handle(query);
                if (vehicle.isPresent()) {
                    device.assignToVehicle(vehicle.get().getId());
                } else {
                    throw new IllegalArgumentException(
                            "Vehicle with license plate " + command.licensePlate() + " not found");
                }
            }

            if (command.status() != null && !command.status().isBlank()) {
                device.updateStatus(command.status());
            }

            deviceRepository.save(device);
            return Optional.of(device);
        }

        return Optional.empty();
    }

    @Override
    public void handle(com.safecar.platform.deviceManagement.domain.model.commands.DeleteDeviceCommand command) {
        var result = deviceRepository.findById(command.deviceId());
        result.ifPresent(deviceRepository::delete);
    }
}
