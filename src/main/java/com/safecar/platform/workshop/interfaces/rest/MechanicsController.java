package com.safecar.platform.workshop.interfaces.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecar.platform.workshop.domain.model.commands.AssignMechanicToWorkshopCommand;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;
import com.safecar.platform.workshop.domain.services.MechanicQueryService;
import com.safecar.platform.workshop.domain.model.queries.GetMechanicByProfileIdQuery;
import com.safecar.platform.workshop.interfaces.rest.resources.AssignMechanicToWorkshopResource;
import com.safecar.platform.workshop.interfaces.rest.resources.MechanicResource;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateMechanicMetricsResource;
import com.safecar.platform.workshop.interfaces.rest.transform.MechanicResourceFromEntityAssembler;
import com.safecar.platform.workshop.interfaces.rest.transform.UpdateMechanicMetricsCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/api/v1/mechanics", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Mechanics", description = "Mechanic management endpoints")
public class MechanicsController {

    private final MechanicCommandService commandService;
    private final MechanicQueryService queryService;

    public MechanicsController(MechanicCommandService commandService, MechanicQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Get mechanic by profile ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mechanic found"),
            @ApiResponse(responseCode = "404", description = "Mechanic not found")
    })
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<MechanicResource> getMechanicByProfileId(@PathVariable Long profileId) {
        var query = new GetMechanicByProfileIdQuery(profileId);
        var mechanic = queryService.handle(query);

        if (mechanic.isEmpty())
            return ResponseEntity.notFound().build();

        var mechanicResource = MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic.get());
        return ResponseEntity.ok(mechanicResource);
    }

    @Operation(summary = "Update mechanic")
    @PatchMapping("/{mechanicId}")
    public ResponseEntity<MechanicResource> updateMechanicMetrics(
            @PathVariable Long mechanicId,
            @RequestBody UpdateMechanicMetricsResource resource) {

        var command = UpdateMechanicMetricsCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command, mechanicId);

        if (result == null || result.isEmpty())
            return ResponseEntity.notFound().build();

        var mechanic = result.get();
        var mechanicResource = MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic);

        return ResponseEntity.ok(mechanicResource);
    }

    @Operation(summary = "Assign mechanic to workshop", 
               description = "Assigns a mechanic to a specific workshop. A mechanic can only belong to one workshop.")
    @PatchMapping("/{mechanicId}/workshop")
    public ResponseEntity<MechanicResource> assignMechanicToWorkshop(
            @PathVariable Long mechanicId,
            @Valid @RequestBody AssignMechanicToWorkshopResource resource) {

        var command = new AssignMechanicToWorkshopCommand(mechanicId, resource.workshopId());
        var result = commandService.handle(command);

        if (result == null || result.isEmpty())
            return ResponseEntity.notFound().build();

        var mechanic = result.get();
        var mechanicResource = MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic);

        return ResponseEntity.ok(mechanicResource);
    }
}
