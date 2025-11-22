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

@RestController
@RequestMapping(value = "/api/v1/insights", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InsightsController {

    private final VehicleInsightCommandService commandService;
    private final VehicleInsightQueryService queryService;

    @PostMapping("/generate/{telemetryId}")
    public ResponseEntity<VehicleInsightResource> generateInsight(@PathVariable Long telemetryId) {
        var command = new com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand(
                telemetryId);
        var insight = commandService.handle(command);
        var response = VehicleInsightResourceFromEntityAssembler.toResource(insight);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleInsightResource> getLatestInsight(@PathVariable Long vehicleId) {
        var optional = queryService.handle(new GetVehicleInsightByVehicleIdQuery(vehicleId));
        return optional
                .map(VehicleInsightResourceFromEntityAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
