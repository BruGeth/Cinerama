package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.BackupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cinerama.backend.service.ScheduledBackupService;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class ScheduledBackupServiceImpl implements ScheduledBackupService {

    private final BackupService backupService;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String CONFIG_PATH = "config/backup-config.json";

    public ScheduledBackupServiceImpl(BackupService backupService) {
        this.backupService = backupService;
    }

    // Checks the current time and runs the automatic backup if the configured time matches
    @Scheduled(cron = "0 * * * * *")
    public void checkAndRunBackup() {
        try {
            Map<String, String> config = mapper.readValue(new File(CONFIG_PATH), Map.class);

            String configuredTime = config.getOrDefault("autoTime", "00:00");
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

            System.out.println("🕒 Checking current time:");
            System.out.println("Current: " + currentTime);
            System.out.println("Configured: " + configuredTime);

            if (configuredTime.equals(currentTime)) {
                System.out.println("⏰ Running automatic backup...");

                String host = config.getOrDefault("host", "localhost");
                String port = config.getOrDefault("port", "3306");
                String user = config.getOrDefault("user", "root");
                String password = config.getOrDefault("password", "");
                String database = config.getOrDefault("database", "cinerama_db");

                String folderPath = config.getOrDefault("folderPath", "backups");
                String fileName = config.getOrDefault("fileName", "backup.sql");

                String result = backupService.generateBackup(host, port, user, password, database, folderPath, fileName);
                System.out.println(result);
            }
        } catch (IOException e) {
            System.out.println("❌ Could not read configuration file: " + e.getMessage());
        }
    }
}