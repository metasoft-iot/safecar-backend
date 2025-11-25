package com.safecar.platform.iam.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.commands.SignInCommand;
import com.safecar.platform.iam.domain.model.commands.SignUpCommand;
import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.domain.services.UserCommandService;
import com.safecar.platform.iam.interfaces.rest.resources.SignInResource;
import com.safecar.platform.iam.interfaces.rest.resources.SignUpResource;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for this test
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserCommandService userCommandService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signUp_WhenUserCreated_ReturnsCreated() throws Exception {
        // Arrange
        SignUpResource resource = new SignUpResource("test@example.com", "Password@123", "Password@123",
                Set.of("ROLE_CLIENT"));
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "Password@123", role);
        user.setId(1L);

        when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void signUp_WhenUserNotCreated_ReturnsBadRequest() throws Exception {
        // Arrange
        SignUpResource resource = new SignUpResource("test@example.com", "Password@123", "Password@123",
                Set.of("ROLE_CLIENT"));

        when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signIn_WhenUserAuthenticated_ReturnsOk() throws Exception {
        // Arrange
        SignInResource resource = new SignInResource("test@example.com", "Password@123");
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "Password@123", role);
        user.setId(1L);
        String token = "test-token";

        when(userCommandService.handle(any(SignInCommand.class)))
                .thenReturn(Optional.of(ImmutablePair.of(user, token)));

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test@example.com"))
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void signIn_WhenUserNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        SignInResource resource = new SignInResource("test@example.com", "Password@123");

        when(userCommandService.handle(any(SignInCommand.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isNotFound());
    }
}
