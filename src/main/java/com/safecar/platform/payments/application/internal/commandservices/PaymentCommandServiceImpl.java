package com.safecar.platform.payments.application.internal.commandservices;

import com.safecar.platform.payments.domain.model.aggregates.Subscription;
import com.safecar.platform.payments.domain.model.commands.CreateCheckoutSessionCommand;
import com.safecar.platform.payments.domain.model.commands.CreateSubscriptionCommand;
import com.safecar.platform.payments.domain.services.PaymentCommandService;
import com.safecar.platform.payments.infrastructure.external.StripePaymentGateway;
import com.safecar.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Payment Command Service Implementation - Handles payment-related command operations.
 */
@Slf4j
@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final StripePaymentGateway stripeGateway;
    private final SubscriptionRepository subscriptionRepository;

    public PaymentCommandServiceImpl(
            StripePaymentGateway stripeGateway,
            SubscriptionRepository subscriptionRepository) {
        this.stripeGateway = stripeGateway;
        this.subscriptionRepository = subscriptionRepository;
        log.info("=== PAYMENT COMMAND SERVICE INITIALIZED ===");
    }

    @Override
    public String handle(CreateCheckoutSessionCommand command) {
        log.info("Creating checkout session for userId: {} and plan: {}", 
                command.userId(), command.planType());

        try {
            String sessionId = stripeGateway.createCheckoutSession(
                    command.userId(), 
                    command.planType());
            
            log.info("Checkout session created successfully: {}", sessionId);
            return sessionId;

        } catch (StripeException e) {
            log.error("Stripe API error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create checkout session: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error creating checkout session: {}", e.getMessage(), e);
            throw new RuntimeException("Payment error: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {
        log.info("Creating subscription for userId: {} with Stripe ID: {}", 
                command.userId(), command.stripeSubscriptionId());

        try {
            var subscription = new Subscription(command);
            var savedSubscription = subscriptionRepository.save(subscription);
            
            log.info("Subscription created successfully with ID: {}", savedSubscription.getId());
            return Optional.of(savedSubscription);

        } catch (Exception e) {
            log.error("Error creating subscription: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
}
