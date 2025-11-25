package com.safecar.platform.iam.application.internal.queryservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class RoleQueryServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleQueryServiceImpl roleQueryService;

    @Test
    public void handleGetAllRolesQuery_ReturnsListOfRoles() {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        List<Role> roles = List.of(role);

        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> result = roleQueryService.handle(new GetAllRolesQuery());

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(Roles.ROLE_CLIENT);
    }
}
