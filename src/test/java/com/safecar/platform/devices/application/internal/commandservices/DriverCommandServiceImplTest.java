package com.safecar.platform.devices.application.internal.commandservices;

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

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.model.commands.UpdateNumberOfDriverVehiclesCommand;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.DriverRepository;

@ExtendWith(MockitoExtension.class)
public class DriverCommandServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverCommandServiceImpl driverCommandService;

    @Test
    public void handleCreateDriverCommand_WhenDriverDoesNotExist_CreatesDriver() {
        CreateDriverCommand command = new CreateDriverCommand(1L);
        Driver driver = new Driver(1L);
        driver.setId(1L);

        when(driverRepository.existsByProfileId_ProfileId(1L)).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Optional<Driver> result = driverCommandService.handle(command);

        assertThat(result).isPresent();
        assertThat(result.get().getProfileId()).isEqualTo(1L);
    }

    @Test
    public void handleCreateDriverCommand_WhenDriverExists_ThrowsException() {
        CreateDriverCommand command = new CreateDriverCommand(1L);

        when(driverRepository.existsByProfileId_ProfileId(1L)).thenReturn(true);

        assertThatThrownBy(() -> driverCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Driver already exists for profile ID: 1");
    }

    @Test
    public void handleUpdateNumberOfDriverVehiclesCommand_WhenDriverExists_UpdatesVehicles() {
        UpdateNumberOfDriverVehiclesCommand command = new UpdateNumberOfDriverVehiclesCommand(1L);
        Driver driver = new Driver(1L);
        driver.setId(1L);

        when(driverRepository.existsById(1L)).thenReturn(true);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Optional<Driver> result = driverCommandService.handle(command);

        assertThat(result).isPresent();
        assertThat(result.get().getTotalVehicles()).isEqualTo(1);
    }

    @Test
    public void handleUpdateNumberOfDriverVehiclesCommand_WhenDriverNotFound_ThrowsException() {
        UpdateNumberOfDriverVehiclesCommand command = new UpdateNumberOfDriverVehiclesCommand(1L);

        when(driverRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> driverCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Driver not found for ID: 1");
    }
}
