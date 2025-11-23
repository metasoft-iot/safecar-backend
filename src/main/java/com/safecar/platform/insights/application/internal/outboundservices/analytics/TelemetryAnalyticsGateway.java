package com.safecar.platform.insights.application.internal.outboundservices.analytics;

import com.safecar.platform.insights.domain.model.valueobjects.VehicleReference;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetryInsightResult;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetrySensorPayload;

import java.util.List;

/**
 * Telemetry Analytics Gateway
 * 
 * <p>
 * This interface provides the telemetry analytics gateway.
 * </p>
 * 
 */
public interface TelemetryAnalyticsGateway {

    TelemetryInsightResult analyze(VehicleReference vehicle,
            List<TelemetrySensorPayload> payloads);
}
