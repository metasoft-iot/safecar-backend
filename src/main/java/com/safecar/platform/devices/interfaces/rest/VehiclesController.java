package com.safecar.platform.devices.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByDriverIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehiclesByWorkshopIdQuery;
import com.safecar.platform.devices.domain.services.VehicleCommandService;
import com.safecar.platform.devices.domain.services.VehicleQueryService;
import com.safecar.platform.devices.interfaces.rest.resources.CreateVehicleResource;
import com.safecar.platform.devices.interfaces.rest.resources.VehicleResource;
import com.safecar.platform.devices.interfaces.rest.transform.CreateVehicleCommandFromVehicleResourceAssembler;
import com.safecar.platform.devices.interfaces.rest.transform.VehicleResourceFromEntityAssembler;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing vehicles.
 * <o>
 * Provide ReSTful endpoints for managing the {@link Vehicle} entities.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Vehicle Management Operations")
public class VehiclesController {
        private final VehicleCommandService commandService;
        private final VehicleQueryService vehicleQueryService;

        /**
         * Constructor for VehiclesController.
         * 
         * @param vehicleCommandService the vehicle command service
         * @param vehicleQueryService   the vehicle query service
         */
        public VehiclesController(VehicleCommandService commandService,
                        VehicleQueryService vehicleQueryService) {
                this.commandService = commandService;
                this.vehicleQueryService = vehicleQueryService;
        }

        /**
         * Creates a new vehicle.
         *
         * @param resource the vehicle creation request
         * @return the created vehicle information
         */
        @Operation(summary = "Create a new vehicle", description = "Creates a new vehicle associated with a driver.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Vehicle created successfully", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = VehicleResource.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Vehicle with same license plate already exists", content = @Content)
        })
        @PostMapping("/vehicles")
        public ResponseEntity<VehicleResource> createVehicle(
                        @Parameter @RequestBody CreateVehicleResource resource) {

                var command = CreateVehicleCommandFromVehicleResourceAssembler.toCommandFromVehicleResource(resource);
                var vehicleOpt = commandService.handle(command);

                if (!vehicleOpt.isPresent())
                        return ResponseEntity.badRequest().build();

                var vehicle = vehicleOpt.get();

                return ResponseEntity.status(CREATED)
                                .body(VehicleResourceFromEntityAssembler
                                                .toResourceFromEntity(vehicle));
        }

        /**
         * Retrieves a vehicle by its ID.
         *
         * @param vehicleId the unique identifier of the vehicle
         * @return the vehicle information
         */
        @Operation(summary = "Get vehicle by ID", description = "Retrieves the complete vehicle information.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vehicle retrieved successfully", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = VehicleResource.class))),
                        @ApiResponse(responseCode = "404", description = "Vehicle not found for the specified ID", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Invalid vehicle ID format", content = @Content)
        })
        @GetMapping("/vehicles/{vehicleId}")
        public ResponseEntity<VehicleResource> getVehicleById(
                        @Parameter @PathVariable Long vehicleId) {

                var query = new GetVehicleByIdQuery(vehicleId);
                var vehicleOpt = vehicleQueryService.handle(query);

                if (vehicleOpt.isEmpty())
                        return ResponseEntity.notFound().build();

                var vehicle = vehicleOpt.get();

                return ResponseEntity.ok(VehicleResourceFromEntityAssembler
                                .toResourceFromEntity(vehicle));
        }

        /**
         * Retrieves all vehicles associated with a specific driver.
         *
         * @param driverId the unique identifier of the driver
         * @return list of vehicles owned by the driver
         */
        @Operation(summary = "Get vehicles by driver ID", description = "Retrieves all vehicles associated with a specific driver ID. ")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully (may be empty list)", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = VehicleResource.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid driver ID format", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Driver not found", content = @Content)
        })
        @GetMapping(value = "/drivers/{driverId}/vehicles")
        public ResponseEntity<List<VehicleResource>> getVehiclesByDriverId(
                        @Parameter @PathVariable Long driverId) {

                var query = new GetVehicleByDriverIdQuery(driverId);
                var vehicles = vehicleQueryService.handle(query);

                if (vehicles == null || vehicles.isEmpty())
                        return ResponseEntity.notFound().build();

                var vehicleResources = vehicles.stream()
                                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                                .toList();

                return ResponseEntity.ok(vehicleResources);
        }

        /**
         * Retrieves all vehicles associated with a specific workshop.
         *
         * @param workshopId the unique identifier of the workshop
         * @return list of vehicles that have appointments at the workshop
         */
        @Operation(summary = "Get vehicles by workshop ID", description = "Retrieves all vehicles that have appointments at a specific workshop.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully (may be empty list)", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = VehicleResource.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid workshop ID format", content = @Content)
        })
        @GetMapping(value = "/workshops/{workshopId}/vehicles")
        public ResponseEntity<List<VehicleResource>> getVehiclesByWorkshopId(
                        @Parameter @PathVariable Long workshopId) {

                var query = new GetVehiclesByWorkshopIdQuery(workshopId);
                var vehicles = vehicleQueryService.handle(query);

                var vehicleResources = vehicles.stream()
                                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                                .toList();

                return ResponseEntity.ok(vehicleResources);
        }
}
