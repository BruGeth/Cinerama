package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Device entity operations.
 * 
 * @author Cinerama Development Team
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    /**
     * Find all devices for a specific user.
     */
    List<Device> findByUserId(Long userId);
    
    /**
     * Find a device by user ID and token.
     */
    Optional<Device> findByUserIdAndToken(Long userId, String token);
    
    /**
     * Check if a device token exists for a user.
     */
    boolean existsByUserIdAndToken(Long userId, String token);
    
    /**
     * Delete all devices for a specific user.
     */
    void deleteByUserId(Long userId);
}
