package com.safecar.platform.deviceManagement.domain.model.commands;

public record UpdateDeviceMetricsCommand(
        String deviceId,
        Double lastLatitude,
        Double lastLongitude,
        Double batteryLevel
) {}