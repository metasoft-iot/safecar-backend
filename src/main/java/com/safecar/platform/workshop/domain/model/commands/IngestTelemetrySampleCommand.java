package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;

/**
 * Command to ingest a telemetry sample.
 */
public record IngestTelemetrySampleCommand(
        TelemetrySample sample,
        String macAddress) {
}
