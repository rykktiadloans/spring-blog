package org.rl.authService.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rl.authService.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Iterator;

/**
 * A filter adds the authentication info to the request based on the JWT token
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    private final Logger logger = LogManager.getLogger(JwtService.class);

    /**
     * Process the request and add an authentication to it if the JWT token is present and valid.
     * @param request HTTP Request
     * @param response HTTP Response
     * @param filterChain The Servlet filter chain
     * @throws ServletException Can throw a Servlet exception
     * @throws IOException Can throw an IO exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = this.parseJwtToken(request);
            if (jwtToken != null && this.jwtService.validateJwtToken(jwtToken)) {
                String username = this.jwtService.getUsernameFromToken(jwtToken);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        catch (Exception e) {
            this.logger.debug("Cannot set user auth: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Get the JWT token from the request
     * @param request HTTP request
     * @return A JWT token if present, null otherwise
     */
    private String parseJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
