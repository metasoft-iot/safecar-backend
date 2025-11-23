package com.safecar.platform.payments.domain.model.valueobjects;

/**
 * Plan Type - Value object representing subscription plan types with their
 * corresponding Stripe price IDs.
 */
public enum PlanType {
    BASIC("price_1SQbsT3l890Fc29CerlSwh4r", 3),
    PROFESSIONAL("price_1SQbt23l890Fc29CqoqLYCnu", 10),
    PREMIUM("price_1SQbtK3l890Fc29COSEZ6iK4", 30);

    private final String stripePriceId;
    private final int mechanicsLimit;

    PlanType(String stripePriceId, int mechanicsLimit) {
        this.stripePriceId = stripePriceId;
        this.mechanicsLimit = mechanicsLimit;
    }

    public String getStripePriceId() {
        return stripePriceId;
    }

    public int getMechanicsLimit() {
        return mechanicsLimit;
    }
}
