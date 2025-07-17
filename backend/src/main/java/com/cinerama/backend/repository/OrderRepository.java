package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing Order entities from the database.
 * 
 * Changes and improvements:
 * - Provides CRUD operations for Order entities.
 * - Includes custom query methods for PayPal integration.
 * - Used to check for existing PayPal transactions and retrieve orders by PayPal order ID.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Checks if an Order exists with the given PayPal order ID.
     * Useful for preventing duplicate payment processing.
     *
     * @param paypalOrderId the PayPal-generated order ID
     * @return true if an order with the given PayPal order ID exists, false otherwise
     */
    boolean existsByPaypalOrderId(String paypalOrderId);

    /**
     * Retrieves an Order entity based on its associated PayPal order ID.
     * Used to fetch the local order for a PayPal transaction.
     *
     * @param paypalOrderId the PayPal-generated order ID
     * @return an Optional containing the matched Order if found, or empty otherwise
     */
    Optional<Order> findByPaypalOrderId(String paypalOrderId);
}