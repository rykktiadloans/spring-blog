package org.rl.authService.controllers;

import org.rl.authService.services.JwtService;
import org.rl.shared.model.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth", produces = "application/json")
public class SecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @GetMapping(path = "/csrf")
    CsrfToken getHome(CsrfToken csrfToken) {
        return csrfToken;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody UserRequest user) {
        Authentication auth = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return this.jwtService.generateToken(userDetails.getUsername());
    }

    @PostMapping(path = "/validate")
    public boolean validate(@RequestParam(name = "token") String token) {
        return this.jwtService.validateJwtToken(token);
    }



}
