package com.cinerama.backend.repository;

import com.cinerama.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity data access operations.
 *
 * <p>This repository provides data access methods for the User entity in the
 * Cinerama cinema booking system. It extends JpaRepository to inherit standard
 * CRUD operations and defines custom query methods for specific business requirements.</p>
 *
 * <h2>Inherited Operations:</h2>
 * <ul>
 *   <li>Standard CRUD operations (save, findById, findAll, delete)</li>
 *   <li>Pagination and sorting support</li>
 *   <li>Batch operations for performance optimization</li>
 *   <li>Existence checks and counting operations</li>
 * </ul>
 *
 * <h2>Custom Query Methods:</h2>
 * <ul>
 *   <li>Email-based user lookup for authentication</li>
 *   <li>Verification code lookup for account activation</li>
 * </ul>
 *
 * <h2>Query Method Conventions:</h2>
 * <ul>
 *   <li>Return Optional&lt;User&gt; for single result queries to handle null safely</li>
 *   <li>Use descriptive method names following Spring Data conventions</li>
 *   <li>Leverage automatic query generation from method names</li>
 * </ul>
 *
 * <h2>Transaction Management:</h2>
 * <ul>
 *   <li>Read operations are typically read-only</li>
 *   <li>Write operations are automatically wrapped in transactions</li>
 *   <li>Service layer should handle transaction boundaries for complex operations</li>
 * </ul>
 *
 * @author Cinerama Development Team
 * @see User for the entity definition
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * <p>This method is primarily used for user authentication and lookup operations.
     * The email serves as a natural unique identifier for users in the system,
     * making it the preferred method for user identification.</p>
     *
     * <h3>Query Details:</h3>
     * <ul>
     *   <li>Generated query: SELECT * FROM users WHERE email = ?</li>
     *   <li>Case-sensitive email matching (consider normalization)</li>
     *   <li>Leverages unique constraint on email field</li>
     * </ul>
     *
     * <h3>Usage Scenarios:</h3>
     * <ul>
     *   <li>User authentication during login process</li>
     *   <li>Profile retrieval in UserController</li>
     *   <li>Duplicate email validation during registration</li>
     *   <li>Password reset and account recovery operations</li>
     * </ul>
     *
     * <h3>Performance Considerations:</h3>
     * <ul>
     *   <li>Email field should be indexed for optimal query performance</li>
     *   <li>Unique constraint provides implicit index</li>
     *   <li>Consider email normalization for consistent lookups</li>
     * </ul>
     *
     * <p><strong>Implementation Note:</strong> Email comparison should be
     * case-insensitive in practice. Consider normalizing email values
     * to lowercase before storage and comparison.</p>
     *
     * @param email the email address to search for (case-sensitive)
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their verification code.
     *
     * <p>This method is used during the email verification process to locate
     * the user account associated with a specific verification code. It enables
     * the system to validate and activate user accounts.</p>
     *
     * <h3>Query Details:</h3>
     * <ul>
     *   <li>Generated query: SELECT * FROM users WHERE verification_code = ?</li>
     *   <li>Searches for exact match on 6-digit verification code</li>
     *   <li>Returns user account requiring activation</li>
     * </ul>
     *
     * <h3>Usage Scenarios:</h3>
     * <ul>
     *   <li>Email verification during account activation</li>
     *   <li>Verification code validation in authentication service</li>
     *   <li>Account recovery and email confirmation processes</li>
     * </ul>
     *
     * <h3>Security Considerations:</h3>
     * <ul>
     *   <li>Verification codes should be time-limited (handled by service)</li>
     *   <li>Consider rate limiting to prevent brute force attacks</li>
     *   <li>Codes should be single-use and cleared after verification</li>
     *   <li>Implement account lockout for multiple failed attempts</li>
     * </ul>
     *
     * <h3>Performance Considerations:</h3>
     * <ul>
     *   <li>Consider adding database index on verification_code field</li>
     *   <li>Codes are typically short-lived, so query frequency may be low</li>
     *   <li>Null verification codes (verified accounts) are excluded automatically</li>
     * </ul>
     *
     * <p><strong>Security Note:</strong> This method should be used in conjunction
     * with additional security measures such as rate limiting, attempt counting,
     * and time-based expiration to prevent abuse.</p>
     *
     * @param verificationCode the 6-digit verification code to search for
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findByVerificationCode(String verificationCode);
}
