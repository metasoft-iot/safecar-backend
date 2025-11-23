package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Value Object representing a geographical point with latitude and longitude.
 */
@Embeddable
public record GeoPoint(
        @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
        BigDecimal latitude,
        
        @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
        BigDecimal longitude
) {
    public GeoPoint {
        if (latitude == null) {
            throw new IllegalArgumentException("Latitude cannot be null");
        }
        if (longitude == null) {
            throw new IllegalArgumentException("Longitude cannot be null");
        }
        if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 || latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 || longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }
}