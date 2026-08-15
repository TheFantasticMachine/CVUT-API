package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.TestRequest;
import com.testgen.restapi.core.managers.PdfManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TemplateEngine templateEngine; // Thymeleaf template renderer

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestBody TestRequest request) {

        // 1. Populate Thymeleaf Context with data from frontend
        Context context = new Context();
        context.setVariable("variant", request.getTestVariant());
        context.setVariable("questions", request.getQuestions());

        // 2. Render templates/test_template.html into a pure HTML String
        String renderedHtml = templateEngine.process("test_template", context);

        // 3. Convert HTML String to PDF bytes using PdfManager
        byte[] pdfBytes = PdfManager.convertHtmlToPdf(renderedHtml);

        // 4. Set HTTP response headers for printable PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // "inline" opens the PDF in a new tab; use "attachment" to force download
        headers.setContentDispositionFormData("inline", "test_paper.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}