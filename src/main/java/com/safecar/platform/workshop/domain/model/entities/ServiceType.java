package com.safecar.platform.workshop.domain.model.entities;

import com.safecar.platform.workshop.domain.model.valueobjects.ServiceTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

/**
 * ServiceType
 * <p>
 *     This class represents a service type entity that wraps the ServiceTypes enum.
 *     It follows the same pattern as Role entity in IAM bounded context.
 * </p>
 */
@Getter
@Entity
public class ServiceType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 30)
    private ServiceTypes name;
    
    public ServiceType() {
    }
    
    public ServiceType(ServiceTypes name) {
        this.name = name;
    }
    
    /**
     * Returns the default service type (GENERAL_MAINTENANCE)
     * @return ServiceType with GENERAL_MAINTENANCE
     */
    public static ServiceType getDefaultServiceType() {
        return new ServiceType(ServiceTypes.GENERAL_MAINTENANCE);
    }
    
    /**
     * Validates the service type, returning default if null
     * @param serviceType the service type to validate
     * @return the service type or default if null
     */
    public static ServiceType validateServiceType(ServiceType serviceType) {
        return serviceType == null ? getDefaultServiceType() : serviceType;
    }
    
    /**
     * Creates a ServiceType from a string name
     * @param name the service type name
     * @return ServiceType instance
     */
    public static ServiceType toServiceTypeFromName(String name) {
        return new ServiceType(ServiceTypes.valueOf(name));
    }
    
    /**
     * Returns the string representation of the service type name
     * @return the service type name as string
     */
    public String getStringName() {
        return this.name.name();
    }
    
    /**
     * Creates a ServiceType instance from a string name
     * @param name the service type name
     * @return ServiceType instance
     */
    public static ServiceType create(String name) {
        return new ServiceType(ServiceTypes.valueOf(name));
    }
    
    /**
     * Returns all service types except CUSTOM
     * @return List of standard service types
     */
    public static List<ServiceTypes> getStandardServiceTypes() {
        return List.of(
            ServiceTypes.OIL_CHANGE,
            ServiceTypes.BRAKE_SERVICE,
            ServiceTypes.TIRE_SERVICE,
            ServiceTypes.ENGINE_DIAGNOSTICS,
            ServiceTypes.TRANSMISSION_SERVICE,
            ServiceTypes.ELECTRICAL_REPAIR,
            ServiceTypes.AIR_CONDITIONING,
            ServiceTypes.SUSPENSION_REPAIR,
            ServiceTypes.GENERAL_MAINTENANCE
        );
    }
}
