package org.rl.authService.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * An authentication entry point for JWTs
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    /**
     * What to do in case there is an issue with authorization
     * @param request HTTP request
     * @param response HTTP response
     * @param authException The exception
     * @throws IOException Can throw if there is an issue
     * @throws ServletException Can throw if there is an issue
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");

    }
}
