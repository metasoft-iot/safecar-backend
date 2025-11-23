package com.safecar.platform.insights.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleInsightRepository extends JpaRepository<VehicleInsight, Long> {

    Optional<VehicleInsight> findTopByVehicleVehicleIdOrderByGeneratedAtDesc(Long vehicleId);
}
