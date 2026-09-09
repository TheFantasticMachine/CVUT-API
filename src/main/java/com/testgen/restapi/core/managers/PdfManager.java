package com.testgen.restapi.core.managers;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class PdfManager {

    /**
     * Converts a processed HTML document string into a binary PDF byte array.
     */
    public byte[] generatePdfFromHtml(String htmlContent) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ConverterProperties properties = new ConverterProperties();
        // Generates the PDF document directly into memory
        HtmlConverter.convertToPdf(htmlContent, outputStream, properties);

        return outputStream.toByteArray();
    }
}