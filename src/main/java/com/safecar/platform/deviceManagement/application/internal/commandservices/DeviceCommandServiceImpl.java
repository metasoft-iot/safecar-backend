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

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Device> handle(CreateDeviceCommand command) {
        // 1. Validar MAC única
        if (deviceRepository.existsByMacAddress(command.macAddress())) {
            throw new IllegalArgumentException("Device with MAC " + command.macAddress() + " already exists");
        }

        // 2. Crear
        var device = new Device(command);

        // 3. Guardar y retornar envuelto en Optional
        deviceRepository.save(device);
        return Optional.of(device);
    }

    @Override
    public Optional<Device> handle(UpdateDeviceMetricsCommand command) {
        // CORRECCIÓN 1: Convertir el String que viene del comando a UUID
        // (Asumiendo que en el command el deviceId es un String)
        var id = java.util.UUID.fromString(command.deviceId());

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
}