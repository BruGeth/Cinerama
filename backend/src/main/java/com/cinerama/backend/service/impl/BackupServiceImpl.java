package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.BackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackupServiceImpl implements BackupService {

    @Override
    public String generateBackup(String host, String port, String user, String password, String database) {
        String date = LocalDate.now().toString();
        String fileName = "backup-" + date + ".sql";
        return generateBackup(host, port, user, password, database, "backups", fileName);
    }

    @Override
    public String generateBackup(String host, String port, String user, String password,
                                 String database, String folderPath, String fileName) {
        Logger logger = LoggerFactory.getLogger(BackupServiceImpl.class);

        // Ensure the file name ends with .sql
        if (!fileName.toLowerCase().endsWith(".sql")) {
            fileName += ".sql";
        }

        try {
            File folder = new File(folderPath);
            // Create the backup folder if it does not exist
            if (!folder.exists() && !folder.mkdirs()) {
                return "❌ Could not create folder: " + folderPath;
            }

            File outputFile = new File(folder, fileName);
            String fullPath = outputFile.getAbsolutePath();

            String mysqldumpPath = "C:\\xampp\\mysql\\bin\\mysqldump.exe";

            List<String> commandParts = new ArrayList<>();
            commandParts.add(mysqldumpPath);
            commandParts.add("-h");
            commandParts.add(host);
            commandParts.add("-P");
            commandParts.add(port);
            commandParts.add("-u");
            commandParts.add(user);
            if (password != null && !password.isEmpty()) {
                commandParts.add("-p" + password);
            }
            commandParts.add(database);

            logger.info("🛠️ Running: " + String.join(" ", commandParts));
            logger.info("📁 Output file: " + fullPath);

            ProcessBuilder builder = new ProcessBuilder(commandParts);
            builder.redirectOutput(outputFile);
            builder.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process process = builder.start();
            int exitCode = process.waitFor();

            return (exitCode == 0)
                    ? "✅ Backup generated successfully: " + fileName
                    : "❌ Backup failed. Code: " + exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("❌ Error during backup", e);
            return "❌ Error during backup: " + e.getMessage();
        }
    }
}