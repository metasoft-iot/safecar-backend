package com.safecar.platform.workshop.application.internal.outboundservices.acl;

import com.safecar.platform.iam.interfaces.acl.IamContextFacade;
import com.safecar.platform.workshop.domain.model.valueobjects.DriverId;
import com.safecar.platform.workshop.domain.model.valueobjects.UserId;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * External IAM Service
 * <p>
 *     This service provides access to IAM bounded context functionality from WorkshopOps.
 *     It acts as an adapter/wrapper around the IamContextFacade to provide domain-specific
 *     operations for the WorkshopOps bounded context.
 * </p>
 */
@Service("workshopExternalIamService")
public class ExternalIamService {
    
    
    private final IamContextFacade iamContextFacade;

    /**
     * Constructor
     *
     * @param iamContextFacade IAM Context Facade
     */
    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Fetch Driver By Email
     * <p>
     *     Retrieves driver information by email address and maps it to WorkshopOps domain objects.
     * </p>
     * @param email The driver's email address
     * @return An {@link Optional} of {@link DriverId} if found
     */
    public Optional<DriverId> fetchDriverByEmail(String email) {
        var userId = iamContextFacade.fetchUserIdByEmail(email);
        if (userId == null || userId <= 0L) {
            return Optional.empty();
        }
        
        var userEmail = iamContextFacade.fetchUserEmailByUserId(userId);
        if (userEmail == null || userEmail.isBlank()) {
            return Optional.empty();
        }
        
        return Optional.of(new DriverId(userId));
    }

    /**
     * Validate Driver Exists
     * <p>
     *     Validates if a driver exists in the IAM system by their ID.
     * </p>
     * @param driverId The driver's ID to validate
     * @return true if the driver exists, false otherwise
     */
    public boolean validateDriverExists(Long driverId) {
        try {
            return iamContextFacade.validateUserExists(driverId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate Driver Exists By Email
     * <p>
     *     Validates if a driver exists in the IAM system by their email address.
     * </p>
     * @param email The driver's email address to validate
     * @return true if the driver exists, false otherwise
     */
    public boolean validateDriverExistsByEmail(String email) {
        try {
            return iamContextFacade.validateUserExistsByEmail(email);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fetch Driver Email By ID
     * <p>
     *     Retrieves the email address for a given driver ID.
     * </p>
     * @param driverId The driver's ID
     * @return The driver's email address, or null if not found
     */
    public String fetchDriverEmailById(Long driverId) {
        try {
            return iamContextFacade.fetchUserEmailByUserId(driverId);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Validates if a user exists and can be used as a mechanic.
     * Used for workshop appointment assignments.
     * 
     * @param mechanicId The mechanic's user ID to validate
     * @return true if the user exists, false otherwise
     */
    public boolean validateMechanicExists(Long mechanicId) {
        try {
            return iamContextFacade.validateUserExists(mechanicId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fetch Mechanic Email By ID
     * <p>
     *     Retrieves the email address for a given mechanic ID.
     * </p>
     * @param mechanicId The mechanic's ID
     * @return The mechanic's email address, empty string if not found
     */
    public String fetchMechanicEmailById(Long mechanicId) {
        try {
            return iamContextFacade.fetchUserEmailByUserId(mechanicId);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Validates user authentication for workshop operations.
     * 
     * @param userId The user ID to validate
     * @return true if user is authenticated and can perform workshop operations
     */
    public boolean validateUserAuthenticated(Long userId) {
        try {
            return iamContextFacade.validateUserExists(userId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates a UserId value object for workshop operations.
     * 
     * @param userId the user ID as Long
     * @return UserId value object
     */
    public UserId createUserId(Long userId) {
        return new UserId(userId);
    }

    /**
     * Fetches user information for workshop display purposes.
     * 
     * @param userId The user ID
     * @return formatted user information
     */
    public String fetchUserDisplayInfo(Long userId) {
        try {
            String email = iamContextFacade.fetchUserEmailByUserId(userId);
            if (email != null && !email.isEmpty()) {
                return "User: " + email;
            }
            return "User ID: " + userId;
        } catch (Exception e) {
            return "Unknown User";
        }
    }
}