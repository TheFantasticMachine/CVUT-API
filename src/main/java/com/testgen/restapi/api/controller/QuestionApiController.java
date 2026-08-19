package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.QuestionRequest;
import com.testgen.restapi.api.service.QuestionService;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/question")
public class QuestionApiController {

    private final DatabaseManager dbManager = new DatabaseManager();

    private final QuestionService questionService;

    @Autowired
    public QuestionApiController(QuestionService questionService) {
        this.questionService = questionService;
    }

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

    @GetMapping("/get")
    public Question getQuestion(@RequestParam Integer id) {
        Optional<Question> question = questionService.getQuestion(id);

        if (question.isPresent()) {
            return (Question) question.get();
        }

        return null;
    }
}