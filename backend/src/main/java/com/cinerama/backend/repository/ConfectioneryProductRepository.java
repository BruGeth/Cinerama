package com.cinerama.backend.repository;

import com.cinerama.backend.entity.ConfectioneryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for accessing confectionery products in the database.
 * 
 * Purpose and improvements:
 * - Provides CRUD operations for ConfectioneryProduct entities.
 * - Includes custom query methods for finding products by category and by name.
 * - Used throughout the confectionery module for product management, stock validation, and purchase flows.
 */
public interface ConfectioneryProductRepository extends JpaRepository<ConfectioneryProduct, Long> {
    /**
     * Finds all confectionery products by category ID.
     * @param categoryId the ID of the category
     * @return list of products in the given category
     */
    List<ConfectioneryProduct> findByCategory_Id(Long categoryId);

    /**
     * Finds a confectionery product by its name.
     * @param name the name of the product
     * @return the product with the given name, or null if not found
     */
    ConfectioneryProduct findByName(String name);
}