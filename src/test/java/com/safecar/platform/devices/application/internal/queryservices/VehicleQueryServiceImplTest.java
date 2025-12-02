package com.safecar.platform.devices.application.internal.queryservices;

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

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByDriverIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehiclesByWorkshopIdQuery;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.VehicleRepository;
import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.AppointmentRepository;

@ExtendWith(MockitoExtension.class)
public class VehicleQueryServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private VehicleQueryServiceImpl vehicleQueryService;

    @Test
    public void handleGetVehicleByIdQuery_WhenVehicleExists_ReturnsVehicle() {
        // Arrange
        CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model", null, null, null,
                null);
        Vehicle vehicle = new Vehicle(command);
        vehicle.setId(1L);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        // Act
        Optional<Vehicle> result = vehicleQueryService.handle(new GetVehicleByIdQuery(1L));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getLicensePlate()).isEqualTo("ABC-123");
    }

    @Test
    public void handleGetVehicleByDriverIdQuery_WhenVehiclesExist_ReturnsVehicles() {
        // Arrange
        CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model", null, null, null,
                null);
        Vehicle vehicle = new Vehicle(command);
        vehicle.setId(1L);

        when(vehicleRepository.findByDriverId_DriverId(1L)).thenReturn(List.of(vehicle));

        // Act
        List<Vehicle> result = vehicleQueryService.handle(new GetVehicleByDriverIdQuery(1L));

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getDriverId()).isEqualTo(1L);
    }

    @Test
    public void handleGetVehiclesByWorkshopIdQuery_WhenAppointmentsExist_ReturnsVehicles() {
        // Arrange
        CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model", null, null, null,
                null);
        Vehicle vehicle = new Vehicle(command);
        vehicle.setId(1L);

        Appointment appointment = org.mockito.Mockito.mock(Appointment.class);
        when(appointment.getVehicleId())
                .thenReturn(new com.safecar.platform.workshop.domain.model.valueobjects.VehicleId(1L));

        when(appointmentRepository.findByWorkshopId(any(WorkshopId.class))).thenReturn(List.of(appointment));
        when(vehicleRepository.findAllById(List.of(1L))).thenReturn(List.of(vehicle));

        // Act
        List<Vehicle> result = vehicleQueryService.handle(new GetVehiclesByWorkshopIdQuery(1L));

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getLicensePlate()).isEqualTo("ABC-123");
    }
}
