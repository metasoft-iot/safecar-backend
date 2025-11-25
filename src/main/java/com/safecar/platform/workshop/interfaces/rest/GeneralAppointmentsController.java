package com.safecar.platform.workshop.interfaces.rest;

import com.safecar.platform.workshop.domain.model.queries.GetAppointmentsByDriverIdQuery;
import com.safecar.platform.workshop.domain.services.AppointmentQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.AppointmentResource;
import com.safecar.platform.workshop.interfaces.rest.transform.AppointmentResourceFromAggregateAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * General Appointments Controller
 * <p>
 * Provides endpoints for querying appointments across the system,
 * not tied to a specific workshop context.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "General appointment query endpoints")
public class GeneralAppointmentsController {

    private final AppointmentQueryService queryService;

    /**
     * Get all appointments with optional driver ID filter.
     *
     * @param driverId optional driver ID to filter by
     * @return ResponseEntity with a list of appointment resources
     */
    @GetMapping
    @Operation(summary = "Get appointments", description = "Get all appointments or filter by driver ID")
    public ResponseEntity<List<AppointmentResource>> getAppointments(
            @RequestParam(required = false) Long driverId) {

        if (driverId != null) {
            // Filter by driver
            var query = new GetAppointmentsByDriverIdQuery(driverId);
            var appointments = queryService.handle(query);

            var resources = appointments.stream()
                    .map(AppointmentResourceFromAggregateAssembler::toResourceFromAggregate)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(resources);
        } else {
            // Return all appointments (or implement pagination if needed)
            // For now, return empty list or implement GetAllAppointmentsQuery
            return ResponseEntity.ok(List.of());
        }
    }
}
