package com.safecar.platform.devices.domain.model.queries;

public record GetVehicleByLicensePlateQuery(String licensePlate) {
    public GetVehicleByLicensePlateQuery {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("licensePlate cannot be null or empty");
        }
    }
}
