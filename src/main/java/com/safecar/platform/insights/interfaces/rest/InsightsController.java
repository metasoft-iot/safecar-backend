package com.safecar.platform.insights.interfaces.rest;

import com.safecar.platform.insights.domain.model.queries.GetVehicleInsightByVehicleIdQuery;
import com.safecar.platform.insights.domain.services.VehicleInsightCommandService;
import com.safecar.platform.insights.domain.services.VehicleInsightQueryService;
import com.safecar.platform.insights.interfaces.rest.resources.VehicleInsightResource;
import com.safecar.platform.insights.interfaces.rest.transform.VehicleInsightResourceFromEntityAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v1/insights", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Insights", description = "Vehicle telemetry analysis and insight generation endpoints")
public class InsightsController {

    private final VehicleInsightCommandService commandService;
    private final VehicleInsightQueryService queryService;

    @PostMapping("/generate/{telemetryId}")
    @Operation(summary = "Generate vehicle insight", description = "Generates a new insight for a vehicle based on a telemetry sample ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Insight generated successfully", content = @Content(schema = @Schema(implementation = VehicleInsightResource.class))),
            @ApiResponse(responseCode = "404", description = "Telemetry sample not found")
    })
    public ResponseEntity<VehicleInsightResource> generateInsight(@PathVariable Long telemetryId) {
        var command = new com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand(
                telemetryId);
        var insight = commandService.handle(command);
        var response = VehicleInsightResourceFromEntityAssembler.toResource(insight);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get latest vehicle insight", description = "Retrieves the most recent insight generated for a specific vehicle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insight found", content = @Content(schema = @Schema(implementation = VehicleInsightResource.class))),
            @ApiResponse(responseCode = "404", description = "Insight not found for the vehicle")
    })
    public ResponseEntity<VehicleInsightResource> getLatestInsight(@PathVariable Long vehicleId) {
        var optional = queryService.handle(new GetVehicleInsightByVehicleIdQuery(vehicleId));
        return optional
                .map(VehicleInsightResourceFromEntityAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
