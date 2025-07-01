package com.cinerama.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * JPA Entity representing a user in the Cinerama cinema booking system.
 *
 * <p>This entity encapsulates all user-related information including authentication
 * credentials, profile data, and account status. The entity supports email-based
 * authentication with account verification functionality.</p>
 *
 * <h2>Database Mapping:</h2>
 * <ul>
 *   <li>Table: "users" with auto-generated primary key</li>
 *   <li>Email field has unique constraint for authentication</li>
 *   <li>Optimistic locking and audit fields included</li>
 * </ul>
 *
 * <h2>Authentication Flow:</h2>
 * <ul>
 *   <li>User registers with email and password</li>
 *   <li>Account created in disabled state (enabled = false)</li>
 *   <li>Verification code sent to email for activation</li>
 *   <li>Account enabled after successful email verification</li>
 * </ul>
 *
 * <h2>Security Features:</h2>
 * <ul>
 *   <li>Password stored as bcrypt hash (never plain text)</li>
 *   <li>Email verification required for account activation</li>
 *   <li>Unique email constraint prevents duplicate accounts</li>
 *   <li>Account status controlled via enabled flag</li>
 * </ul>
 *
 * @author Cinerama Development Team
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique identifier for the user account.
     *
     * <p>Auto-generated primary key using database identity strategy.
     * This ID is used internally for database relationships and should
     * not be exposed to clients in most cases.</p>
     *
     * <h3>Database Configuration:</h3>
     * <ul>
     *   <li>Primary key with auto-increment strategy</li>
     *   <li>Generated automatically on entity persistence</li>
     *   <li>Used for internal references and relationships</li>
     * </ul>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User's display name for personalization and identification.
     *
     * <p>This field stores the user's full name or preferred display name
     * used throughout the application for personalization and user interface
     * elements. The name is displayed in greetings, profiles, and bookings.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Validated at both entity and DTO levels</li>
     *   <li>No specific length restrictions defined</li>
     * </ul>
     *
     * <h3>Usage:</h3>
     * <ul>
     *   <li>Displayed in user interface elements</li>
     *   <li>Included in LoginResponse for client personalization</li>
     *   <li>Used in booking confirmations and communications</li>
     * </ul>
     */
    @NotBlank(message = "Name is mandatory")
    private String name;

    /**
     * User's email address serving as unique authentication identifier.
     *
     * <p>This field serves as the primary identifier for user authentication
     * and must be unique across the system. The email is used for login,
     * account verification, and system communications.</p>
     *
     * <h3>Database Constraints:</h3>
     * <ul>
     *   <li>Unique constraint prevents duplicate registrations</li>
     *   <li>Not null constraint ensures data integrity</li>
     *   <li>Used as natural key for user lookup</li>
     * </ul>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must conform to standard email format (RFC 5322)</li>
     *   <li>Uniqueness enforced at database level</li>
     * </ul>
     *
     * <h3>Security Considerations:</h3>
     * <ul>
     *   <li>Case-insensitive comparison recommended for authentication</li>
     *   <li>Used as username in JWT token generation</li>
     *   <li>Email verification required for account activation</li>
     * </ul>
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * User's hashed password for authentication security.
     *
     * <p>This field stores the user's password as a bcrypt hash, never in plain text.
     * The password is hashed during registration and compared during authentication
     * using secure password encoding mechanisms.</p>
     *
     * <h3>Security Implementation:</h3>
     * <ul>
     *   <li>Stored as bcrypt hash with configurable strength</li>
     *   <li>Never stored or transmitted in plain text</li>
     *   <li>Validated using BCryptPasswordEncoder during authentication</li>
     * </ul>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Password complexity rules enforced at service level</li>
     *   <li>Hash validation ensures data integrity</li>
     * </ul>
     *
     * <p><strong>Security Note:</strong> This field should be excluded from
     * JSON serialization and never exposed in API responses. Consider using
     * @JsonIgnore annotation or dedicated response DTOs.</p>
     */
    @NotBlank(message = "Password is mandatory")
    private String password;
    
    /**
     * Role associated with the user.
     *
     * <p>Many-to-one relationship with the Role entity. Loads the role eagerly (EAGER fetch type).
     * The "role_id" field in the database acts as a foreign key and cannot be null.</p>
     */  
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    /**
     * Account activation status for email verification control.
     *
     * <p>This flag determines whether the user account is active and can
     * be used for authentication. New accounts are created with enabled = false
     * and must be activated through email verification.</p>
     *
     * <h3>Account Lifecycle:</h3>
     * <ul>
     *   <li>Default value: false (account disabled)</li>
     *   <li>Set to true after successful email verification</li>
     *   <li>Can be toggled for account suspension/reactivation</li>
     * </ul>
     *
     * <h3>Authentication Impact:</h3>
     * <ul>
     *   <li>Disabled accounts cannot authenticate</li>
     *   <li>Used in authentication service validation</li>
     *   <li>Prevents unauthorized access to unverified accounts</li>
     * </ul>
     */

    @Column(nullable = false)
    private boolean enabled = false;

    /**
     * Timestamp of account creation for audit and analytics.
     *
     * <p>This field automatically captures the exact date and time when
     * the user account was created. It's managed by Hibernate and cannot
     * be modified after initial creation.</p>
     *
     * <h3>Automatic Management:</h3>
     * <ul>
     *   <li>Set automatically on entity creation</li>
     *   <li>Immutable after initial persistence</li>
     *   <li>Uses server timestamp for consistency</li>
     * </ul>
     *
     * <h3>Usage:</h3>
     * <ul>
     *   <li>Audit trail for account creation</li>
     *   <li>Analytics and reporting purposes</li>
     *   <li>Account age calculations</li>
     *   <li>Compliance and data retention policies</li>
     * </ul>
     */
    @CreationTimestamp
    private LocalDateTime createdAt;
}
