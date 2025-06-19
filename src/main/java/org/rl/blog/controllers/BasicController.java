package org.rl.blog.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1", produces = "application/json")
public class BasicController {

    @GetMapping(path = "/csrf")
    CsrfToken getHome(CsrfToken csrfToken) {
        return csrfToken;
    }
}
