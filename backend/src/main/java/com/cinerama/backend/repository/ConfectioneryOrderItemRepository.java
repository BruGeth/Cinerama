package com.cinerama.backend.repository;

import com.cinerama.backend.entity.ConfectioneryOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing ConfectioneryOrderItem entities.
 * Provides data access methods for confectionery order items.
 */
@Repository
public interface ConfectioneryOrderItemRepository extends JpaRepository<ConfectioneryOrderItem, Long> {
    
    /**
     * Finds all confectionery order items associated with a specific order.
     * Useful for retrieving all items in a particular order.
     *
     * @param orderId the ID of the order
     * @return list of confectionery order items for the specified order
     */
    List<ConfectioneryOrderItem> findByOrderId(Long orderId);
    
    /**
     * Finds all confectionery order items for a specific product.
     * Useful for tracking sales history of a particular product.
     *
     * @param productId the ID of the confectionery product
     * @return list of confectionery order items for the specified product
     */
    List<ConfectioneryOrderItem> findByProductId(Long productId);
} 