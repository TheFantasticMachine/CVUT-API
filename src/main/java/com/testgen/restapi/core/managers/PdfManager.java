package com.testgen.restapi.core.managers;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;

public class PdfManager {

    public static byte[] convertHtmlToPdf(String htmlString) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 🔑 Point iText to the static resources directory
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri("src/main/resources/static/");

        HtmlConverter.convertToPdf(htmlString, outputStream, properties);
        return outputStream.toByteArray();
    }
}