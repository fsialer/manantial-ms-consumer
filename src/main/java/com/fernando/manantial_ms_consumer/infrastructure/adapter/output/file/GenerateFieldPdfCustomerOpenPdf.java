package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.file;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class GenerateFieldPdfCustomerOpenPdf implements GenerateField<Customer>{
    @Override
    public byte[] generate(Customer data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document= new Document(PageSize.A4,50,50,60,60);
        try{
            PdfWriter.getInstance(document,outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Information customer",titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Name: " + data.getName(),textFont));
            document.add(new Paragraph("Lastname: " + data.getLastName(),textFont));
            document.add(new Paragraph("Age: " + data.getAge().toString(),textFont));
            document.add(new Paragraph("Birthday: " + data.getBirthDate(),textFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Created: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), textFont));
            document.close();
            log.info("✅ PDF generado con diseño personalizado: customer_{}.pdf",data.getId());
        }catch(Exception e){
            log.error("Error generating PDF for customer {}: {}",data.getId(),e.getMessage());
        }finally {
            document.close();
        }
        return outputStream.toByteArray();
    }
}
