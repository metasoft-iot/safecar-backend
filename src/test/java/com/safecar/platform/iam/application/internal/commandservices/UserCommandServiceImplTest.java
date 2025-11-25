package com.safecar.platform.iam.application.internal.commandservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.safecar.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.commands.SignInCommand;
import com.safecar.platform.iam.domain.model.commands.SignUpCommand;
import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.valueobjects.Email;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashingService hashingService;

    @Mock
    private TokenService tokenService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @Test
    public void handleSignUpCommand_WhenUserDoesNotExist_CreatesUser() {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        SignUpCommand command = new SignUpCommand("test@example.com", "Password@123", "Password@123", Set.of(role));
        User user = new User("test@example.com", "$HashedPassword123!", role);

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_CLIENT)).thenReturn(Optional.of(role));
        when(hashingService.encode("Password@123")).thenReturn("$HashedPassword123!");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        Optional<User> result = userCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void handleSignUpCommand_WhenUserExists_ThrowsException() {
        // Arrange
        Role role = new Role(Roles.ROLE_CLIENT);
        SignUpCommand command = new SignUpCommand("test@example.com", "Password@123", "Password@123", Set.of(role));

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userCommandService.handle(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already exists");
    }

    @Test
    public void handleSignInCommand_WhenCredentialsAreValid_ReturnsUserAndToken() {
        // Arrange
        SignInCommand command = new SignInCommand("test@example.com", "Password@123");
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "$HashedPassword123!", role);

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(hashingService.matches("Password@123", "$HashedPassword123!")).thenReturn(true);
        when(tokenService.generateToken("test@example.com")).thenReturn("token");

        // Act
        Optional<ImmutablePair<User, String>> result = userCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getLeft().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getRight()).isEqualTo("token");
    }

    @Test
    public void handleSignInCommand_WhenUserNotFound_ThrowsException() {
        // Arrange
        SignInCommand command = new SignInCommand("test@example.com", "Password@123");

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userCommandService.handle(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    public void handleSignInCommand_WhenPasswordInvalid_ThrowsException() {
        // Arrange
        SignInCommand command = new SignInCommand("test@example.com", "Password@123");
        Role role = new Role(Roles.ROLE_CLIENT);
        User user = new User("test@example.com", "$HashedPassword123!", role);

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(hashingService.matches("Password@123", "$HashedPassword123!")).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userCommandService.handle(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid password");
    }
}
