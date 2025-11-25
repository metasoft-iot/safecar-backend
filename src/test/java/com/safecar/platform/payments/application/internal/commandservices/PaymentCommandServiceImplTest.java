package com.safecar.platform.payments.application.internal.commandservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.payments.domain.model.aggregates.Subscription;
import com.safecar.platform.payments.domain.model.commands.CreateCheckoutSessionCommand;
import com.safecar.platform.payments.domain.model.commands.CreateSubscriptionCommand;
import com.safecar.platform.payments.domain.model.valueobjects.PlanType;
import com.safecar.platform.payments.infrastructure.external.StripePaymentGateway;
import com.safecar.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import com.stripe.exception.StripeException;

@ExtendWith(MockitoExtension.class)
public class PaymentCommandServiceImplTest {

    @Mock
    private StripePaymentGateway stripeGateway;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private PaymentCommandServiceImpl paymentCommandService;

    @Test
    public void handleCreateCheckoutSessionCommand_WhenValidData_ReturnsSessionId() throws StripeException {
        // Arrange
        CreateCheckoutSessionCommand command = new CreateCheckoutSessionCommand("user_123", PlanType.BASIC);

        when(stripeGateway.createCheckoutSession("user_123", PlanType.BASIC)).thenReturn("session_id");

        // Act
        String result = paymentCommandService.handle(command);

        // Assert
        assertThat(result).isEqualTo("session_id");
    }

    @Test
    public void handleCreateCheckoutSessionCommand_WhenStripeError_ThrowsException() throws StripeException {
        // Arrange
        CreateCheckoutSessionCommand command = new CreateCheckoutSessionCommand("user_123", PlanType.BASIC);

        when(stripeGateway.createCheckoutSession("user_123", PlanType.BASIC))
                .thenThrow(new RuntimeException("Stripe error"));

        // Act & Assert
        assertThatThrownBy(() -> paymentCommandService.handle(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Payment error");
    }

    @Test
    public void handleCreateSubscriptionCommand_WhenValidData_CreatesSubscription() {
        // Arrange
        CreateSubscriptionCommand command = new CreateSubscriptionCommand("user_123", "sub_123", PlanType.BASIC);
        Subscription subscription = new Subscription(command);
        subscription.setId(1L);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Act
        Optional<Subscription> result = paymentCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getStripeSubscriptionId()).isEqualTo("sub_123");
    }

    @Test
    public void handleCreateSubscriptionCommand_WhenError_ReturnsEmpty() {
        // Arrange
        CreateSubscriptionCommand command = new CreateSubscriptionCommand("user_123", "sub_123", PlanType.BASIC);

        when(subscriptionRepository.save(any(Subscription.class))).thenThrow(new RuntimeException("DB error"));

        // Act
        Optional<Subscription> result = paymentCommandService.handle(command);

        // Assert
        assertThat(result).isEmpty();
    }
}
