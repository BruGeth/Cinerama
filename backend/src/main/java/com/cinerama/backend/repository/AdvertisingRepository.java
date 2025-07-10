package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Advertising;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing Advertising entities.
 *
 * <p>Provides CRUD operations for the Advertising entity by extending the JpaRepository interface.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><b>save:</b> Saves an Advertising entity to the database</li>
 * <li><b>findById:</b> Retrieves an Advertising entity by its ID</li>
 * <li><b>findAll:</b> Retrieves all Advertising entities</li>
 * <li><b>deleteById:</b> Deletes an Advertising entity by its ID</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This repository is used in the service layer to interact with the database for Advertising-related operations.</p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.cinerama.backend.entity.Advertising
 */

/* === Repository interface for Advertising entity, providing CRUD operations === */
public interface AdvertisingRepository extends JpaRepository<Advertising, Long> {
}
