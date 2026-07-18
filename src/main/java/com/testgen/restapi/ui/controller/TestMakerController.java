package com.testgen.restapi.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestMakerController {

    @GetMapping("/test_maker")
    public String test_maker() {
        return "test_maker";
    }
}
