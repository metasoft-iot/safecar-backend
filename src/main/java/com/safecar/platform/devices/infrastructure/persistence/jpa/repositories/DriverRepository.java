package com.safecar.platform.devices.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByProfileId_ProfileId(Long profileId);
    boolean existsByProfileId_ProfileId(Long profileId);
}