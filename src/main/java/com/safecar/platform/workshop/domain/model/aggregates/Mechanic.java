package com.safecar.platform.workshop.domain.model.aggregates;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.model.entities.Specialization;
import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

/**
 * Mechanic aggregate
 * <p>
 * Represents a mechanic in the workshop domain.
 * A mechanic belongs to a single workshop and has specializations.
 * </p>
 */
@Entity
@Getter
public class Mechanic extends AuditableAbstractAggregateRoot<Mechanic> {

    @Embedded
    @Column(name = "profile_id")
    private ProfileId profileId;

    /**
     * Workshop ID - The workshop to which this mechanic belongs
     * Nullable initially when mechanic is created automatically from ProfileCreatedEvent
     */
    @Column(name = "workshop_id")
    private Long workshopId;

    /**
     * Specializations assigned to the mechanic
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "mechanic_specializations", joinColumns = @JoinColumn(name = "mechanic_id"), inverseJoinColumns = @JoinColumn(name = "specialization_id"))
    private Set<Specialization> specializations = new HashSet<>();

    @NotNull
    @PositiveOrZero(message = "Years of experience must be zero or positive")
    private Integer yearsOfExperience;

    /**
     * Default constructor for JPA
     */
    protected Mechanic() {
        super();
    }

    /**
     * Constructor with ProfileId only (for automatic creation from events)
     * 
     * @param profileId the profile id from Profiles BC
     */
    public Mechanic(Long profileId) {
        this();
        this.profileId = new ProfileId(profileId);
        this.workshopId = null; // Will be assigned later
        this.yearsOfExperience = 0;
    }

    /**
     * Constructor with ProfileId and WorkshopId
     * 
     * @param profileId the profile id from Profiles BC
     * @param workshopId the workshop id to which this mechanic belongs
     */
    public Mechanic(Long profileId, Long workshopId) {
        this();
        this.profileId = new ProfileId(profileId);
        this.workshopId = workshopId;
        this.yearsOfExperience = 0;
    }

    /**
     * Constructor with ProfileId value object and WorkshopId
     * 
     * @param profileId the profile id value object
     * @param workshopId the workshop id
     * @see ProfileId
     */
    public Mechanic(ProfileId profileId, Long workshopId) {
        this();
        this.profileId = profileId;
        this.workshopId = workshopId;
        this.yearsOfExperience = 0;
    }

    /**
     * Constructor with all fields
     * 
     * @param profileId         the profile id
     * @param workshopId        the workshop id (can be null for automatic creation)
     * @param specializations   the specializations of the mechanic
     * @param yearsOfExperience the years of experience
     */
    public Mechanic(Long profileId,
                    Long workshopId,
                    Set<Specialization> specializations,
                    Integer yearsOfExperience) {
        this();
        this.profileId = new ProfileId(profileId);
        this.workshopId = workshopId;
        this.yearsOfExperience = yearsOfExperience == null ? 0 : Math.max(0, yearsOfExperience);
        // DO NOT create transient Specialization instances here
        // The CommandService is responsible for fetching persisted entities from DB
        if (specializations != null && !specializations.isEmpty()) {
            this.specializations = new HashSet<>(specializations);
        }
        // If specializations is null/empty, leave the Set empty
        // The CommandService will add the default specialization from DB
    }

    /**
     * Get profile id
     * 
     * @return the profile id from Profiles BC
     */
    public Long getProfileId() {
        return this.profileId.profileId();
    }

    /**
     * Get workshop id
     * 
     * @return the workshop id to which this mechanic belongs, or null if not assigned yet
     */
    public Long getWorkshopIdValue() {
        return this.workshopId;
    }

    /**
     * Assign mechanic to a workshop
     * 
     * @param workshopId the workshop id
     */
    public void assignToWorkshop(Long workshopId) {
        if (workshopId == null || workshopId <= 0) {
            throw new IllegalArgumentException("Workshop ID must be a positive value");
        }
        this.workshopId = workshopId;
    }

    /**
     * Check if mechanic is assigned to a workshop
     * 
     * @return true if mechanic has a workshop assigned, false otherwise
     */
    public boolean hasWorkshopAssigned() {
        return this.workshopId != null;
    }

    /**
     * Get the names of specializations assigned to the mechanic.
     * 
     * @return Set of specialization names
     */
    public Set<String> getSpecializationNames() {
        return this.specializations.stream()
                .map(Specialization::getStringName)
                .collect(Collectors.toSet());
    }

    /**
     * Check if the mechanic has a specific specialization.
     * 
     * @param specializationName Name of the specialization to check
     * @return true if the mechanic has the specialization, false otherwise
     */
    public boolean hasSpecialization(String specializationName) {
        return this.specializations.stream()
                .anyMatch(specialization -> specialization.getStringName().equals(specializationName));
    }

    /**
     * Add a set of specializations to the mechanic.
     * IMPORTANT: The specializations MUST be persisted entities from the database.
     * 
     * @param specializations the specializations (persisted entities).
     * @return the mechanic.
     */
    public Mechanic addSpecializations(Set<Specialization> specializations) {
        if (specializations != null && !specializations.isEmpty()) {
            this.specializations.addAll(specializations);
        }
        return this;
    }

    /**
     * Update mechanic specializations
     * 
     * @param specializations new specializations set
     */
    public void updateSpecializations(Set<Specialization> specializations) {
        this.specializations.clear();
        if (specializations != null) {
            this.specializations.addAll(specializations);
        }
    }

    /**
     * Update years of experience
     * 
     * @param yearsOfExperience new years of experience
     */
    public void updateYearsOfExperience(Integer yearsOfExperience) {
        if (yearsOfExperience != null && yearsOfExperience >= 0) {
            this.yearsOfExperience = yearsOfExperience;
        }
    }

    /**
     * Factory method to create a Mechanic from CreateMechanicCommand
     * 
     * @param command the create mechanic command
     * @return a new Mechanic instance
     */
    public static Mechanic create(CreateMechanicCommand command) {
        return new Mechanic(
            command.profileId(),
            command.workshopId(),
            command.specializations(),
            command.yearsOfExperience());
    }
}