package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Notification entity operations.
 * 
 * @author Cinerama Development Team
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Find all notifications for a specific user, ordered by creation date descending.
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Find unread notifications for a specific user.
     */
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    /**
     * Count unread notifications for a user.
     */
    long countByUserIdAndIsReadFalse(Long userId);
}
