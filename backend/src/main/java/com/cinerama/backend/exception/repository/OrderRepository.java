package com.cinerama.backend.exception.repository;

import com.cinerama.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing Order entities from the database.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves an Order entity based on its associated PayPal order ID.
     * Useful for checking if a PayPal transaction has already been recorded.
     *
     * @param paypalOrderId the PayPal-generated order ID
     * @return an Optional containing the matched Order if found, or empty otherwise
     */
    boolean existsByPaypalOrderId(String paypalOrderId);
}