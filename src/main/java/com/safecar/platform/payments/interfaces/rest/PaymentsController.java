package com.safecar.platform.payments.interfaces.rest;

import com.safecar.platform.payments.domain.services.PaymentCommandService;
import com.safecar.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.safecar.platform.payments.interfaces.rest.resources.CheckoutSessionResource;
import com.safecar.platform.payments.interfaces.rest.resources.CreateCheckoutSessionResource;
import com.safecar.platform.payments.interfaces.rest.transform.CheckoutSessionResourceFromSessionIdAssembler;
import com.safecar.platform.payments.interfaces.rest.transform.CreateCheckoutSessionCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * Payments Controller
 * 
 * <p>
 * This class is a REST controller that handles payment-related operations.
 * It provides endpoints for creating Stripe checkout sessions and managing
 * subscriptions.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Stripe payment integration and subscription management endpoints")
public class PaymentsController {

        private final PaymentCommandService paymentCommandService;

        public PaymentsController(PaymentCommandService paymentCommandService) {
                this.paymentCommandService = paymentCommandService;
        }

        /**
         * Creates a Stripe checkout session for the specified plan type (BASIC,
         * PROFESSIONAL, PREMIUM).
         * Requires JWT authentication.
         * 
         * @param resource
         * @param user
         * @return
         */
        @PostMapping("/checkout-session")
        @Operation(summary = "Create Stripe checkout session", description = "Creates a Stripe checkout session for the specified plan type (BASIC, PROFESSIONAL, PREMIUM). Requires JWT authentication.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Checkout session created successfully", content = @Content(schema = @Schema(implementation = CheckoutSessionResource.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid plan type"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
                        @ApiResponse(responseCode = "500", description = "Stripe API error")
        })
        public ResponseEntity<CheckoutSessionResource> createCheckoutSession(
                        @Valid @RequestBody CreateCheckoutSessionResource resource,
                        @AuthenticationPrincipal UserDetailsImpl user) {

                var userId = user.getId().toString();

                var command = CreateCheckoutSessionCommandFromResourceAssembler
                                .toCommandFromResource(userId, resource);

                var sessionId = paymentCommandService.handle(command);

                var response = CheckoutSessionResourceFromSessionIdAssembler
                                .toResourceFromSessionId(sessionId);

                return ResponseEntity.ok(response);
        }
}
