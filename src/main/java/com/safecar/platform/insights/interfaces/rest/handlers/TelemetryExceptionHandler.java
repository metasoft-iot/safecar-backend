package com.safecar.platform.insights.interfaces.rest.handlers;

import com.safecar.platform.insights.domain.exceptions.TelemetryAnalysisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.safecar.platform.insights.interfaces.rest")
public class TelemetryExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryExceptionHandler.class);

    @ExceptionHandler(TelemetryAnalysisException.class)
    public ResponseEntity<Map<String, Object>> handleTelemetryAnalysisException(TelemetryAnalysisException ex) {
        LOGGER.error("Telemetry analysis failed", ex);
        var body = Map.<String, Object>of(
                "timestamp", Instant.now().toString(),
                "error", "TelemetryAnalysisError",
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }
}
