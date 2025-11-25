package com.safecar.platform.payments.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safecar.platform.payments.domain.model.commands.CreateSubscriptionCommand;
import com.safecar.platform.payments.domain.services.PaymentCommandService;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;

import com.stripe.model.Subscription;
import com.stripe.net.Webhook;

@WebMvcTest(StripeWebhooksController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StripeWebhooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentCommandService paymentCommandService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    public void handleWebhook_WhenValidEvent_ProcessesSubscription() throws Exception {
        // Arrange
        String payload = "{}";
        String sigHeader = "valid_signature";

        try (MockedStatic<Webhook> mockedWebhook = mockStatic(Webhook.class)) {
            Event event = mock(Event.class);
            EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
            Subscription subscription = mock(Subscription.class);

            when(event.getType()).thenReturn("customer.subscription.created");
            when(event.getDataObjectDeserializer()).thenReturn(deserializer);
            when(deserializer.getObject()).thenReturn(Optional.of(subscription));
            when(subscription.getId()).thenReturn("sub_123");
            when(subscription.getMetadata()).thenReturn(Map.of("user_id", "1", "plan_type", "BASIC"));

            mockedWebhook.when(() -> Webhook.constructEvent(anyString(), anyString(), anyString()))
                    .thenReturn(event);

            // Act & Assert
            mockMvc.perform(post("/webhooks/stripe")
                    .content(payload)
                    .header("Stripe-Signature", sigHeader))
                    .andExpect(status().isOk());

            verify(paymentCommandService).handle(any(CreateSubscriptionCommand.class));
        }
    }
}
