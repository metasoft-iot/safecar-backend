package com.safecar.platform.insights.domain.model.valueobjects;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Simplified snapshot of sensors that feeds the analytics engine.
 */
public record TelemetrySensorPayload(
        Instant capturedAt,
        /**
         * MH MQ
         */
        BigDecimal gasLevel,
        /**
         * LM35
         */
        BigDecimal engineTemperature,
        /**
         * DHT11
         */
        BigDecimal ambientTemperature,
        /**
         * DHT11
         */
        BigDecimal humidity,
        /**
         * ACS71
         */
        BigDecimal currentAmps,
        BigDecimal latitude,
        BigDecimal longitude,
        String alertSeverity) {
}
