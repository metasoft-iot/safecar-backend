package com.safecar.platform.workshop.application.internal.commandservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.commands.CreateWorkshopCommand;
import com.safecar.platform.workshop.domain.model.commands.UpdateWorkshopCommand;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopRepository;

@ExtendWith(MockitoExtension.class)
public class WorkshopCommandServiceImplTest {

    @Mock
    private WorkshopRepository workshopRepository;

    @Mock
    private ProfilesContextFacade profilesContextFacade;

    @InjectMocks
    private WorkshopCommandServiceImpl workshopCommandService;

    @Test
    public void handleCreateWorkshopCommand_WhenValidData_CreatesWorkshop() {
        // Arrange
        CreateWorkshopCommand command = new CreateWorkshopCommand(1L, "Description");
        Workshop workshop = new Workshop(command);
        workshop.setId(1L);

        when(profilesContextFacade.existsBusinessProfileById(1L)).thenReturn(true);
        when(workshopRepository.existsByBusinessProfileId(any(ProfileId.class))).thenReturn(false);
        when(workshopRepository.save(any(Workshop.class))).thenReturn(workshop);

        // Act
        Optional<Workshop> result = workshopCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getWorkshopDescription()).isEqualTo("Description");
    }

    @Test
    public void handleCreateWorkshopCommand_WhenBusinessProfileNotFound_ThrowsException() {
        // Arrange
        CreateWorkshopCommand command = new CreateWorkshopCommand(1L, "Description");

        when(profilesContextFacade.existsBusinessProfileById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> workshopCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Business profile with ID 1 does not exist");
    }

    @Test
    public void handleCreateWorkshopCommand_WhenWorkshopExists_ThrowsException() {
        // Arrange
        CreateWorkshopCommand command = new CreateWorkshopCommand(1L, "Description");

        when(profilesContextFacade.existsBusinessProfileById(1L)).thenReturn(true);
        when(workshopRepository.existsByBusinessProfileId(any(ProfileId.class))).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> workshopCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Workshop already exists for business profile ID 1");
    }

    @Test
    public void handleUpdateWorkshopCommand_WhenWorkshopExists_UpdatesWorkshop() {
        // Arrange
        UpdateWorkshopCommand command = new UpdateWorkshopCommand(1L, "Updated Description");
        Workshop workshop = new Workshop(1L, "Description");
        workshop.setId(1L);

        when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
        when(workshopRepository.save(any(Workshop.class))).thenReturn(workshop);

        // Act
        Optional<Workshop> result = workshopCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getWorkshopDescription()).isEqualTo("Updated Description");
    }
}
