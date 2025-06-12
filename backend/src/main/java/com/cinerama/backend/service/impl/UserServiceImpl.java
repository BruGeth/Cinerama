package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.UserService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ByteArrayInputStream exportUsersToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Usuarios");

            // Main title style
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setColor(IndexedColors.DARK_RED.getIndex());
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Centered title: "User Registration - CINERAMA"
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(2);
            titleCell.setCellValue("Registro de Usuarios - CINERAMA");
            titleCell.setCellStyle(titleStyle);

            // Merge cells C1 to F1 for the title
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 2, 5));

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Style for normal cells
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // Create header at row 1 (index 1), starting at column C (index 2)
            Row headerRow = sheet.createRow(1);
            String[] columnas = {"ID", "Nombre", "Email", "Rol"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(2 + i); // Column C = index 2
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Insert users from row 3 onwards
            List<User> users = userRepository.findAll();
            int rowIdx = 2;
            for (User user : users) {
                Row row = sheet.createRow(rowIdx++);
                Cell cell0 = row.createCell(2);
                cell0.setCellValue(user.getId());
                cell0.setCellStyle(cellStyle);

                Cell cell1 = row.createCell(3);
                cell1.setCellValue(user.getName());
                cell1.setCellStyle(cellStyle);

                Cell cell2 = row.createCell(4);
                cell2.setCellValue(user.getEmail());
                cell2.setCellStyle(cellStyle);

                Cell cell3 = row.createCell(5);
                cell3.setCellValue(user.getRole().getName());
                cell3.setCellStyle(cellStyle);
            }

            // Autofit width of columns C to F
            for (int i = 2; i <= 5; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }

}

