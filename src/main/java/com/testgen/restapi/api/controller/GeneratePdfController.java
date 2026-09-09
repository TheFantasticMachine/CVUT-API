package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.TestRequest;
import com.testgen.restapi.core.managers.PdfManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.List;
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

    public record GenTestRequest(
        String variant,
        String subject,
        String  title,
        Map<String, ?> questions
    ){};

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateTestPdf(@RequestBody GenTestRequest request) {
        try {
            // 1. Prepare Thymeleaf Context with data from the frontend
            Context context = new Context();
            context.setVariable("variant", request.variant);
            context.setVariable( "subject", request.subject);
            context.setVariable("questions", request.questions);

            // 2. Render templates/test_template.html to a raw HTML string
            String renderedHtml = templateEngine.process("test_template", context);

            // 3. Convert rendered HTML to PDF binary
            byte[] pdfBytes = pdfManager.generatePdfFromHtml(renderedHtml);

            // 4. Build HTTP response headers for browser download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            String safeFileName = (request.title != null ? request.title : "exam")
                    .replaceAll("[^a-zA-Z0-9-_\\.]", "_") + ".pdf";
            headers.setContentDispositionFormData("attachment", safeFileName);
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("PDF Generation Failed: " + e.getMessage()).getBytes());
        }
    }
}