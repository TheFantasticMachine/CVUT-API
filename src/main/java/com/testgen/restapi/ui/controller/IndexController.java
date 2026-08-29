package com.testgen.restapi.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/main")
    public String main() { return "main"; }

    @GetMapping("/main2")
    public String main2() { return "main2"; }

    @GetMapping("/icons")
    public String icons() { return "icons"; }

    @GetMapping("/login")
    public String login() { return "login"; }
}
