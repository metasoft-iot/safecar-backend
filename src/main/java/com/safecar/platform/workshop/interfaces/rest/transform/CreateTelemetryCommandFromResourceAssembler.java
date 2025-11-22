package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.IngestTelemetrySampleCommand;
import com.safecar.platform.workshop.domain.model.valueobjects.*;
import com.safecar.platform.workshop.interfaces.rest.resources.CreateTelemetryResource;

import java.math.BigDecimal;

public class CreateTelemetryCommandFromResourceAssembler {

        public static IngestTelemetrySampleCommand toCommandFromResource(CreateTelemetryResource resource) {

                var type = resource.type() != null ? TelemetryType.valueOf(resource.type()) : TelemetryType.SENSOR_DATA;
                var severity = resource.severity() != null ? AlertSeverity.valueOf(resource.severity())
                                : AlertSeverity.INFO;
                var timestamp = resource.timestamp() != null ? new TelemetryTimestamp(resource.timestamp())
                                : new TelemetryTimestamp(java.time.Instant.now());
                var vehicleId = new VehicleId(resource.vehicleId());
                var driverId = new DriverId(resource.driverId());

                SpeedKmh speed = resource.speed() != null ? new SpeedKmh(BigDecimal.valueOf(resource.speed())) : null;

                GeoPoint location = null;
                if (resource.latitude() != null && resource.longitude() != null) {
                        location = new GeoPoint(BigDecimal.valueOf(resource.latitude()),
                                        BigDecimal.valueOf(resource.longitude()));
                }

                OdometerKm odometer = resource.odometer() != null
                                ? new OdometerKm(BigDecimal.valueOf(resource.odometer()))
                                : null;

                FaultCode dtc = null;
                if (resource.dtcCode() != null) {
                        dtc = new FaultCode(resource.dtcCode(), resource.dtcStandard());
                }

                TirePressure tirePressure = null;

                CabinGasLevel cabinGas = null;
                if (resource.cabinGasType() != null && resource.cabinGasConcentration() != null) {
                        cabinGas = new CabinGasLevel(
                                        CabinGasType.valueOf(resource.cabinGasType()),
                                        BigDecimal.valueOf(resource.cabinGasConcentration()));
                }

                AccelerationVector acceleration = null;
                if (resource.accelerationLateral() != null) {
                        acceleration = new AccelerationVector(
                                        toBigDecimal(resource.accelerationLateral()),
                                        toBigDecimal(resource.accelerationLongitudinal()),
                                        toBigDecimal(resource.accelerationVertical()));
                }

                TemperatureCelsius engineTemp = resource.engineTemperature() != null
                                ? new TemperatureCelsius(BigDecimal.valueOf(resource.engineTemperature()))
                                : null;
                TemperatureCelsius cabinTemp = resource.cabinTemperature() != null
                                ? new TemperatureCelsius(BigDecimal.valueOf(resource.cabinTemperature()))
                                : null;
                HumidityPercent cabinHumidity = resource.cabinHumidity() != null
                                ? new HumidityPercent(BigDecimal.valueOf(resource.cabinHumidity()))
                                : null;
                ElectricalCurrentAmps electricalCurrent = resource.electricalCurrent() != null
                                ? new ElectricalCurrentAmps(BigDecimal.valueOf(resource.electricalCurrent()))
                                : null;

                var sample = new TelemetrySample(
                                type, severity, timestamp, vehicleId, driverId,
                                speed, location, odometer, dtc, tirePressure, cabinGas,
                                acceleration, engineTemp, cabinTemp, cabinHumidity, electricalCurrent);

                return new IngestTelemetrySampleCommand(sample);
        }

        private static BigDecimal toBigDecimal(Double value) {
                return value != null ? BigDecimal.valueOf(value) : null;
        }
}
