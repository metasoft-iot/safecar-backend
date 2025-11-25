package com.safecar.platform.profiles.application.internal.commandservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.safecar.platform.profiles.application.internal.outbounceservices.acl.ExternalIamService;
import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;

@ExtendWith(MockitoExtension.class)
public class PersonProfileCommandServiceImplTest {

    @Mock
    private PersonProfileRepository personProfileRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private ExternalIamService externalIamService;

    @InjectMocks
    private PersonProfileCommandServiceImpl personProfileCommandService;

    @Test
    public void handleCreatePersonProfileCommand_WhenValidData_CreatesProfileAndPublishesEvent() {
        // Arrange
        CreatePersonProfileCommand command = new CreatePersonProfileCommand("John Doe", "City", "Country", "123456789",
                "12345678");
        PersonProfile profile = new PersonProfile("test@example.com", "John Doe", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));
        profile.setId(1L);

        when(externalIamService.fetchUserRolesByUserEmail("test@example.com"))
                .thenReturn(java.util.Set.of("ROLE_CLIENT"));
        when(personProfileRepository.save(any(PersonProfile.class))).thenReturn(profile);

        // Act
        Optional<PersonProfile> result = personProfileCommandService.handle(command, "test@example.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("John Doe");
        verify(applicationEventPublisher).publishEvent(any(ProfileCreatedEvent.class));
    }

    @Test
    public void handleUpdatePersonProfileCommand_WhenProfileExists_UpdatesProfile() {
        // Arrange
        UpdatePersonProfileCommand command = new UpdatePersonProfileCommand(1L, "John Doe Updated", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));
        PersonProfile profile = new PersonProfile("test@example.com", "John Doe", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));
        profile.setId(1L);

        when(personProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(personProfileRepository.save(any(PersonProfile.class))).thenReturn(profile);

        // Act
        Optional<PersonProfile> result = personProfileCommandService.handle(command, 1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("John Doe Updated");
    }

    @Test
    public void handleUpdatePersonProfileCommand_WhenProfileNotFound_ThrowsException() {
        // Arrange
        UpdatePersonProfileCommand command = new UpdatePersonProfileCommand(1L, "John Doe Updated", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));

        when(personProfileRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> personProfileCommandService.handle(command, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PersonProfile with id 1 not found");
    }
}
