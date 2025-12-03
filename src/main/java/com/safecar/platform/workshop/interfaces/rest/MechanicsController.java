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
import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/api/v1/mechanics", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Mechanics", description = "Mechanic management endpoints")
public class MechanicsController {

    private final MechanicCommandService commandService;
    private final MechanicQueryService queryService;
    private final ProfilesContextFacade profilesContextFacade; // Changed type and name

    public MechanicsController(
            MechanicCommandService commandService,
            MechanicQueryService queryService,
            ProfilesContextFacade profilesContextFacade) { // Changed type and name
        this.commandService = commandService;
        this.queryService = queryService;
        this.profilesContextFacade = profilesContextFacade; // Changed assignment
    }

    @Operation(summary = "Get mechanic by profile ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mechanic found"),
            @ApiResponse(responseCode = "404", description = "Mechanic not found")
    })
    @GetMapping(params = "profile")
    public ResponseEntity<MechanicResource> getMechanicByProfileId(@RequestParam("profile") Long profileId) {
        var query = new GetMechanicByProfileIdQuery(profileId);
        var mechanic = queryService.handle(query);

        if (mechanic.isEmpty())
            return ResponseEntity.notFound().build();

        var fullName = getFullNameForMechanic(mechanic.get().getProfileId());
        var mechanicResource = MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic.get(), fullName);
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
        var fullName = getFullNameForMechanic(mechanic.getProfileId());
        var mechanicResource = MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic, fullName);

        return ResponseEntity.ok(mechanicResource);
    }

    @Operation(summary = "Assign mechanic to workshop", description = "Assigns a mechanic to a specific workshop. A mechanic can only belong to one workshop.")
    @PatchMapping("/{mechanicId}/workshop")
    public ResponseEntity<MechanicResource> assignMechanicToWorkshop(
            @PathVariable Long mechanicId,
            @Valid @RequestBody AssignMechanicToWorkshopResource resource) {

        var command = new AssignMechanicToWorkshopCommand(mechanicId, resource.workshopId());
        var result = commandService.handle(command);

        if (result == null || result.isEmpty())
            return ResponseEntity.notFound().build();

        var mechanic = result.get();
        var fullName = getFullNameForMechanic(mechanic.getProfileId());
        var mechanicResource = MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic, fullName);

        return ResponseEntity.ok(mechanicResource);
    }

    @Operation(summary = "Get mechanics by workshop ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mechanics found"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @GetMapping(params = "workshop")
    public ResponseEntity<java.util.List<MechanicResource>> getMechanicsByWorkshopId(
            @RequestParam("workshop") Long workshopId) {
        var query = new com.safecar.platform.workshop.domain.model.queries.GetMechanicsByWorkshopIdQuery(workshopId);
        var mechanics = queryService.handle(query);

        var mechanicResources = mechanics.stream()
                .map(mechanic -> {
                    var fullName = getFullNameForMechanic(mechanic.getProfileId());
                    return MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic, fullName);
                })
                .toList();

        return ResponseEntity.ok(mechanicResources);
    }

    private String getFullNameForMechanic(Long profileId) {
        return profilesContextFacade.getPersonFullNameByProfileId(profileId);
    }
}
