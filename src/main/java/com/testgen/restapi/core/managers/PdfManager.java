package com.testgen.restapi.core.managers;

import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;

public class PdfManager {
    private static final String PDF_FILEPATH = "/output/pdf/hello.pdf";

    public static byte[] convertHtmlToPdf(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        return outputStream.toByteArray();
    }
}
