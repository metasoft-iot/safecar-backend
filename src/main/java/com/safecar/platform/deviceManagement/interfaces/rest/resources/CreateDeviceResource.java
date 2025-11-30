package com.safecar.platform.deviceManagement.interfaces.rest.resources;

public record CreateDeviceResource(
        String macAddress,
        String deviceType, // Ej: "GPS_TRACKER"
        String licensePlate // Placa del vehículo (opcional, alternativa a vehicleId)
) {
}
