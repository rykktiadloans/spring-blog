package org.rl.frontendService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    String getHome(Model model) {
        model.addAttribute("content", "wor");
        return "home";
    }
}
