package com.testgen.restapi.ui.controller;

import com.testgen.restapi.api.repo.CategoryRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionMakerController {
    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/question_maker")
    public String questionMaker(Model model, HttpSession session) {
        if (session == null || session.getAttribute("currentUser") == null) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryRepo.findAll());
        return "question_maker";
    }
}
