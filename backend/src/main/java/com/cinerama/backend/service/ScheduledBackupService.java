package com.cinerama.backend.service;

/**
 * Service interface for scheduling and running automatic database backups.
 */
public interface ScheduledBackupService {
    /**
     * Checks the current time and runs the automatic backup if the configured time matches.
     */
    void checkAndRunBackup();
}