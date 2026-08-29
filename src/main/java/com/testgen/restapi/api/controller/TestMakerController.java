package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestMakerController {

    @GetMapping("/test_maker")
    public String test_maker(Model model) {

        // fetch all subjects
        DatabaseManager databaseManager = new DatabaseManager();
        List<Subject> subjects = databaseManager.getAllSubjects();
        List<String> subjectsNames = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsNames.add(subject.getSubjectName());
        }
        model.addAttribute("subjects", subjectsNames);


        return "test_maker";
    }
}
