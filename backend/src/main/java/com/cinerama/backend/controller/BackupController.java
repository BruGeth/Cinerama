package com.cinerama.backend.controller;

import com.cinerama.backend.service.BackupService;
import com.cinerama.backend.service.ScheduledBackupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class BackupController {

    private final BackupService backupService;
    private final ScheduledBackupService scheduledBackupService;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String CONFIG_PATH = "config/backup-config.json";

    public BackupController(BackupService backupService, ScheduledBackupService scheduledBackupService) {
        this.backupService = backupService;
        this.scheduledBackupService = scheduledBackupService;
    }

    /**
     * Endpoint to download a backup file by its name.
     */
    @GetMapping("/backup/download/{fileName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadBackup(@PathVariable String fileName) {
        try {
            File file = new File("backups" + File.separator + fileName);
            if (!file.exists()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint to generate a new backup using the configuration file.
     */
    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> generateBackup() {
        Map<String, String> response = new HashMap<>();

        try {
            Map<String, String> config = mapper.readValue(new File(CONFIG_PATH), 
                    new TypeReference<Map<String, String>>() {});

            String folderPath = config.getOrDefault("folderPath", "backups");
            String fileName   = config.getOrDefault("fileName", "backup.sql");

            String host     = config.getOrDefault("host", "localhost");
            String port     = config.getOrDefault("port", "3306");
            String user     = config.getOrDefault("user", "root");
            String password = config.getOrDefault("password", "");
            String database = config.getOrDefault("database", "cinerama_db");

            String message = backupService.generateBackup(host, port, user, password, database, folderPath, fileName);

            response.put("message", message);
            response.put("fileName", fileName);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "❌ Error generating backup: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint to manually trigger the automatic backup process.
     */
    @PostMapping("/backup/trigger-auto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> triggerAutomaticBackup() {
        String result;
        try {
            scheduledBackupService.checkAndRunBackup();
            result = "⏱️ Automatic backup executed.";
        } catch (Exception e) {
            result = "❌ Error executing automatic backup: " + e.getMessage();
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to save the backup configuration.
     */
    @PostMapping("/config/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> saveBackupConfig(@RequestBody Map<String, String> config) {
        Map<String, String> response = new HashMap<>();
        try {
            File configDir = new File("config");
            if (!configDir.exists()) configDir.mkdirs();

            // Console output in English
            System.out.println("📝 Configuration received:");
            System.out.println("Path: " + config.get("folderPath"));
            System.out.println("File: " + config.get("fileName"));
            System.out.println("Automatic time: " + config.get("autoTime"));

            mapper.writeValue(new File(CONFIG_PATH), config);
            response.put("message", "✅ Configuration saved successfully.");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "❌ Error saving configuration: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint to get the current backup configuration.
     */
    @GetMapping("/config/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBackupConfig() {
        try {
            Map<String, String> config = mapper.readValue(new File(CONFIG_PATH), 
                    new TypeReference<Map<String, String>>() {});
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "❌ Could not read configuration.");
            return ResponseEntity.status(500).body(error);
        }
    }
}