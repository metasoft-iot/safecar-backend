package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.commands.FlushTelemetryCommand;
import com.safecar.platform.workshop.domain.model.commands.IngestTelemetrySampleCommand;

public interface VehicleTelemetryCommandService {
    void handle(IngestTelemetrySampleCommand command);
    long handle(FlushTelemetryCommand command);
}
