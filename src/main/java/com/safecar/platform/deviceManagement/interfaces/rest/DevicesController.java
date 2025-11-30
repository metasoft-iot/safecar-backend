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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping
    public ResponseEntity<DeviceResource> createDevice(@RequestBody CreateDeviceResource resource) {
        // 1. Usamos el Assembler para crear el Command
        var command = CreateDeviceCommandFromResourceAssembler.toCommandFromResource(resource);

        var result = deviceCommandService.handle(command);

        if (result.isEmpty())
            return ResponseEntity.badRequest().build();

        // 2. Resolver placa
        String licensePlate = resolveLicensePlate(result.get());

        // 3. Usamos el Assembler para crear la respuesta
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get(), licensePlate);

        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResource> getDeviceById(@PathVariable String deviceId) {
        var query = new GetDeviceByIdQuery(deviceId);
        var result = deviceQueryService.handle(query);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        // 2. Resolver placa
        String licensePlate = resolveLicensePlate(result.get());

        // 3. Assembler de salida
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get(), licensePlate));
    }

    @GetMapping("/search")
    public ResponseEntity<DeviceResource> getDeviceByMac(@RequestParam String macAddress) {
        var query = new GetDeviceByMacAddressQuery(macAddress);
        var result = deviceQueryService.handle(query);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        // 2. Resolver placa
        String licensePlate = resolveLicensePlate(result.get());

        // 3. Assembler de salida
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get(), licensePlate));
    }

    @GetMapping
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

    @PutMapping("/{deviceId}")
    public ResponseEntity<DeviceResource> updateDevice(@PathVariable String deviceId,
            @RequestBody UpdateDeviceResource resource) {
        var command = UpdateDeviceCommandFromResourceAssembler.toCommandFromResource(Long.parseLong(deviceId),
                resource);
        var result = deviceCommandService.handle(command);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        String licensePlate = resolveLicensePlate(result.get());
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get(), licensePlate));
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<?> deleteDevice(@PathVariable String deviceId) {
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
