package com.safecar.platform.workshop.domain.model.events;

import java.time.Instant;

import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;

public record TelemetrySampleIngestedEvent(
        Long recordId,
        TelemetrySample sample,
        Instant ingestedAt
) {
}
