package com.safecar.platform.deviceManagement.domain.model.commands;

import com.safecar.platform.deviceManagement.domain.model.valueobjects.DeviceType;

public record CreateDeviceCommand(
        String macAddress,
        DeviceType deviceType,
        String licensePlate // Opcional: Para buscar el vehículo por placa
) {
}
