package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test {
    @GetMapping("/home")

    public String test (Model model) {
        model.addAttribute("message", "Đạt kuto");
        return "index";
    }
}
