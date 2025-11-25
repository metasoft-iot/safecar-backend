package com.safecar.platform.iam.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.domain.services.RoleQueryService;

@WebMvcTest(RolesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RolesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleQueryService roleQueryService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    public void getAllRoles_ReturnsListOfRoles() throws Exception {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        role.setId(1L);
        List<Role> roles = List.of(role);

        when(roleQueryService.handle(any(GetAllRolesQuery.class))).thenReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("ROLE_CLIENT"));
    }
}
