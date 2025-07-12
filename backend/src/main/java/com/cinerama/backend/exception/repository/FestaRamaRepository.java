package com.cinerama.backend.exception.repository;

import com.cinerama.backend.entity.FestaRama;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing FestaRama entities.
 *
 * <p>This interface provides CRUD operations for the FestaRama entity
 * by extending the JpaRepository interface.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>save:</b> Saves a FestaRama entity to the database</li>
 *   <li><b>findById:</b> Retrieves a FestaRama entity by its ID</li>
 *   <li><b>findAll:</b> Retrieves all FestaRama entities</li>
 *   <li><b>deleteById:</b> Deletes a FestaRama entity by its ID</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This repository is used in the service layer to interact with the
 * database for FestaRama-related operations.</p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.cinerama.backend.entity.FestaRama
 */

public interface FestaRamaRepository extends JpaRepository<FestaRama, Long> {
}
