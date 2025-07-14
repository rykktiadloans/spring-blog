package org.rl.frontendService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for returning pages that only the owner should have access to
 */
@Controller
public class OwnerController {
    /**
     * Return the page with the login form
     * @return Page with the login form
     */
    @GetMapping("/owner/login")
    public String getLogin() {
        return "forward:/index.html";
    }

    /**
     * Return the page with the new post form
     * @return Page with the new post form
     */
    @GetMapping("/owner/newpost")
    public String getNewPost() {
        return "forward:/index.html";
    }
}
