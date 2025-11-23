package com.safecar.platform.workshop.domain.model.events;

import java.time.Instant;

public record TelemetryFlushedEvent(
        Long telemetryAggregateId,
        Instant flushedAt,
        long count
) {
}
