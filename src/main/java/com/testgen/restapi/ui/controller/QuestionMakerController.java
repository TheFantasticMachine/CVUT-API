package com.testgen.restapi.ui.controller;

import com.testgen.restapi.core.Globals;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionMakerController {

    @GetMapping("/question_maker")
    public String question_maker(Model model) {

        model.addAttribute("subjects", Globals.subjects);
        model.addAttribute("categories", Globals.categories);

        return "question_maker";
    }
}
