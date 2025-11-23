package com.safecar.platform.payments.interfaces.rest.transform;

import com.safecar.platform.payments.interfaces.rest.resources.CheckoutSessionResource;

/**
 * Checkout Session Resource From Session ID Assembler - Converts a Stripe session ID
 * to a CheckoutSessionResource.
 */
public class CheckoutSessionResourceFromSessionIdAssembler {

    /**
     * Converts a Stripe session ID string to a {@link CheckoutSessionResource}.
     *
     * @param sessionId the Stripe session ID
     * @return the checkout session resource
     */
    public static CheckoutSessionResource toResourceFromSessionId(String sessionId) {
        return new CheckoutSessionResource(sessionId);
    }
}
