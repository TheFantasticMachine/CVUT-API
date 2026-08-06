package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.QuestionRequest;
import com.testgen.restapi.api.service.QuestionService;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/question")
    public Question getQuestion(@RequestParam Integer id) {
        Optional<Question> question = questionService.getQuestion(id);

        if (question.isPresent()) {
            return (Question) question.get();
        }

        return null;
    }

    @PostMapping("/api/question/new")
    public ResponseEntity createQuestion (@RequestBody QuestionRequest request) {

        DatabaseManager databaseManager = new DatabaseManager();
        try (Connection connection = databaseManager.getConnection() ) {
            // get the values

            // send to db

            Statement statement = connection.createStatement();
            String sql = "insert into questions (questionText, correctAnswer, otherAnswer, categoryID, status)" +
                    "values ()";
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }
}
