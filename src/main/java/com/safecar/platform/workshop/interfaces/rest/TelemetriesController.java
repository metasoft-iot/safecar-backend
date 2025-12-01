package com.safecar.platform.workshop.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safecar.platform.workshop.domain.model.commands.FlushTelemetryCommand;

import com.safecar.platform.workshop.domain.model.queries.GetTelemetryAlertsBySeverityQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryByVehicleAndRangeQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryRecordByIdQuery;
import com.safecar.platform.workshop.domain.services.VehicleTelemetryCommandService;
import com.safecar.platform.workshop.domain.services.VehicleTelemetryQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.TelemetryRecordResource;
import com.safecar.platform.workshop.interfaces.rest.transform.TelemetryRecordResourceFromEntityAssembler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for handling vehicle telemetry operations.
 */
@RestController
@RequestMapping(value = "/api/v1/telemetry", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Telemetry", description = "Vehicle telemetry endpoints")
public class TelemetriesController {

    /**
     * Command and Query services for vehicle telemetry.
     */
    private final VehicleTelemetryCommandService commandService;
    private final VehicleTelemetryQueryService queryService;

    /**
     * Create a new telemetry sample.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new telemetry sample")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Telemetry sample created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Void> createTelemetrySample(
            @RequestBody com.safecar.platform.workshop.interfaces.rest.resources.CreateTelemetryResource resource) {
        var command = com.safecar.platform.workshop.interfaces.rest.transform.CreateTelemetryCommandFromResourceAssembler
                .toCommandFromResource(resource);
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
     * Bulk delete telemetry records for an aggregate id.
     */
    @DeleteMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Bulk delete telemetry records for an aggregate id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telemetry records deleted"),
            @ApiResponse(responseCode = "404", description = "Aggregate not found")
    })
    public ResponseEntity<Long> bulkDeleteTelemetry(@RequestBody FlushTelemetryCommand command) {
        var count = commandService.handle(command);
        return ResponseEntity.ok(count);
    }

    /*
     * Get telemetry record by id.
     */
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get telemetry record by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telemetry record found"),
            @ApiResponse(responseCode = "404", description = "Telemetry record not found")
    })
    public ResponseEntity<TelemetryRecordResource> getTelemetryById(@PathVariable Long id) {
        var query = new GetTelemetryRecordByIdQuery(id);
        var maybe = queryService.handle(query);
        if (maybe.isEmpty())
            return ResponseEntity.notFound().build();
        var resource = TelemetryRecordResourceFromEntityAssembler.toResourceFromEntity(maybe.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Get telemetry records by device id.
     */
    @GetMapping(value = "/device/{deviceId}")
    @Operation(summary = "Get telemetry records by device id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telemetry records found"),
            @ApiResponse(responseCode = "404", description = "Device not found or no records")
    })
    public ResponseEntity<List<TelemetryRecordResource>> getTelemetryByDeviceId(@PathVariable String deviceId) {
        var query = new com.safecar.platform.workshop.domain.model.queries.GetTelemetryByDeviceIdQuery(deviceId);
        var records = queryService.handle(query);

        if (records.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = records.stream()
                .map(TelemetryRecordResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    /**
     * Get telemetry records for vehicle (optionally filtered by time range).
     * If from and to are not provided, returns all telemetry records for the
     * vehicle (most recent first).
     *
     * @param vehicleId The ID of the vehicle.
     * @param from      Optional start time of the range.
     * @param to        Optional end time of the range.
     * @return A list of telemetry records for the vehicle.
     */
    @GetMapping
    @Operation(summary = "Get telemetry records for vehicle (optionally filtered by time range)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telemetry records found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<List<TelemetryRecordResource>> getTelemetryByVehicleAndRange(
            @RequestParam Long vehicleId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        var vehicle = new com.safecar.platform.workshop.domain.model.valueobjects.VehicleId(vehicleId);

        List<com.safecar.platform.workshop.domain.model.entities.TelemetryRecord> records;

        // If both from and to are provided, use range query
        if (from != null && to != null) {
            var query = new GetTelemetryByVehicleAndRangeQuery(vehicle, from, to);
            records = queryService.handle(query);
        } else {
            // Otherwise, get all telemetry for the vehicle (most recent first)
            var query = new com.safecar.platform.workshop.domain.model.queries.GetTelemetryByVehicleQuery(vehicle);
            records = queryService.handle(query);
        }

        var resources = records.stream().map(TelemetryRecordResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    /**
     * Get telemetry alerts by severity in range.
     *
     * @param severity The severity level of the alerts.
     * @param from     The start time of the range.
     * @param to       The end time of the range.
     * @return A list of telemetry alerts matching the criteria.
     */
    @GetMapping(value = "/alerts")
    @Operation(summary = "Get telemetry alerts by severity in range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telemetry alerts found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<List<TelemetryRecordResource>> getAlertsBySeverity(
            @RequestParam com.safecar.platform.workshop.domain.model.valueobjects.AlertSeverity severity,
            @RequestParam Instant from, @RequestParam Instant to) {
        var query = new GetTelemetryAlertsBySeverityQuery(severity, from, to);
        var records = queryService.handle(query);
        var resources = records.stream().map(TelemetryRecordResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }
}
