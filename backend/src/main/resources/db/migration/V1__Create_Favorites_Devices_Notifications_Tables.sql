-- Migration: Add Favorites, Devices, and Notifications Tables
-- Date: 2025-11-21
-- Description: Creates tables for user favorites, push notification devices, and notifications

-- =====================================================
-- 1. CREATE FAVORITES TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT unique_user_movie UNIQUE (user_id, movie_id),
    CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorites_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for better query performance
CREATE INDEX idx_favorites_user_id ON favorites(user_id);
CREATE INDEX idx_favorites_movie_id ON favorites(movie_id);

-- =====================================================
-- 2. CREATE DEVICES TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS devices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    provider VARCHAR(255) NOT NULL COMMENT 'Push notification provider (e.g., expo)',
    token VARCHAR(500) NOT NULL COMMENT 'Expo push notification token',
    platform VARCHAR(255) NOT NULL COMMENT 'Device platform (ios, android, web)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT unique_user_token UNIQUE (user_id, token),
    CONSTRAINT fk_devices_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for better query performance
CREATE INDEX idx_devices_user_id ON devices(user_id);
CREATE INDEX idx_devices_token ON devices(token);

-- =====================================================
-- 3. CREATE NOTIFICATIONS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    type VARCHAR(255) NOT NULL COMMENT 'Notification type (booking, promotion, system, etc.)',
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    data TEXT COMMENT 'Optional JSON data payload',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for better query performance
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_user_is_read ON notifications(user_id, is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at DESC);

-- =====================================================
-- 4. VERIFICATION QUERIES
-- =====================================================
-- Run these to verify the tables were created successfully:

-- SELECT COUNT(*) FROM favorites;
-- SELECT COUNT(*) FROM devices;
-- SELECT COUNT(*) FROM notifications;

-- SHOW CREATE TABLE favorites;
-- SHOW CREATE TABLE devices;
-- SHOW CREATE TABLE notifications;

-- =====================================================
-- 5. ROLLBACK (if needed)
-- =====================================================
-- Uncomment and run these commands to rollback the migration:

-- DROP TABLE IF EXISTS notifications;
-- DROP TABLE IF EXISTS devices;
-- DROP TABLE IF EXISTS favorites;
