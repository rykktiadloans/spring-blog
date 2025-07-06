package org.rl.frontendService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(name = "/owner")
public class OwnerController {
    @GetMapping("/login")
    public String getLogin() {
        return "forward:/index.html";
    }

    @GetMapping("/newpost")
    public String getNewPost() {
        return "forward:/index.html";
    }
}
