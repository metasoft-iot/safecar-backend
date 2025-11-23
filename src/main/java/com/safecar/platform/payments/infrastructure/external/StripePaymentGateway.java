package com.safecar.platform.payments.infrastructure.external;

import com.safecar.platform.payments.domain.model.valueobjects.PlanType;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class StripePaymentGateway {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createCheckoutSession(String userId, PlanType plan) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .putMetadata("user_id", userId)
                .putMetadata("plan_type", plan.name())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(plan.getStripePriceId())
                                .setQuantity(1L)
                                .build()
                )
                .setSuccessUrl(frontendUrl + "/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/payment/cancel")
                .build();

        Session session = Session.create(params);
        return session.getId();
    }
}