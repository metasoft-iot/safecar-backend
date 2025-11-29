package com.safecar.platform.deviceManagement.domain.services;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Device;
import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByIdQuery;
import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByMacAddressQuery;

import java.util.Optional;

public interface DeviceQueryService {

    Optional<Device> handle(GetDeviceByMacAddressQuery query);

    Optional<Device> handle(GetDeviceByIdQuery query);
}