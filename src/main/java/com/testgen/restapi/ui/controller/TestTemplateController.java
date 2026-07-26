package com.testgen.restapi.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestTemplateController {

    @GetMapping("/test_template")
    public String test_maker() {
        return "test_template";
    }
}
