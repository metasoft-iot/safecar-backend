package com.safecar.platform.insights.domain.exceptions;

/**
 * Domain exception used when telemetry cannot be analyzed by the AI provider.
 */
public class TelemetryAnalysisException extends RuntimeException {

    public TelemetryAnalysisException(String message) {
        super(message);
    }

    public TelemetryAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
