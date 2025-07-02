package org.rl.gateway.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "auth-service", url = "${services.auth}")
@Component
public interface AuthClient {
    @RequestMapping(method = {RequestMethod.POST}, value = {"/api/v1/auth/validate"})
    public Mono<Boolean> validate(@RequestParam(name = "token") String token);
}
