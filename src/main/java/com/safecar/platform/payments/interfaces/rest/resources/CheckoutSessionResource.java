package com.safecar.platform.payments.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Checkout Session Resource - Represents the response containing the Stripe
 * checkout session ID that the frontend will use to redirect the user to Stripe.
 * 
 * @param sessionId The Stripe checkout session ID.
 */
@Schema(description = "Response containing Stripe checkout session ID")
public record CheckoutSessionResource(
        @Schema(description = "Stripe checkout session ID", 
                example = "cs_test_a1B2c3D4e5F6g7H8i9J0k1L2m3N4o5P6")
        String sessionId
) {
}
