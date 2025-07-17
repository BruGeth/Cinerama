package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.ConfectioneryOrderItem;
import com.cinerama.backend.entity.Order;
import com.cinerama.backend.repository.OrderRepository;
import com.cinerama.backend.service.PdfService;
import com.cinerama.backend.service.impl.PaymentServiceImpl;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentServiceImpl paymentService;

    @Override
    public byte[] generateConfectioneryOrderPdf(Long orderId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            List<ConfectioneryOrderItem> items = order.getConfectioneryItems();

            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Title
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Comprobante de Compra - Cinerama", titleFont);
            title.setSpacingAfter(18);
            document.add(title);

            // Date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy, H:mm:ss");
            String fecha = order.getTimestamp() != null ? order.getTimestamp().format(formatter) : "-";
            document.add(new Paragraph("Fecha: " + fecha));

            // User and email
            document.add(new Paragraph("Usuario: " + (order.getPayerName() != null ? order.getPayerName() : "-")));
            document.add(new Paragraph("Email: " + (order.getPayerEmail() != null ? order.getPayerEmail() : "-")));
            document.add(Chunk.NEWLINE);

            // Table of products
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            // Header row
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("Producto", headerFont));
            cell.setBackgroundColor(new Color(33, 150, 243));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cantidad", headerFont));
            cell.setBackgroundColor(new Color(33, 150, 243));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Precio", headerFont));
            cell.setBackgroundColor(new Color(33, 150, 243));
            table.addCell(cell);
            // Data rows
            Font rowFont = new Font(Font.HELVETICA, 12);
            for (ConfectioneryOrderItem item : items) {
                table.addCell(new Phrase(item.getProductName(), rowFont));
                table.addCell(new Phrase(String.valueOf(item.getQuantity()), rowFont));
                table.addCell(new Phrase("S/. " + String.format("%.2f", item.getUnitPrice()), rowFont));
            }
            document.add(table);

            // Total calculation
            double totalPEN = items.stream().mapToDouble(ConfectioneryOrderItem::getTotalPrice).sum();
            double totalUSD = paymentService.convertSolesToDollars(totalPEN);
            document.add(new Paragraph("Total: S/. " + String.format("%.2f", totalPEN) + "   (USD " + String.format("%.2f", totalUSD) + ")"));
            document.add(Chunk.NEWLINE);

            // Thank you message
            Font thanksFont = new Font(Font.HELVETICA, 13, Font.BOLD);
            Paragraph thanks = new Paragraph("¡Gracias por tu compra en Cinerama!", thanksFont);
            thanks.setSpacingBefore(10);
            document.add(thanks);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generando el PDF", e);
        }
        return baos.toByteArray();
    }
} 