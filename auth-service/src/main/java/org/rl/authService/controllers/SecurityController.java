package org.rl.authService.controllers;

import lombok.extern.slf4j.Slf4j;
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

import java.util.Date;

/**
 * REST controller for security related things
 */
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/auth", produces = "application/json")
public class SecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Process user credentials and return a corresponding JWT token
     * @param user User credentials
     * @return JWT token
     */
    @PostMapping(path = "/login")
    public String login(@RequestBody UserRequest user) {
        Authentication auth = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return this.jwtService.generateToken(userDetails.getUsername());
    }

    /**
     * Is the JWT token valid?
     * @param token JWT token to validate
     * @return True if valid, false otherwise
     */
    @PostMapping(path = "/validate")
    public boolean validate(@RequestParam(name = "token") String token) {
        boolean res = this.jwtService.validateJwtToken(token);
        log.debug("RES: " + res);
        return res;
    }

    /**
     * Return the expiration date of the token
     * @param token JWT token
     * @return Its expiration date
     */
    @PostMapping(path = "/expiration")
    public Date expiration(@RequestParam(name = "token") String token) {
        return this.jwtService.getExpirationFromToken(token);
    }



}
