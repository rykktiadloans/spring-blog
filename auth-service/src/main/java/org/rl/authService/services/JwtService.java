package org.rl.authService.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Service responsible for implementing logic relating to validating and generating JWT tokens
 */
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    private SecretKey key;

    private final Logger logger = LogManager.getLogger(JwtService.class);

    /**
     * Set the key from the JWT secret
     */
    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a new token for the user
     * @param username User's username
     * @return New JWT token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + this.jwtExpirationMs))
                .signWith(this.key)
                .compact();
    }

    /**
     * Extract the username from JWT token
     * @param token JWT token
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Extract the expiration date from JWT token
     * @param token JWT token
     * @return Expiration date
     */
    public Date getExpirationFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    /**
     * Validate the token
     * @param token JWT token to avlidate
     * @return True if the JWT token is valid, false otherwise.
     */
    public boolean validateJwtToken(String token) {
        this.logger.debug("AuthService: " + token);
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }
        catch (SecurityException e) {
            this.logger.debug("Invalid JWT signature: " + e.getMessage());
        }
        catch (MalformedJwtException e) {
            this.logger.debug("Malformed JWT token: " + e.getMessage());
        }
        catch (ExpiredJwtException e) {
            this.logger.debug("Expired JWT token: " + e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            this.logger.debug("JWT token is not supported: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            this.logger.debug("JWT token is empty: " + e.getMessage());
        }
        return false;
    }
}
