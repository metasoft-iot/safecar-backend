package com.safecar.platform.payments.interfaces.rest.transform;

import com.safecar.platform.payments.domain.model.commands.CreateCheckoutSessionCommand;
import com.safecar.platform.payments.domain.model.valueobjects.PlanType;
import com.safecar.platform.payments.interfaces.rest.resources.CreateCheckoutSessionResource;

/**
 * Create Checkout Session Command From Resource Assembler - Converts
 * CreateCheckoutSessionResource to CreateCheckoutSessionCommand.
 */
public class CreateCheckoutSessionCommandFromResourceAssembler {

    /**
     * Converts a {@link CreateCheckoutSessionResource} to a
     * {@link CreateCheckoutSessionCommand}.
     *
     * @param userId the user ID from request header
     * @param resource the create checkout session resource
     * @return the create checkout session command
     * @throws IllegalArgumentException if plan type is invalid
     */
    public static CreateCheckoutSessionCommand toCommandFromResource(String userId, CreateCheckoutSessionResource resource) {
        try {
            PlanType planType = PlanType.valueOf(resource.planType().toUpperCase());
            return new CreateCheckoutSessionCommand(userId, planType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid plan type: " + resource.planType() + 
                ". Must be BASIC, PROFESSIONAL, or PREMIUM", e);
        }
    }
}
