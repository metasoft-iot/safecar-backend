package com.safecar.platform.devices.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * Driver aggregate root entity
 * 
 * @summary
 *          This entity represents the driver aggregate root entity.
 *          It uses the inherited Long id from AuditableAbstractAggregateRoot as
 *          its primary key.
 *          Contains the profile id (reference to PersonProfile) and
 *          driver-specific metrics.
 *          The driver is associated with vehicles and can perform vehicle
 *          operations.
 * @see ProfileId
 * @see AuditableAbstractAggregateRoot
 * @since 1.0
 */
@Entity
@Getter
public class Driver extends AuditableAbstractAggregateRoot<Driver> {

    @Embedded
    private ProfileId profileId;

    private Integer totalVehicles;

    /**
     * Default constructor for JPA
     */
    protected Driver() {
        super();
        this.totalVehicles = 0;
    }

    /**
     * Constructor with profile id (Long)
     * 
     * @param profileId the profile id as Long
     */
    public Driver(Long profileId) {
        this();
        this.profileId = ProfileId.of(profileId); // Use factory method with validation
    }

    /**
     * Constructor with ProfileId value object
     * 
     * @param profileId the profile id value object
     * @see ProfileId
     */
    public Driver(ProfileId profileId) {
        this();
        if (profileId == null || !profileId.isValid()) {
            throw new IllegalArgumentException("ProfileId must be valid");
        }
        this.profileId = profileId;
    }

    /**
     * Update metrics when a vehicle is assigned.
     * 
     * @summary
     *          This method increments the total vehicles count.
     */
    public void updateTotalVehicles() {
        this.totalVehicles = this.totalVehicles + 1;
    }
    
    /**
     * Get driver id (uses inherited Long id from AuditableAbstractAggregateRoot)
     * 
     * @return the driver id
     */
    public Long getDriverId() {
        return getId(); // Use inherited getId() method
    }

    /**
     * Get profile id
     * 
     * @return the profile id
     */
    public Long getProfileId() {
        return this.profileId.profileId();
    }

    /**
     * Factory method to create Driver from CreateDriverCommand
     * 
     * @param command the create driver command
     * @return Driver instance
     */
    public static Driver create(CreateDriverCommand command) {
        return new Driver(
                command.profileId());
    }
}