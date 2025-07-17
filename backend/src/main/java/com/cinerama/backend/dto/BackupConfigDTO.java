package com.cinerama.backend.dto;

import lombok.Data;

/**
 * Data Transfer Object for backup configuration.
 * Contains folder path, file name, and automatic backup time.
 */
@Data
public class BackupConfigDTO {
    private String folderPath;
    private String fileName;
    private String autoTime;
}