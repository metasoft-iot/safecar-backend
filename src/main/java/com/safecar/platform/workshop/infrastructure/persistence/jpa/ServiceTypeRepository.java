package com.safecar.platform.workshop.infrastructure.persistence.jpa;

import com.safecar.platform.workshop.domain.model.entities.ServiceType;
import com.safecar.platform.workshop.domain.model.valueobjects.ServiceTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ServiceTypeRepository
 * <p>
 *     This interface represents the repository for ServiceType entities.
 *     It follows the same pattern as RoleRepository in IAM bounded context.
 * </p>
 */
@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    
    /**
     * Find a service type by its name
     * @param name the service type name
     * @return Optional containing the service type if found
     */
    Optional<ServiceType> findByName(ServiceTypes name);
    
    /**
     * Check if a service type exists by its name
     * @param name the service type name
     * @return true if exists, false otherwise
     */
    boolean existsByName(ServiceTypes name);
}
