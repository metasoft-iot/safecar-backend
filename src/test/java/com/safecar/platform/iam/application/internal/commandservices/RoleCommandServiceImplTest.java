package com.safecar.platform.iam.application.internal.commandservices;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.iam.domain.model.commands.SeedRolesCommand;
import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class RoleCommandServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleCommandServiceImpl roleCommandService;

    @Test
    public void handleSeedRolesCommand_WhenRolesDoNotExist_SavesRoles() {
        // Arrange
        when(roleRepository.existsByName(any(Roles.class))).thenReturn(false);

        // Act
        roleCommandService.handle(new SeedRolesCommand());

        // Assert
        verify(roleRepository, times(Roles.values().length)).save(any(Role.class));
    }

    @Test
    public void handleSeedRolesCommand_WhenRolesExist_DoesNotSaveRoles() {
        // Arrange
        when(roleRepository.existsByName(any(Roles.class))).thenReturn(true);

        // Act
        roleCommandService.handle(new SeedRolesCommand());

        // Assert
        verify(roleRepository, never()).save(any(Role.class));
    }
}
