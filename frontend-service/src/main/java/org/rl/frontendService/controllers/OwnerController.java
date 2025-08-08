package org.rl.frontendService.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for returning pages that only the owner should have access to
 */
@Controller
@Tag(name = "owner_controller", description = "API for fetching pages for logging in and making new posts")
public class OwnerController {
    /**
     * Return the page with the login form
     * @return Page with the login form
     */
    @GetMapping("/owner/login")
    @Operation(summary = "Returns the frontend")
    public String getLogin() {
        return "forward:/index.html";
    }

    /**
     * Return the page with the new post form
     * @return Page with the new post form
     */
    @GetMapping("/owner/newpost")
    @Operation(summary = "Returns the frontend")
    public String getNewPost() {
        return "forward:/index.html";
    }
}
