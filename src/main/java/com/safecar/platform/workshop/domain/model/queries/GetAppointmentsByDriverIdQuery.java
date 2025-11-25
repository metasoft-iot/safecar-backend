package com.safecar.platform.workshop.domain.model.queries;

import com.safecar.platform.workshop.domain.model.valueobjects.DriverId;

public record GetAppointmentsByDriverIdQuery(DriverId driverId) {
    public GetAppointmentsByDriverIdQuery(Long driverId) {
        this(new DriverId(driverId));
    }
}
