package com.safecar.platform.profiles.domain.model.aggregates;

import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * PersonProfile Aggregate
 * <p>
 * Represents a person's profile within the SafeCar platform including personal
 * details.
 * </p>
 */
@Setter
@Getter
@Entity
public class PersonProfile extends AuditableAbstractAggregateRoot<PersonProfile> {

    /**
     * User email associated with this profile - Cannot be blank
     */
    @NotBlank
    private String userEmail;

    /**
     * Full name of the person - Cannot be blank
     */
    @NotBlank
    private String fullName;

    /**
     * City of residence - Cannot be blank
     */
    @NotBlank
    private String city;

    /**
     * Country of residence - Cannot be blank
     */
    @NotBlank
    private String country;

    /**
     * Phone value object representing the person's phone details
     */
    @Embedded
    private Phone phone;

    /**
     * Dni value object representing the person's national identification number
     */
    @Embedded
    private Dni dni;

    /**
     * Default constructor for JPA
     */
    protected PersonProfile() {
    }

    /**
     * Constructor to create PersonProfile from command and userEmail
     * 
     * @param command   the create person profile command
     * @param userEmail the associated user email
     */
    public PersonProfile(CreatePersonProfileCommand command, String userEmail) {
        this.userEmail = userEmail;
        this.fullName = command.fullName();
        this.city = command.city();
        this.country = command.country();
        this.phone = new Phone(command.phone());
        this.dni = new Dni(command.dni());
    }

    /**
     * Constructor with all fields
     * 
     * @param userEmail the user email
     * @param fullName  the full name of the person
     * @param city      the city of residence
     * @param country   the country of residence
     * @param phone     the phone value object
     * @param dni       the dni value object
     */
    public PersonProfile(String userEmail, String fullName, String city, String country, Phone phone, Dni dni) {
        this.userEmail = userEmail;
        this.fullName = fullName;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.dni = dni;
    }

    /**
     * Get phone number as string
     * 
     * @return phone number
     */
    public String getPhoneNumber() {
        return phone.phone();
    }

    /**
     * Get dni number as string
     * 
     * @return dni number
     */
    public String getDniNumber() {
        return dni.dni();
    }

    /**
     * Update person profile metrics
     * @param fullName the full name of the person
     * @param city the city of residence
     * @param country the country of residence
     * @param phone the phone value object
     * @param dni the dni value object
     */
    public void updatePersonProfileMetrics(
            String fullName,
            String city,
            String country,
            Phone phone,
            Dni dni) {
        this.fullName = fullName;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.dni = dni;
    }
}
