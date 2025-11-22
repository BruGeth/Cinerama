package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a user notification.
 * 
 * @author Cinerama Development Team
 */
@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * The user who receives this notification.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * Notification title.
     */
    @Column(nullable = false)
    private String title;
    
    /**
     * Notification message body.
     */
    @Column(nullable = false, length = 1000)
    private String message;
    
    /**
     * Notification type (e.g., "booking", "promotion", "system").
     */
    @Column(nullable = false)
    private String type;
    
    /**
     * Whether the notification has been read.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean isRead = false;
    
    /**
     * Optional data payload as JSON string.
     */
    @Column(columnDefinition = "TEXT")
    private String data;
    
    /**
     * Timestamp when the notification was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
