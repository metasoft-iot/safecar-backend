package com.safecar.platform.payments.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safecar.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.safecar.platform.payments.domain.model.commands.CreateCheckoutSessionCommand;
import com.safecar.platform.payments.domain.services.PaymentCommandService;
import com.safecar.platform.payments.interfaces.rest.resources.CreateCheckoutSessionResource;

@WebMvcTest(PaymentsController.class)
@AutoConfigureMockMvc
public class PaymentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentCommandService paymentCommandService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createCheckoutSession_WhenValidData_ReturnsSessionId() throws Exception {
        // Arrange
        CreateCheckoutSessionResource resource = new CreateCheckoutSessionResource("BASIC");
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "password", Collections.emptyList());

        when(paymentCommandService.handle(any(CreateCheckoutSessionCommand.class))).thenReturn("session_id");

        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/checkout-session")
                .with(user(userDetails))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("session_id"));
    }
}
