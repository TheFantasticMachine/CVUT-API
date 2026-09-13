package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.TestRequest;
import com.testgen.restapi.core.managers.PdfManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "*")
public class GeneratePdfController {

    private final SpringTemplateEngine templateEngine;
    private final PdfManager pdfManager;

    @Autowired
    public GeneratePdfController(SpringTemplateEngine templateEngine, PdfManager pdfManager) {
        this.templateEngine = templateEngine;
        this.pdfManager = pdfManager;
    }

    @PostMapping(value = {"/generate", ""}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generatePdf(@RequestBody TestRequest request) {
        try {
            PrintStream stream = new PrintStream(System.out);
            stream.print(request.getSubject());
            // 1. Bind variables matching your payload names
            Context context = new Context();
            context.setVariable("title", request.getTitle() != null ? request.getTitle() : "Examination Paper");
            context.setVariable("subject", request.getSubject() != null ? request.getSubject() : "");
            context.setVariable("variant", request.getVariant() != null ? request.getVariant() : "A");
            context.setVariable("questions", request.getQuestions() != null ? request.getQuestions() : Collections.emptyList());

            // 2. Render templates/test_template.html
            String renderedHtml = templateEngine.process("test_template", context);

            // 3. Convert HTML to PDF byte stream
            byte[] pdfBytes = pdfManager.generatePdfFromHtml(renderedHtml);

            // 4. Send attachment
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            String safeName = (request.getTitle() != null && !request.getTitle().isBlank()
                    ? request.getTitle().replaceAll("[^a-zA-Z0-9-_\\.]", "_")
                    : "exam") + "_Var_" + (request.getVariant() != null ? request.getVariant() : "A") + ".pdf";

            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(safeName, StandardCharsets.UTF_8)
                    .build());
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "PDF generation failed: " + e.getMessage()));
        }
    }
}