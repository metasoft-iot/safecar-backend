package com.safecar.platform.workshop.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.events.TelemetryFlushedEvent;
import com.safecar.platform.workshop.domain.model.events.TelemetrySampleIngestedEvent;

/**
 * Event handler for Telemetry related events.
 * <p>
 *     These events are triggered when telemetry samples are ingested or flushed.
 *     Used for real-time monitoring, analysis, and integration with other bounded contexts.
 * </p>
 */
@Service
public class TelemetryAndServiceEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryAndServiceEventHandler.class);

    /**
     * Event listener for TelemetrySampleIngestedEvent.
     * <p>
     *     This method is triggered when a telemetry sample is ingested.
     * </p>
     *
     * @param event the {@link TelemetrySampleIngestedEvent} event.
     */
    @EventListener
    public void on(TelemetrySampleIngestedEvent event) {
        LOGGER.info("Telemetry sample ingested for record ID {} at {}",
                   event.recordId(), event.ingestedAt());

        // Business logic: Real-time telemetry analysis and anomaly detection
        var sample = event.sample();
        var speed = sample.speed() != null ? sample.speed().value() : null;
        LOGGER.debug("Telemetry ingested - Vehicle: {}, Type: {}, Severity: {}, Speed: {}",
                    sample.vehicleId().vehicleId(), sample.type(),
                    sample.severity(), speed != null ? speed + " km/h" : "n/a");

        // Real-time safety analysis
        if (isUrgentTelemetryData(event)) {
            LOGGER.warn("🚨 CRITICAL ALERT: Vehicle {} requires immediate attention - Type: {} (Severity: {})",
                       sample.vehicleId().vehicleId(), sample.type(), sample.severity());

            if (sample.dtc() != null) {
                LOGGER.error("🔧 FAULT CODE DETECTED: {} - Standard: {}",
                           sample.dtc().code(), sample.dtc().standard());
            }

            // TODO: Integrate with Notification BC for critical telemetry alerts
            // TODO: Integrate with Emergency BC for immediate response protocols
            // TODO: Integrate with Appointment BC for emergency appointment creation
        }

        // Performance monitoring
        if (isPerformanceImpactTelemetry(sample)) {
            LOGGER.info("📊 Performance Alert: Vehicle {} showing degraded performance - Type: {}, Location: {}",
                       sample.vehicleId().vehicleId(), sample.type(), sample.location());

            // TODO: Integrate with Predictive BC for maintenance scheduling
        }

        if (sample.tirePressure() != null && hasCriticalTirePressure(sample.tirePressure())) {
            LOGGER.warn("⚠️ Tire pressure imbalance detected on vehicle {}. Pressure snapshot: FL={} FR={} RL={} RR={}",
                    sample.vehicleId().vehicleId(),
                    sample.tirePressure().frontLeft(),
                    sample.tirePressure().frontRight(),
                    sample.tirePressure().rearLeft(),
                    sample.tirePressure().rearRight());
        }

        if (sample.cabinGasLevel() != null && isToxicGas(sample.cabinGasLevel())) {
            LOGGER.error("☣️ Cabin gas alert for vehicle {}: {} concentration {} ppm",
                    sample.vehicleId().vehicleId(),
                    sample.cabinGasLevel().type(),
                    sample.cabinGasLevel().concentrationPpm());
        }

        if (sample.accelerationVector() != null && sample.accelerationVector().isHarshEvent()) {
            LOGGER.info("🌀 Driving behavior event detected on vehicle {} with accel vector {}",
                    sample.vehicleId().vehicleId(), sample.accelerationVector());
        }
    }

    /**
     * Event listener for TelemetryFlushedEvent.
     * <p>
     *     This method is triggered when telemetry data is flushed.
     * </p>
     *
     * @param event the {@link TelemetryFlushedEvent} event.
     */
    @EventListener
    public void on(TelemetryFlushedEvent event) {
        LOGGER.info("Telemetry data flushed for aggregate ID {}. {} records flushed at {}",
                   event.telemetryAggregateId(), event.count(), event.flushedAt());

        // Business logic: Data archival and analytics processing
        LOGGER.info("📦 TELEMETRY BATCH PROCESSED: {} records archived for aggregate {}",
                   event.count(), event.telemetryAggregateId());

        // TODO: Integrate with Analytics BC for batch processing of telemetry data
        // TODO: Integrate with Storage BC for long-term data archival
        // TODO: Integrate with Reporting BC for performance analytics
    }



    /**
     * Check if telemetry data indicates urgent issues.
     *
     * @param event the telemetry sample event
     * @return true if urgent, false otherwise
     */
    private boolean isUrgentTelemetryData(TelemetrySampleIngestedEvent event) {
        var sample = event.sample();
        // Basic urgent condition checks
        return sample.severity().name().equals("CRITICAL") ||
               sample.severity().name().equals("HIGH") ||
               sample.dtc() != null; // Fault codes always require attention
    }

    /**
     * Check if telemetry data indicates performance degradation.
     *
     * @param sample the telemetry sample
     * @return true if performance impact detected, false otherwise
     */
    private boolean isPerformanceImpactTelemetry(com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample sample) {
        // Check for performance-related telemetry types and moderate severity
        return sample.severity().name().equals("MEDIUM") ||
               (sample.type().name().equals("ENGINE") && sample.severity().name().equals("LOW"));
    }

    private boolean hasCriticalTirePressure(com.safecar.platform.workshop.domain.model.valueobjects.TirePressure pressure) {
        if (pressure == null) return false;
        var readings = new java.math.BigDecimal[] {
                pressure.frontLeft(), pressure.frontRight(), pressure.rearLeft(), pressure.rearRight()
        };
        java.math.BigDecimal min = null;
        java.math.BigDecimal max = null;
        for (var value : readings) {
            if (value == null) continue;
            min = (min == null || value.compareTo(min) < 0) ? value : min;
            max = (max == null || value.compareTo(max) > 0) ? value : max;
            if (value.compareTo(java.math.BigDecimal.valueOf(25)) < 0 ||
                value.compareTo(java.math.BigDecimal.valueOf(45)) > 0) {
                return true;
            }
        }
        return min != null && max != null && max.subtract(min).compareTo(java.math.BigDecimal.valueOf(6)) > 0;
    }

    private boolean isToxicGas(com.safecar.platform.workshop.domain.model.valueobjects.CabinGasLevel cabinGasLevel) {
        return cabinGasLevel.concentrationPpm().compareTo(java.math.BigDecimal.valueOf(800)) > 0;
    }
}
