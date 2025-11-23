package com.safecar.platform.workshop.interfaces.rest;

import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetAllWorkshopsQuery;
import com.safecar.platform.workshop.domain.services.WorkshopCommandService;
import com.safecar.platform.workshop.domain.services.WorkshopQueryService;
import com.safecar.platform.workshop.domain.services.MechanicQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.WorkshopResource;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateWorkshopResource;
import com.safecar.platform.workshop.interfaces.rest.resources.MechanicResource;
import com.safecar.platform.workshop.interfaces.rest.transform.WorkshopEntityResourceFromEntityAssembler;
import com.safecar.platform.workshop.interfaces.rest.transform.UpdateWorkshopCommandFromResourceAssembler;
import com.safecar.platform.workshop.interfaces.rest.transform.MechanicResourceFromEntityAssembler;
import com.safecar.platform.workshop.domain.model.queries.GetMechanicsByWorkshopIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByBusinessProfileIdQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Workshop Entity Controller
 * Handles Workshop aggregate operations separate from WorkshopOperation
 * operations
 * Route: /api/v1/workshop (Workshop entity management)
 * Note: /api/v1/workshop-operations is used by WorkshopOperationsController for
 * operations/metrics
 */
@RestController
@RequestMapping(value = "/api/v1/workshops", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Workshops", description = "Workshop entity management endpoints")
public class WorkshopsController {

    private final WorkshopCommandService workshopCommandService;
    private final WorkshopQueryService workshopQueryService;
    private final MechanicQueryService mechanicQueryService;

    public WorkshopsController(WorkshopCommandService workshopCommandService,
            WorkshopQueryService workshopQueryService,
            MechanicQueryService mechanicQueryService) {
        this.workshopCommandService = workshopCommandService;
        this.workshopQueryService = workshopQueryService;
        this.mechanicQueryService = mechanicQueryService;
    }

    /**
     * Gets all workshops.
     *
     * @return the list of {@link WorkshopResource} resources
     */
    @Operation(summary = "Get all workshops")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workshops found")
    })
    @GetMapping
    public ResponseEntity<java.util.List<WorkshopResource>> getAllWorkshops() {
        var query = new GetAllWorkshopsQuery();
        var workshops = workshopQueryService.handle(query);
        
        return ResponseEntity.ok(workshops.stream()
                .map(WorkshopEntityResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    /**
     * Gets a workshop by its ID.
     *
     * @param workshopId the workshop ID
     * @return the {@link WorkshopResource} resource
     */
    @Operation(summary = "Get workshop by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workshop found"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @GetMapping("/{workshopId}")
    public ResponseEntity<WorkshopResource> getWorkshopById(
            @Parameter(required = true) @PathVariable Long workshopId) {

        var getWorkshopByIdQuery = new GetWorkshopByIdQuery(workshopId);
        var workshop = workshopQueryService.handle(getWorkshopByIdQuery);

        if (workshop.isEmpty())
            return ResponseEntity.notFound().build();

        var workshopResource = WorkshopEntityResourceFromEntityAssembler
                .toResourceFromEntity(workshop.get());
        return ResponseEntity.ok(workshopResource);
    }

    /**
     * Partially update a workshop (currently only description).
     */
    @Operation(summary = "Update workshop")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workshop updated"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @PatchMapping("/{workshopId}")
    public ResponseEntity<WorkshopResource> updateWorkshop(
            @PathVariable Long workshopId,
            @RequestBody UpdateWorkshopResource resource) {

        var command = UpdateWorkshopCommandFromResourceAssembler.toCommandFromResource(workshopId, resource);
        var result = workshopCommandService.handle(command);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        var workshopResource = WorkshopEntityResourceFromEntityAssembler.toResourceFromEntity(result.get());
        return ResponseEntity.ok(workshopResource);
    }

    /**
     * Get mechanics by workshop ID.
     */
    @Operation(summary = "Get mechanics by workshop ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mechanics found"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @GetMapping("/{workshopId}/mechanics")
    public ResponseEntity<java.util.List<MechanicResource>> getMechanicsByWorkshopId(
            @PathVariable Long workshopId) {

        var getMechanicsByWorkshopIdQuery = new GetMechanicsByWorkshopIdQuery(workshopId);
        var mechanics = mechanicQueryService.handle(getMechanicsByWorkshopIdQuery);

        return ResponseEntity.ok(mechanics.stream()
                .map(MechanicResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    /**
     * Get workshop by business profile ID.
     */
    @Operation(summary = "Get workshop by business profile ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workshop found"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @GetMapping("/by-business-profile/{businessProfileId}")
    public ResponseEntity<WorkshopResource> getWorkshopByBusinessProfileId(
            @PathVariable Long businessProfileId) {

        var query = new GetWorkshopByBusinessProfileIdQuery(businessProfileId);
        var workshop = workshopQueryService.handle(query);

        if (workshop.isEmpty())
            return ResponseEntity.notFound().build();

        var workshopResource = WorkshopEntityResourceFromEntityAssembler.toResourceFromEntity(workshop.get());
        return ResponseEntity.ok(workshopResource);
    }
}