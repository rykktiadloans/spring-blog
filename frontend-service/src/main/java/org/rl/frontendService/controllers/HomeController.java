package org.rl.frontendService.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that returns the home page
 */
@Controller
@Tag(name = "home_controller", description = "API for fetching Home and 404 pages")
public class HomeController {

    /**
     * Returns the homepage
     * @param model
     * @return Homepage
     */
    @GetMapping("/")
    @Operation(summary = "Returns the frontend")
    String getHome(Model model) {
        model.addAttribute("content", "work");
        return "forward:/index.html";
    }

    /**
     * Return the page when there is some sort of an error
     * @return Error page
     */
    @GetMapping("/err")
    @Operation(summary = "Returns the frontend")
    String getNotFound() {
        return "forward:/index.html";
    }
}
