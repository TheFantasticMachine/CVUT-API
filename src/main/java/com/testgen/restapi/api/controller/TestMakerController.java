package com.testgen.restapi.api.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestMakerController {

    @GetMapping("/test_maker")
    public String testMaker(HttpSession session) {
        if (session == null || session.getAttribute("currentUser") == null) {
            return "redirect:/login";
        }
        return "test_maker";
    }
}