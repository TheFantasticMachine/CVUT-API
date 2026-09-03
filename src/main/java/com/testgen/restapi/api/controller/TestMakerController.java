package com.testgen.restapi.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestMakerController {

    @GetMapping("/test_maker")
    public String testMaker() {
        return "test_maker";
    }
}