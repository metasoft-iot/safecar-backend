package com.safecar.platform.profiles.domain.model.aggregates;

import com.safecar.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * BusinessProfile Aggregate
 * <p>
 * Represents a business profile within the SafeCar platform including business
 * details for workshop owners.
 * </p>
 */
@Setter
@Getter
@Entity
public class BusinessProfile extends AuditableAbstractAggregateRoot<BusinessProfile> {

    /**
     * User email associated with this business profile - Cannot be blank
     */
    @NotBlank
    private String userEmail;

    /**
     * Business name - Cannot be blank
     */
    @NotBlank
    private String businessName;

    /**
     * Tax identification number - Cannot be blank
     */
    @NotBlank
    private String ruc;

    /**
     * Business address - Cannot be blank
     */
    @NotBlank
    private String businessAddress;

    /**
     * Contact phone number - Cannot be blank
     */
    @NotBlank
    private String contactPhone;

    /**
     * Contact email - Cannot be blank
     */
    @NotBlank
    private String contactEmail;

    /**
     * Default constructor for JPA
     */
    protected BusinessProfile() {
    }

    /**
     * Constructor to create BusinessProfile from command and userEmail
     * 
     * @param command   the create business profile command
     * @param userEmail the associated user email
     */
    public BusinessProfile(CreateBusinessProfileCommand command, String userEmail) {
        this.userEmail = userEmail;
        this.businessName = command.businessName();
        this.ruc = command.ruc();
        this.businessAddress = command.businessAddress();
        this.contactPhone = command.contactPhone();
        this.contactEmail = command.contactEmail();
    }

    /**
     * Constructor with all fields
     * 
     * @param userEmail       the user email
     * @param businessName    the business name
     * @param taxId           the tax identification number
     * @param businessAddress the business address
     * @param contactPhone    the contact phone number
     * @param contactEmail    the contact email
     */
    public BusinessProfile(String userEmail, String businessName, String ruc,
            String businessAddress, String contactPhone, String contactEmail) {
        this.userEmail = userEmail;
        this.businessName = businessName;
        this.ruc = ruc;
        this.businessAddress = businessAddress;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }

    /**
     * Update business profile metrics
     * 
     * @param businessName    the new business name
     * @param ruc             the new tax identification number
     * @param businessAddress the new business address
     * @param contactPhone    the new contact phone number
     * @param contactEmail    the new contact email
     */
    public void updateBusinessProfileMetrics(
            String businessName,
            String ruc,
            String businessAddress,
            String contactPhone,
            String contactEmail) {
        this.businessName = businessName;
        this.ruc = ruc;
        this.businessAddress = businessAddress;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }
}