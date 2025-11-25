package com.safecar.platform.shared.domain.model.aggregates;

import java.util.Date;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.domain.AbstractAggregateRoot;

import lombok.Getter;

/**
 * Abstract base class for aggregate roots that includes auditing fields.
 * <p>
 * Provides automatic management of creation and update timestamps,
 * as well as a generated primary key.
 * </p>
 *
 * @param <T> the type of the aggregate root.
 */
@Getter
@MappedSuperclass
// Use Spring Data JPA's AuditingEntityListener to populate @CreatedDate /
// @LastModifiedDate
@EntityListeners(AuditingEntityListener.class)
public class AuditableAbstractAggregateRoot<T extends AbstractAggregateRoot<T>> extends AbstractAggregateRoot<T> {
    /**
     * The unique identifier for the aggregate root.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    /**
     * The timestamp when the entity was created.
     * Set automatically and not updatable.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    /**
     * The timestamp when the entity was last updated.
     * Set automatically.
     */
    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;

    /**
     * Gets the unique identifier for this aggregate root.
     * 
     * @return the ID of this aggregate root
     */
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
