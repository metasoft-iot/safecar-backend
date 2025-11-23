package com.safecar.platform.workshop.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.workshop.domain.model.entities.TelemetryRecord;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryAlertsBySeverityQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryByVehicleAndRangeQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryByVehicleQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryRecordByIdQuery;

public interface VehicleTelemetryQueryService {
    Optional<TelemetryRecord> handle(GetTelemetryRecordByIdQuery query);
    List<TelemetryRecord> handle(GetTelemetryByVehicleAndRangeQuery query);
    List<TelemetryRecord> handle(GetTelemetryByVehicleQuery query);
    List<TelemetryRecord> handle(GetTelemetryAlertsBySeverityQuery query);
}
