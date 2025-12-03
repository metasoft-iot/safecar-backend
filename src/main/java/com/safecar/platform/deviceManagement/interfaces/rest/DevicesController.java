package com.safecar.platform.deviceManagement.interfaces.rest;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Device;
import com.safecar.platform.deviceManagement.domain.model.commands.DeleteDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.commands.UpdateDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.queries.GetAllDevicesQuery;
import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByIdQuery;
import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByMacAddressQuery;
import com.safecar.platform.deviceManagement.domain.services.DeviceCommandService;
import com.safecar.platform.deviceManagement.domain.services.DeviceQueryService;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.CreateDeviceResource;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.DeviceResource;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.UpdateDeviceResource;
import com.safecar.platform.deviceManagement.interfaces.rest.transform.CreateDeviceCommandFromResourceAssembler;
import com.safecar.platform.deviceManagement.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.safecar.platform.deviceManagement.interfaces.rest.transform.UpdateDeviceCommandFromResourceAssembler;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.devices.domain.services.VehicleQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "Device management endpoints")
public class DevicesController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;
    private final VehicleQueryService vehicleQueryService;

    public DevicesController(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService,
            VehicleQueryService vehicleQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
        this.vehicleQueryService = vehicleQueryService;
    }

    /**
     * Create a new IoT device.
     */
    @PostMapping
    @Operation(summary = "Create a new device", description = "Registers a new IoT device (ESP32) in the system with its MAC address, type, and associated vehicle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid device data or validation errors", content = @Content),
            @ApiResponse(responseCode = "409", description = "Device with this MAC address already exists", content = @Content)
    })
    public ResponseEntity<DeviceResource> createDevice(
            @Parameter(description = "Device creation data including MAC address, device type, and license plate") @RequestBody CreateDeviceResource resource) {
        var command = CreateDeviceCommandFromResourceAssembler.toCommandFromResource(resource);

        var result = deviceCommandService.handle(command);

        if (result.isEmpty())
            return ResponseEntity.badRequest().build();

        String licensePlate = resolveLicensePlate(result.get());
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get(), licensePlate);

        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    /**
     * Get device by ID.
     */
    @GetMapping("/{deviceId}")
    @Operation(summary = "Get device by ID", description = "Retrieves detailed information about a specific IoT device including its status, type, and associated vehicle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResource.class))),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid device ID format", content = @Content)
    })
    public ResponseEntity<DeviceResource> getDeviceById(
            @Parameter(description = "The unique identifier of the device") @PathVariable String deviceId) {
        var query = new GetDeviceByIdQuery(deviceId);
        var result = deviceQueryService.handle(query);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        String licensePlate = resolveLicensePlate(result.get());
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get(), licensePlate));
    }

    /**
     * Find device by MAC address.
     */
    @GetMapping(params = "mac-address")
    @Operation(summary = "Find device by MAC address", description = "Searches for an IoT device using its unique MAC address identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResource.class))),
            @ApiResponse(responseCode = "404", description = "No device found with the specified MAC address", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid MAC address format", content = @Content)
    })
    public ResponseEntity<DeviceResource> getDeviceByMacAddress(
            @Parameter(description = "MAC address of the device (format: AA:BB:CC:DD:EE:FF)", example = "00:1A:2B:3C:4D:5E") @RequestParam("mac-address") String macAddress) {
        var query = new GetDeviceByMacAddressQuery(macAddress);
        var result = deviceQueryService.handle(query);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        String licensePlate = resolveLicensePlate(result.get());
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get(), licensePlate));
    }

    /**
     * Get all devices.
     */
    @GetMapping
    @Operation(summary = "Get all devices", description = "Retrieves a list of all registered IoT devices in the system with their current status and vehicle associations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully (may be empty list)", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResource.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<DeviceResource>> getAllDevices() {
        var query = new GetAllDevicesQuery();
        var devices = deviceQueryService.handle(query);

        var resources = devices.stream()
                .map(device -> {
                    String licensePlate = resolveLicensePlate(device);
                    return DeviceResourceFromEntityAssembler.toResourceFromEntity(device, licensePlate);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    /**
     * Update device information.
     */
    @PutMapping("/{deviceId}")
    @Operation(summary = "Update device", description = "Updates an existing IoT device's information such as status, vehicle association, or device type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResource.class))),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid device data", content = @Content)
    })
    public ResponseEntity<DeviceResource> updateDevice(
            @Parameter(description = "The unique identifier of the device to update") @PathVariable String deviceId,
            @Parameter(description = "Updated device information") @RequestBody UpdateDeviceResource resource) {
        var command = UpdateDeviceCommandFromResourceAssembler.toCommandFromResource(Long.parseLong(deviceId),
                resource);
        var result = deviceCommandService.handle(command);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        String licensePlate = resolveLicensePlate(result.get());
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get(), licensePlate));
    }

    /**
     * Delete a device.
     */
    @DeleteMapping("/{deviceId}")
    @Operation(summary = "Delete device", description = "Removes an IoT device from the system. This operation is irreversible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content)
    })
    public ResponseEntity<?> deleteDevice(
            @Parameter(description = "The unique identifier of the device to delete") @PathVariable String deviceId) {
        var command = new DeleteDeviceCommand(Long.parseLong(deviceId));
        deviceCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }

    private String resolveLicensePlate(Device device) {
        if (device.getVehicleId() == null) {
            return null;
        }
        var query = new GetVehicleByIdQuery(device.getVehicleId());
        var vehicle = vehicleQueryService.handle(query);
        return vehicle.map(com.safecar.platform.devices.domain.model.aggregates.Vehicle::getLicensePlate).orElse(null);
    }
}
