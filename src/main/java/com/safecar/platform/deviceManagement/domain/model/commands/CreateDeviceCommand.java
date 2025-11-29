package com.safecar.platform.deviceManagement.domain.model.commands;

import com.safecar.platform.deviceManagement.domain.model.valueobjects.DeviceType;

import java.util.UUID;

public record CreateDeviceCommand(
        String macAddress,
        DeviceType deviceType,
        UUID vehicleId // Opcional: Si el dispositivo se instala directo a un auto
) {}