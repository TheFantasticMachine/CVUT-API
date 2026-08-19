package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.TestRequest;
import com.testgen.restapi.api.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestTemplateController {

    @GetMapping("/test_template")
    public String test_maker(Model model) {

        List<Question> questions = new ArrayList<>();
        QuestionService questionService = new QuestionService();
        questions.add(questionService.getQuestion(10).get());
        TestRequest request = new TestRequest("test", questions);

        model.addAttribute("variant", request.getTestVariant());
        model.addAttribute("questions", request.getQuestions());

        return "test_template";
    }
}
