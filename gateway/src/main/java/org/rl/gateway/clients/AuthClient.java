package org.rl.gateway.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * API client for the authorization microservice
 */
@ReactiveFeignClient(name = "auth-service", url = "${services.auth}")
@Component
public interface AuthClient {
    /**
     * Validate a JWT token
     * @param token JWT token
     * @return True if it is valid, false otherwise
     */
    @RequestMapping(method = {RequestMethod.POST}, value = {"/api/v1/auth/validate"})
    public Mono<Boolean> validate(@RequestParam(name = "token") String token);
}
