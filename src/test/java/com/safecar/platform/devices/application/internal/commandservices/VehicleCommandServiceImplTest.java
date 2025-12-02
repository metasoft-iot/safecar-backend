package com.safecar.platform.devices.application.internal.commandservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.safecar.platform.devices.application.internal.outboundservices.acl.ExternalProfileService;
import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.devices.domain.model.commands.DeleteVehicleCommand;
import com.safecar.platform.devices.domain.model.commands.UpdateVehicleCommand;
import com.safecar.platform.devices.domain.model.events.VehicleCreatedEvent;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class VehicleCommandServiceImplTest {

        @Mock
        private VehicleRepository vehicleRepository;

        @Mock
        private ExternalProfileService externalProfileService;

        @Mock
        private ApplicationEventPublisher eventPublisher;

        @InjectMocks
        private VehicleCommandServiceImpl vehicleCommandService;

        @Test
        public void handleCreateVehicleCommand_WhenValidData_CreatesVehicleAndPublishesEvent() {
                // Arrange
                CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model", null, null,
                                null,
                                null);
                Vehicle vehicle = new Vehicle(command);
                vehicle.setId(1L);

                when(externalProfileService.validatePersonProfileExists(1L)).thenReturn(true);
                when(vehicleRepository.existsByLicensePlate("ABC-123")).thenReturn(false);
                when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

                // Act
                Optional<Vehicle> result = vehicleCommandService.handle(command);

                // Assert
                assertThat(result).isPresent();
                assertThat(result.get().getLicensePlate()).isEqualTo("ABC-123");
                verify(eventPublisher).publishEvent(any(VehicleCreatedEvent.class));
        }

        @Test
        public void handleCreateVehicleCommand_WhenDriverNotFound_ThrowsException() {
                // Arrange
                CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model", null, null,
                                null,
                                null);

                when(externalProfileService.validatePersonProfileExists(1L)).thenReturn(false);

                // Act & Assert
                assertThatThrownBy(() -> vehicleCommandService.handle(command))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Driver profile not found for ID: 1");
        }

        @Test
        public void handleCreateVehicleCommand_WhenVehicleExists_ThrowsException() {
                // Arrange
                CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model", null, null,
                                null,
                                null);

                when(externalProfileService.validatePersonProfileExists(1L)).thenReturn(true);
                when(vehicleRepository.existsByLicensePlate("ABC-123")).thenReturn(true);

                // Act & Assert
                assertThatThrownBy(() -> vehicleCommandService.handle(command))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Vehicle with license plate 'ABC-123' already exists");
        }

        @Test
        public void handleUpdateVehicleCommand_WhenVehicleExists_UpdatesVehicle() {
                // Arrange
                UpdateVehicleCommand command = new UpdateVehicleCommand(1L, 1L, "ABC-123-UPDATED", "Brand", "Model",
                                null, null,
                                null, null);
                CreateVehicleCommand createCommand = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model", null,
                                null, null,
                                null);
                Vehicle vehicle = new Vehicle(createCommand);
                vehicle.setId(1L);

                when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
                when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

                // Act
                Optional<Vehicle> result = vehicleCommandService.handle(command);

                // Assert
                assertThat(result).isPresent();
                assertThat(result.get().getLicensePlate()).isEqualTo("ABC-123-UPDATED");
        }

        @Test
        public void handleDeleteVehicleCommand_WhenVehicleExists_DeletesVehicle() {
                // Arrange
                DeleteVehicleCommand command = new DeleteVehicleCommand(1L, 1L);

                when(vehicleRepository.existsById(1L)).thenReturn(true);

                // Act
                vehicleCommandService.handle(command);

                // Assert
                verify(vehicleRepository).deleteById(1L);
        }

        @Test
        public void handleDeleteVehicleCommand_WhenVehicleNotFound_ThrowsException() {
                // Arrange
                DeleteVehicleCommand command = new DeleteVehicleCommand(1L, 1L);

                when(vehicleRepository.existsById(1L)).thenReturn(false);

                // Act & Assert
                assertThatThrownBy(() -> vehicleCommandService.handle(command))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Vehicle not found with ID: 1");
        }
}
