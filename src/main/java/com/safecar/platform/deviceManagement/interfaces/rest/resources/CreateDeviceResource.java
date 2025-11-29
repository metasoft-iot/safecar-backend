package com.safecar.platform.deviceManagement.interfaces.rest.resources;

public record CreateDeviceResource(
        String macAddress,
        String deviceType, // Ej: "GPS_TRACKER"
        String vehicleId   // UUID del vehículo en formato String (opcional)
) {}