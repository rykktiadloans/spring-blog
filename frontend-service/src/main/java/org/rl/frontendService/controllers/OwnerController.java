package org.rl.frontendService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OwnerController {
    @GetMapping("/owner/login")
    public String getLogin() {
        return "forward:/index.html";
    }

    @GetMapping("/owner/newpost")
    public String getNewPost() {
        return "forward:/index.html";
    }
}
