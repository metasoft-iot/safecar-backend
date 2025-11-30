package com.safecar.platform.deviceManagement.interfaces.rest.resources;

public record DeviceResource(
        String id, // El UUID lógico
        String macAddress, // La MAC física
        String deviceType,
        String status,
        String licensePlate // Puede ser null si no está instalado
) {
}
