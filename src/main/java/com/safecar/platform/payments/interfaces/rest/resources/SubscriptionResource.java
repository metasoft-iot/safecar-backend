package com.safecar.platform.payments.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Subscription Resource - Represents a subscription entity in the API response.
 * 
 * @param id The subscription ID.
 * @param userId The user ID associated with the subscription.
 * @param planType The subscription plan type.
 * @param status The subscription status (ACTIVE, CANCELLED, etc.).
 * @param stripeSubscriptionId The Stripe subscription ID.
 */
@Schema(description = "Subscription information")
public record SubscriptionResource(
        @Schema(description = "Subscription ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,
        
        @Schema(description = "User ID", example = "3")
        String userId,
        
        @Schema(description = "Plan type", example = "PROFESSIONAL")
        String planType,
        
        @Schema(description = "Subscription status", example = "ACTIVE")
        String status,
        
        @Schema(description = "Stripe subscription ID", example = "sub_1234567890")
        String stripeSubscriptionId
) {
}
