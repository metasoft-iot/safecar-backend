package com.safecar.platform.iam.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.safecar.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.domain.services.UserQueryService;

@WebMvcTest(UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserQueryService userQueryService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    public void getAllUsers_ReturnsListOfUsers() throws Exception {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "Password@123", role);
        user.setId(1L);
        List<User> users = List.of(user);

        when(userQueryService.handle(any(GetAllUsersQuery.class))).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    public void getUserById_WhenUserExists_ReturnsUser() throws Exception {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "Password@123", role);
        user.setId(1L);

        when(userQueryService.handle(any(GetUserByEmailQuery.class))).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void getUserById_WhenUserNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(userQueryService.handle(any(GetUserByEmailQuery.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
