package com.safecar.platform.payments.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Create Checkout Session Resource - Represents the request payload for creating
 * a Stripe checkout session for subscription payment.
 * 
 * @param planType The subscription plan type (BASIC, PROFESSIONAL, PREMIUM).
 */
@Schema(description = "Request object for creating a Stripe checkout session")
public record CreateCheckoutSessionResource(
        @NotBlank(message = "Plan type is required")
        @Pattern(regexp = "BASIC|PROFESSIONAL|PREMIUM", 
                 message = "Plan type must be BASIC, PROFESSIONAL, or PREMIUM")
        @Schema(description = "Subscription plan type", 
                example = "BASIC", 
                allowableValues = {"BASIC", "PROFESSIONAL", "PREMIUM"})
        String planType
) {
}
