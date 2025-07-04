package org.rl.frontendService.controllers;

import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    String getHome(Model model) {
        model.addAttribute("content", "work");
        return "forward:/index.html";
    }
}
