package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.workshop.domain.model.commands.CreateWorkshopCommand;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * Workshop Aggregate
 * <p>
 * Represents a workshop (taller) in the system with its business information
 * and relationship to a business profile.
 * </p>
 */
@Getter
@Entity
public class Workshop extends AuditableAbstractAggregateRoot<Workshop> {

    /**
     * Business Profile ID - Reference to the business profile that owns this workshop
     */
    @Embedded
    private ProfileId businessProfileId;

    /**
     * Workshop description - Can be blank
     * Additional information specific to the workshop operation
     */
    private String workshopDescription;

    /**
     * Total mechanics working in this workshop
     */
    private Integer totalMechanics;

    /**
     * Default constructor for JPA
     */
    protected Workshop() {
        super();
        this.totalMechanics = 0;
    }

    /**
     * Constructor with business profile id (Long)
     * 
     * @param businessProfileId the business profile id as Long
     * @param workshopDescription the workshop description
     */
    public Workshop(Long businessProfileId, String workshopDescription) {
        this();
        this.businessProfileId = ProfileId.of(businessProfileId);
        this.workshopDescription = workshopDescription;
    }

    /**
     * Constructor from CreateWorkshopCommand
     * 
     * @param command the create workshop command
     */
    public Workshop(CreateWorkshopCommand command) {
        this();
        this.businessProfileId = ProfileId.of(command.businessProfileId());
        this.workshopDescription = command.workshopDescription();
    }

    /**
     * Update metrics when a mechanic is assigned.
     */
    public void incrementMechanicsCount() {
        this.totalMechanics = this.totalMechanics + 1;
    }

    /**
     * Update metrics when a mechanic is removed.
     */
    public void decrementMechanicsCount() {
        if (this.totalMechanics > 0) {
            this.totalMechanics = this.totalMechanics - 1;
        }
    }

    /**
     * Get business profile ID as Long
     * 
     * @return the business profile ID
     */
    public Long getBusinessProfileId() {
        return this.businessProfileId.profileId();
    }

    /**
     * Update workshop description.
     * Trims value; ignores null or blank to avoid accidental clearing.
     *
     * @param newDescription new description text
     */
    public void updateDescription(String newDescription) {
        if (newDescription == null) return;
        var trimmed = newDescription.trim();
        if (trimmed.isEmpty()) return;
        this.workshopDescription = trimmed;
    }
}