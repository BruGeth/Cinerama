package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.Order;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generateTicketSummary(Order order) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A6);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font valueFont = new Font(Font.HELVETICA, 10);

            Paragraph title = new Paragraph("🎫 Recibo de Compra - Cinerama", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Número de orden: ", labelFont));
            document.add(new Paragraph(String.valueOf(order.getId()), valueFont));
            document.add(new Paragraph("Fecha: ", labelFont));
            document.add(new Paragraph(order.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), valueFont));
            document.add(new Paragraph("Estado: ", labelFont));
            document.add(new Paragraph(order.getStatus(), valueFont));
            document.add(new Paragraph("Moneda: ", labelFont));
            document.add(new Paragraph(order.getCurrency(), valueFont));
            document.add(new Paragraph("Monto total: ", labelFont));
            document.add(new Paragraph("$" + order.getAmount(), valueFont));
            document.add(new Paragraph("Email del comprador: ", labelFont));
            document.add(new Paragraph(order.getPayerEmail(), valueFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Gracias por tu compra 🎬", valueFont));
            document.add(new Paragraph("www.cinerama.pe", valueFont));

        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF del recibo", e);
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }
}
