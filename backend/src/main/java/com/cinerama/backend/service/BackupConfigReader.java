package com.cinerama.backend.service;

import com.cinerama.backend.dto.BackupConfigDTO;

/**
 * Service interface for reading the backup configuration.
 */
public interface BackupConfigReader {
    /**
     * Reads the backup configuration from the configuration source.
     * @return BackupConfigDTO object with the configuration, or null if not found or error.
     */
    BackupConfigDTO readConfig();
}