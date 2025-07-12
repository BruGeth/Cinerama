package com.cinerama.backend.exception.repository;

import com.cinerama.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Event entities.
 *
 * <p>This interface provides CRUD operations for the Event entity
 * by extending the JpaRepository interface.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>save:</b> Saves an Event entity to the database</li>
 *   <li><b>findById:</b> Retrieves an Event entity by its ID</li>
 *   <li><b>findAll:</b> Retrieves all Event entities</li>
 *   <li><b>deleteById:</b> Deletes an Event entity by its ID</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This repository is used in the service layer to interact with the
 * database for Event-related operations.</p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.cinerama.backend.entity.Event
 */

public interface EventRepository extends JpaRepository<Event,Long> {
}
