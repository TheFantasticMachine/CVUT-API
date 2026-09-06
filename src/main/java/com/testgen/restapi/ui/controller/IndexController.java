package com.testgen.restapi.ui.controller;

import com.testgen.restapi.api.service.SubjectService;
import com.testgen.restapi.core.Globals;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String mainPage(Model model, HttpSession session) {
        if (session == null || session.getAttribute("currentUser") == null) {
            return "redirect:/login";
        }
        // Pass subjects into template so the "New Test" modal can list them dynamically
        model.addAttribute("subjects", Globals.subjects);
        return "main";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 1. Fetch current session without creating a new one if missing
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 2. Invalidate removes all attributes ("currentUser") and deletes session from RAM
            session.invalidate();
        }
        // 3. Send browser back to the login screen
        return "redirect:/login";
    }
}
