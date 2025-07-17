// BackupConfigReaderImpl.java
package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.BackupConfigDTO;
import com.cinerama.backend.service.BackupConfigReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Implementation of BackupConfigReader to read backup configuration from a JSON file.
 */
@Component
public class BackupConfigReaderImpl implements BackupConfigReader {

    /**
     * Reads the backup configuration from the config/backup-config.json file.
     * @return BackupConfigDTO object if config exists, otherwise null.
     */
    @Override
    public BackupConfigDTO readConfig() {
        try {
            File configFile = new File("config/backup-config.json");
            if (!configFile.exists()) {
                // Console output in English
                System.out.println("⚠️ Backup configuration file not found.");
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(configFile, BackupConfigDTO.class);
        } catch (Exception e) {
            // Console output in English
            System.out.println("❌ Error reading backup configuration: " + e.getMessage());
            return null;
        }
    }
}