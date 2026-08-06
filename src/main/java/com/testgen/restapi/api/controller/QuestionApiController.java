package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.QuestionRequest;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/question")
public class QuestionApiController {

    private final DatabaseManager dbManager = new DatabaseManager();

    @PostMapping("/add")
    public ResponseEntity<Question> addQuestion(@RequestBody QuestionRequest request) {

        System.out.println("[API] Adding Question with Difficulty: " + request.getDifficulty());

        // Insert question and answers into MySQL/H2
        Question savedQuestion = dbManager.createQuestion(request);

        if (savedQuestion != null) {
            return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}