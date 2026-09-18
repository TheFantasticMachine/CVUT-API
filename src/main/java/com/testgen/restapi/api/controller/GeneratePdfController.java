package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.SavedTest;
import com.testgen.restapi.api.model.TestRequest;
import com.testgen.restapi.api.service.SavedTestService;
import com.testgen.restapi.core.managers.PdfManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "*")
public class GeneratePdfController {

    private final SpringTemplateEngine templateEngine;
    private final PdfManager pdfManager;
    private final SavedTestService savedTestService;

    @Autowired
    public GeneratePdfController(SpringTemplateEngine templateEngine, PdfManager pdfManager, SavedTestService savedTestService) {
        this.templateEngine = templateEngine;
        this.pdfManager = pdfManager;
        this.savedTestService = savedTestService;
    }

    public record VariantResult (
            String letter,
            List<String> answers
    ){}

    @PostMapping("/anwsers/{id}")
    public String generateAnswerSheet(@PathVariable Integer test_id, Model model) {
        // 1) get data
        Optional<SavedTest> savedTest = savedTestService.getTestByID(test_id);
        if (savedTest.isEmpty()) {
            return null;
        }
        SavedTest test = savedTest.get();
        JSONObject config = new JSONObject(test.getTestConfig());
        JSONObject data = new JSONObject(test.getTestData());

        // 2) evaluate data
        List<VariantResult> variants = new ArrayList<>();
        for (String letter : (List<String>) config.get("variants")) {
            List<JSONObject> question = (List<JSONObject>) data.get(letter);

            VariantResult variant = new VariantResult(letter, null);
        }

        // 3) set to pdf
        model.addAttribute("subject", config.getString("subject_name"));
        model.addAttribute("title", config.getString("title"));

        return "answer_sheet";
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