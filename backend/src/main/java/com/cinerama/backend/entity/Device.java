package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a user's registered device for push notifications.
 * 
 * <p>Stores Expo push notification tokens associated with user devices.</p>
 * 
 * @author Cinerama Development Team
 */
@Entity
@Table(name = "devices", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "token"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * The user who owns this device.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * Push notification provider (e.g., "expo").
     */
    @Column(nullable = false)
    private String provider;
    
    /**
     * Expo push notification token.
     */
    @Column(nullable = false, length = 500)
    private String token;
    
    /**
     * Device platform (ios, android, web).
     */
    @Column(nullable = false)
    private String platform;
    
    /**
     * Timestamp when the device was registered.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
