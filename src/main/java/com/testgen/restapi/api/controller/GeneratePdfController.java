package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.SavedTest;
import com.testgen.restapi.api.model.TestRequest;
import com.testgen.restapi.api.service.SavedTestService;
import com.testgen.restapi.core.managers.PdfManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

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

    public record CorrectAnswerItem(
            int questionNumber,
            String assignment,
            String correctLetters,
            String correctTexts
    ) {}

    public record VariantResult(
            String letter,
            List<CorrectAnswerItem> answers
    ) {}

    @PostMapping(value = "/answers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateAnswersPdf(@RequestBody TestRequest request) {
        try {
            String variantLetter = request.getVariant() != null ? request.getVariant() : "A";
            List<TestRequest.QuestionData> questions = request.getQuestions() != null ? request.getQuestions() : Collections.emptyList();

            List<CorrectAnswerItem> answerItems = new ArrayList<>();
            String alphabet = "ABCDEFGHIJ";

            for (int i = 0; i < questions.size(); i++) {
                TestRequest.QuestionData q = questions.get(i);
                List<String> letters = new ArrayList<>();
                List<String> texts = new ArrayList<>();

                if (q.getAnswers() != null) {
                    for (int j = 0; j < q.getAnswers().size(); j++) {
                        TestRequest.AnswerData ans = q.getAnswers().get(j);
                        if (ans.isCorrect()) {
                            String let = j < alphabet.length() ? String.valueOf(alphabet.charAt(j)) : String.valueOf(j + 1);
                            letters.add(let);
                            texts.add(ans.getAnswerText());
                        }
                    }
                }

                String joinedLetters = letters.isEmpty() ? "-" : String.join(", ", letters);
                String joinedTexts = texts.isEmpty() ? "—" : String.join("; ", texts);

                answerItems.add(new CorrectAnswerItem(
                        i + 1,
                        q.getAssignment(),
                        joinedLetters,
                        joinedTexts
                ));
            }

            List<VariantResult> variants = List.of(new VariantResult(variantLetter, answerItems));

            Context context = new Context();
            context.setVariable("title", request.getTitle() != null ? request.getTitle() : "Test");
            context.setVariable("subject", request.getSubject() != null ? request.getSubject() : "");
            context.setVariable("variant", variantLetter);
            context.setVariable("variants", variants);

            String renderedHtml = templateEngine.process("answer_sheet", context);
            byte[] pdfBytes = pdfManager.generatePdfFromHtml(renderedHtml);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            String safeName = (request.getTitle() != null && !request.getTitle().isBlank()
                    ? request.getTitle().replaceAll("[^a-zA-Z0-9-_\\.]", "_")
                    : "test") + "_Var_" + variantLetter + "_Odpovedi.pdf";

            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(safeName, StandardCharsets.UTF_8)
                    .build());
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Generování PDF s odpověďmi selhalo: " + e.getMessage()));
        }
    }

    @PostMapping(value = {"/generate", ""}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generatePdf(@RequestBody TestRequest request) {
        try {
            Context context = new Context();
            context.setVariable("title", request.getTitle() != null ? request.getTitle() : "Zkouškový test");
            context.setVariable("subject", request.getSubject() != null ? request.getSubject() : "");
            context.setVariable("variant", request.getVariant() != null ? request.getVariant() : "A");
            context.setVariable("questions", request.getQuestions() != null ? request.getQuestions() : Collections.emptyList());

            String renderedHtml = templateEngine.process("test_template", context);
            byte[] pdfBytes = pdfManager.generatePdfFromHtml(renderedHtml);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            String safeName = (request.getTitle() != null && !request.getTitle().isBlank()
                    ? request.getTitle().replaceAll("[^a-zA-Z0-9-_\\.]", "_")
                    : "test") + "_Var_" + (request.getVariant() != null ? request.getVariant() : "A") + ".pdf";

            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(safeName, StandardCharsets.UTF_8)
                    .build());
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Generování PDF selhalo: " + e.getMessage()));
        }
    }
}