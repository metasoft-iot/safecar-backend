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
import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.safecar.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdateBusinessProfileCommand;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessProfileRepository;
import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;

@ExtendWith(MockitoExtension.class)
public class BusinessProfileCommandServiceImplTest {

        @Mock
        private BusinessProfileRepository businessProfileRepository;

        @Mock
        private ApplicationEventPublisher applicationEventPublisher;

        @Mock
        private ExternalIamService externalIamService;

        @InjectMocks
        private BusinessProfileCommandServiceImpl businessProfileCommandService;

        @Test
        public void handleCreateBusinessProfileCommand_WhenValidData_CreatesProfileAndPublishesEvent() {
                // Arrange
                CreateBusinessProfileCommand command = new CreateBusinessProfileCommand("testuser", "Business Name",
                                "12345678901",
                                "Address", "123456789", "contact@example.com", "Description");
                BusinessProfile profile = new BusinessProfile("test@example.com", "testuser", "Business Name",
                                "12345678901", "Address",
                                "123456789", "contact@example.com", "Description");
                profile.setId(1L);

                when(externalIamService.fetchUserRolesByUserEmail("test@example.com"))
                                .thenReturn(java.util.Set.of("ROLE_WORKSHOP_OWNER"));
                when(businessProfileRepository.save(any(BusinessProfile.class))).thenReturn(profile);

                // Act
                Optional<BusinessProfile> result = businessProfileCommandService.handle(command, "test@example.com");

                // Assert
                assertThat(result).isPresent();
                assertThat(result.get().getBusinessName()).isEqualTo("Business Name");
                verify(applicationEventPublisher).publishEvent(any(ProfileCreatedEvent.class));
        }

        @Test
        public void handleUpdateBusinessProfileCommand_WhenProfileExists_UpdatesProfile() {
                // Arrange
                UpdateBusinessProfileCommand command = new UpdateBusinessProfileCommand("testuser",
                                "Business Name Updated", "12345678901",
                                "Address", "123456789", "contact@example.com", "Description");
                BusinessProfile profile = new BusinessProfile("test@example.com", "testuser", "Business Name",
                                "12345678901", "Address",
                                "123456789", "contact@example.com", "Description");
                profile.setId(1L);

                when(businessProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
                when(businessProfileRepository.save(any(BusinessProfile.class))).thenReturn(profile);

                // Act
                Optional<BusinessProfile> result = businessProfileCommandService.handle(command, 1L);

                // Assert
                assertThat(result).isPresent();
                assertThat(result.get().getBusinessName()).isEqualTo("Business Name Updated");
        }

        @Test
        public void handleUpdateBusinessProfileCommand_WhenProfileNotFound_ThrowsException() {
                // Arrange
                UpdateBusinessProfileCommand command = new UpdateBusinessProfileCommand("testuser",
                                "Business Name Updated", "12345678901",
                                "Address", "123456789", "contact@example.com", "Description");

                when(businessProfileRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> businessProfileCommandService.handle(command, 1L))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("BusinessProfile with ID 1 does not exist.");
        }
}
