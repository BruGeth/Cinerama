package com.cinerama.backend.repository;

import com.cinerama.backend.entity.SpecialFunction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing SpecialFunction entities.
 *
 * <p>This interface provides CRUD operations for the SpecialFunction entity
 * by extending the JpaRepository interface.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>save:</b> Saves a SpecialFunction entity to the database</li>
 *   <li><b>findById:</b> Retrieves a SpecialFunction entity by its ID</li>
 *   <li><b>findAll:</b> Retrieves all SpecialFunction entities</li>
 *   <li><b>deleteById:</b> Deletes a SpecialFunction entity by its ID</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This repository is used in the service layer to interact with the
 * database for SpecialFunction-related operations.</p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.cinerama.backend.entity.SpecialFunction
 */

/* === Repository interface for SpecialFunction entity === */
public interface SpecialFunctionRepository extends JpaRepository<SpecialFunction, Long>{
}
