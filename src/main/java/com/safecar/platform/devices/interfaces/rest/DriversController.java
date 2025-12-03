package com.safecar.platform.devices.interfaces.rest;

import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.model.queries.GetDriverByProfileIdQuery;
import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.devices.domain.services.DriverQueryService;
import com.safecar.platform.devices.interfaces.rest.resources.DriverResource;
import com.safecar.platform.devices.interfaces.rest.transform.DriverResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing drivers.
 * <p>
 * Provides RESTful endpoints for querying driver information.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Drivers", description = "Driver Management Operations")
public class DriversController {
    private final DriverQueryService driverQueryService;
    private final DriverCommandService driverCommandService;

    /**
     * Constructor for DriversController.
     * 
     * @param driverQueryService   the driver query service
     * @param driverCommandService the driver command service
     */
    public DriversController(
            DriverQueryService driverQueryService,
            DriverCommandService driverCommandService) {
        this.driverQueryService = driverQueryService;
        this.driverCommandService = driverCommandService;
    }

    /**
     * Retrieves a driver by their profile ID.
     *
     * @param profileId the unique identifier of the person profile
     * @return the driver information
     */
    @Operation(summary = "Get driver by profile ID", description = "Retrieves driver information associated with a specific person profile ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver retrieved successfully", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = DriverResource.class))),
            @ApiResponse(responseCode = "404", description = "Driver not found for the specified profile ID", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid profile ID format", content = @Content)
    })
    @GetMapping(value = "/drivers", params = "profile")
    public ResponseEntity<DriverResource> getDriverByProfileId(
            @Parameter(description = "The profile ID to search for") @RequestParam("profile") Long profileId) {

        var query = new GetDriverByProfileIdQuery(profileId);
        var driverOpt = driverQueryService.handle(query);

        if (driverOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var driver = driverOpt.get();
        var driverResource = DriverResourceFromEntityAssembler.toResourceFromEntity(driver);

        return ResponseEntity.ok(driverResource);
    }

    /**
     * Manually creates a driver for a profile ID.
     * This endpoint is a fallback in case the automatic event-driven creation
     * fails.
     *
     * @param profileId the unique identifier of the person profile
     * @return the created driver information
     */
    @Operation(summary = "Create driver manually", description = "Manually creates a driver for a specific profile ID. "
            +
            "This is a fallback endpoint in case automatic creation via events fails.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Driver created successfully", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = DriverResource.class))),
            @ApiResponse(responseCode = "409", description = "Driver already exists for this profile", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid profile ID format", content = @Content)
    })
    @PostMapping("/profiles/{profileId}/driver")
    public ResponseEntity<DriverResource> createDriverForProfile(
            @Parameter(description = "The profile ID to create driver for") @PathVariable Long profileId) {

        try {
            var command = new CreateDriverCommand(profileId);
            var driverOpt = driverCommandService.handle(command);

            if (driverOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            var driver = driverOpt.get();
            var driverResource = DriverResourceFromEntityAssembler.toResourceFromEntity(driver);

            return ResponseEntity.status(HttpStatus.CREATED).body(driverResource);
        } catch (IllegalArgumentException e) {
            // Driver already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
