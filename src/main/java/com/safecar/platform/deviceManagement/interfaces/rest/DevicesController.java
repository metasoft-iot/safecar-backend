package com.safecar.platform.deviceManagement.interfaces.rest;

import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByIdQuery;
import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByMacAddressQuery;
import com.safecar.platform.deviceManagement.domain.services.DeviceCommandService;
import com.safecar.platform.deviceManagement.domain.services.DeviceQueryService;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.CreateDeviceResource;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.DeviceResource;
import com.safecar.platform.deviceManagement.interfaces.rest.transform.CreateDeviceCommandFromResourceAssembler;
import com.safecar.platform.deviceManagement.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
public class DevicesController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public DevicesController(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @PostMapping
    public ResponseEntity<DeviceResource> createDevice(@RequestBody CreateDeviceResource resource) {
        // 1. Usamos el Assembler para crear el Command
        var command = CreateDeviceCommandFromResourceAssembler.toCommandFromResource(resource);

        var result = deviceCommandService.handle(command);

        if (result.isEmpty()) return ResponseEntity.badRequest().build();

        // 2. Usamos el Assembler para crear la respuesta
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get());

        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResource> getDeviceById(@PathVariable String deviceId) {
        var query = new GetDeviceByIdQuery(deviceId);
        var result = deviceQueryService.handle(query);

        if (result.isEmpty()) return ResponseEntity.notFound().build();

        // 2. Assembler de salida
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get()));
    }

    @GetMapping("/search")
    public ResponseEntity<DeviceResource> getDeviceByMac(@RequestParam String macAddress) {
        var query = new GetDeviceByMacAddressQuery(macAddress);
        var result = deviceQueryService.handle(query);

        if (result.isEmpty()) return ResponseEntity.notFound().build();

        // 2. Assembler de salida
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(result.get()));
    }
}