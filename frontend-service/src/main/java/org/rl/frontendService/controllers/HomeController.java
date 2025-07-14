package org.rl.frontendService.controllers;

import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that returns the home page
 */
@Controller
public class HomeController {

    /**
     * Returns the homepage
     * @param model
     * @return
     */
    @GetMapping("/")
    String getHome(Model model) {
        model.addAttribute("content", "work");
        return "forward:/index.html";
    }
}
