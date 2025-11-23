package com.safecar.platform.workshop.interfaces.rest.transform;

import java.util.Arrays;
import com.safecar.platform.workshop.domain.model.valueobjects.AppointmentStatus;

public class AppointmentStatusEnumValueFromSetAssembler {

    public static AppointmentStatus toAppointmentStatusEnumValueFromString(String status) {
        var normalizedStatus = status.trim().toUpperCase();
        return Arrays.stream(AppointmentStatus.values())
                .filter(s -> s.name().equals(normalizedStatus))
                .findFirst()
                .get();
    }
}
