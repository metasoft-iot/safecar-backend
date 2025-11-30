package com.safecar.platform.deviceManagement.domain.model.commands;

public record UpdateDeviceCommand(
        Long deviceId,
        String licensePlate,
        String status) {
}
