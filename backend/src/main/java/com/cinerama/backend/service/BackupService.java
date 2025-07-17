package com.cinerama.backend.service;

/**
 * Service interface for generating database backups.
 */
public interface BackupService {
    /**
     * Generates a backup with default folder and file name.
     * @param host Database host
     * @param port Database port
     * @param user Database user
     * @param password Database password
     * @param database Database name
     * @return Status message of the backup process
     */
    String generateBackup(String host, String port, String user, String password, String database);

    /**
     * Generates a backup with custom folder and file name.
     * @param host Database host
     * @param port Database port
     * @param user Database user
     * @param password Database password
     * @param database Database name
     * @param folderPath Path to save the backup
     * @param fileName Name of the backup file
     * @return Status message of the backup process
     */
    String generateBackup(String host, String port, String user, String password, String database, String folderPath, String fileName);
}