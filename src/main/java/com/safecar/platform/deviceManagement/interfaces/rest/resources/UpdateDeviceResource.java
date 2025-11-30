package com.safecar.platform.deviceManagement.interfaces.rest.resources;

public record UpdateDeviceResource(
        String licensePlate,
        String status) {
}
