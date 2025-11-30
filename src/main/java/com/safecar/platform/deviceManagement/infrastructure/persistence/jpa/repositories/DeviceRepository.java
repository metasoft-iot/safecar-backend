package com.safecar.platform.deviceManagement.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    // 1. VALIDACIÓN: Para evitar registrar dos veces la misma MAC física
    // Spring crea la query: SELECT count(*) > 0 FROM devices WHERE mac_address = ?
    boolean existsByMacAddress(String macAddress);

    // 2. IOT INGEST: Para buscar el dispositivo cuando el Edge envía datos (usa la
    // MAC)
    // Spring crea la query: SELECT * FROM devices WHERE mac_address = ?
    Optional<Device> findByMacAddress(String macAddress);

    // 3. RELACIÓN: Para buscar todos los sensores instalados en un Vehículo
    // específico
    // Como en tu entidad Device definimos 'private Long vehicleId', aquí pasas el
    // Long directo.
    List<Device> findByVehicleId(Long vehicleId);

}
