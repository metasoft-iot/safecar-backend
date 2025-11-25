package com.safecar.platform.iam.application.internal.queryservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.safecar.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.safecar.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.safecar.platform.iam.domain.model.valueobjects.Email;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryServiceImpl userQueryService;

    @Test
    public void handleGetAllUsersQuery_ReturnsListOfUsers() {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "Password@123", role);
        List<User> users = List.of(user);

        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userQueryService.handle(new GetAllUsersQuery());

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void handleGetUserByEmailQuery_WhenUserExists_ReturnsUser() {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "Password@123", role);

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userQueryService.handle(new GetUserByEmailQuery("test@example.com"));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void handleGetUserByIdQuery_WhenUserExists_ReturnsUser() {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "Password@123", role);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userQueryService.handle(new GetUserByIdQuery(1L));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }
}
