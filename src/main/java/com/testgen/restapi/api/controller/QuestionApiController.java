package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.util.AbstractElementVisitor14;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/question")
public class QuestionApiController {

    private final QuestionService questionService;

    @Autowired
    public QuestionApiController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // 1. Get single question by ID: GET /api/question/get?id=1
    @GetMapping("/get")
    public ResponseEntity<Question> getQuestion(@RequestParam int id) {
        Question question = questionService.getQuestionById(id);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(question);
    }

    // 2. Get all questions for a subject: GET /api/question/by-subject-id?subjectId=1
    @GetMapping("/by-subject-id")
    public ResponseEntity<List<Question>> getQuestionsBySubjectId(@RequestParam(required = false, defaultValue = "1") Integer subjectId) {
        List<Question> questions = questionService.getQuestionsBySubjectId(subjectId);
        return ResponseEntity.ok(questions);
    }

    // GET /api/question/by-subject-name?subjectName=name
    @GetMapping("/by-subject-name")
    public ResponseEntity<List<Question>> getQuestionsBySubjectName(@RequestParam String subjectName) {
        List<Question> questions = questionService.getQuestionsBySubjectName(subjectName);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/by-category-name")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@RequestParam String categoryName) {
        List<Question> questions = questionService.getQuestionsByCategoryName(categoryName);
        return ResponseEntity.ok(questions);
    }

    // 3. Get all approved questions: GET /api/question/all
    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }
}