package org.rl.authService.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class JwtServiceTest {
    @Autowired
    private JwtService jwtService;

    @Test
    void canGenerateAToken() {
        String token = this.jwtService.generateToken("username");
        assertThat(token)
                .isNotEmpty();
    }

    @Test
    void canExtractUsernameFromAToken() {
        String username = "username";
        String token = this.jwtService.generateToken(username);
        String extractedUsername = this.jwtService.getUsernameFromToken(token);
        assertThat(extractedUsername)
                .isEqualTo(username);
    }

    @Test
    void canExtractExpirationDateFromAToken() {
        String token = this.jwtService.generateToken("username");
        Date expiration = this.jwtService.getExpirationFromToken(token);

        assertThat(expiration)
                .isAfter(Date.from(Instant.now()));
    }

    @Test
    void canValidateAValidToken() {
        String token = this.jwtService.generateToken("username");
        assertThat(this.jwtService.validateJwtToken(token))
                .isTrue();
    }

    @Test
    void canUnvalidateABadToken() {
        assertThat(this.jwtService.validateJwtToken("afsdffas.asdfa.adsf.adsf"))
                .isFalse();
    }
}
