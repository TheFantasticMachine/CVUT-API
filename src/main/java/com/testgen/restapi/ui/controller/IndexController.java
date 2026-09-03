package com.testgen.restapi.ui.controller;

import com.testgen.restapi.api.service.SubjectService;
import com.testgen.restapi.core.Globals;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // 1. Root route automatically loads Login
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // 2. Login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 3. Main teacher dashboard
    @GetMapping("/main")
    public String mainPage(Model model) {
        // Pass subjects into template so the "New Test" modal can list them dynamically
        model.addAttribute("subjects", SubjectService.getAllSubjects());
        return "main";
    }
}
