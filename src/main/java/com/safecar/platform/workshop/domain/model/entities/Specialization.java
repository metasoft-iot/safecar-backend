package com.safecar.platform.workshop.domain.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.safecar.platform.workshop.domain.model.valueobjects.Specializations;

/**
 * Entity representing a mechanic specialization within the system.
 * <p>
 * Maps to the {@code specializations} table and encapsulates the specialization's unique identifier
 * and name, provides utility methods for default specialization assignment, specialization creation, and validation.
 * </p>
 * 
 * @author SafeCar Platform Team
 * @version 1.0
 * @since 2025-11-07
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Specialization {

    /**
     * The unique identifier for the specialization.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the specialization, represented as an enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50, unique = true, nullable = false)
    private Specializations name;

    /**
     * Constructs a specialization with the specified name.
     *
     * @param name the specialization name
     */
    public Specialization(Specializations name) {
        this.name = name;
    }

    /**
     * Creates a specialization from the given string name.
     *
     * @param name the name of the specialization as a string
     * @return the corresponding {@code Specialization} instance
     * @throws IllegalArgumentException if the name does not match any specialization
     */
    public static Specialization toSpecializationFromName(String name) {
        return new Specialization(Specializations.valueOf(name.toUpperCase()));
    }

    /**
     * Returns the string representation of the specialization's name.
     *
     * @return the name of the specialization as a string
     */
    public String getStringName() {
        return this.name.name();
    }
}