package org.rl.gateway.filters;

import io.micrometer.core.ipc.http.HttpSender;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rl.gateway.clients.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * A gateway filter that checks the request for authorization
 */
@Component
public class SecurityFilter implements GatewayFilter {
    @Autowired
    @Lazy
    private AuthClient authClient;
    private final Logger logger = LogManager.getLogger(SecurityFilter.class);

    /**
     * Extract JWT token from the request and validate it using authorization microservice. If the token is absent or invalid, then return HTTP Unauthorized
     * @param exchange Request-response exchange
     * @param chain Gateway filter chain
     * @return Call next filter or short circuit
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        val request = exchange.getRequest();
        List<String> headers = request.getHeaders().getOrEmpty("Authorization");
        String authHeader = headers.isEmpty() ? "" : headers.get(0);
        if(!authHeader.startsWith("Bearer ")) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        }
        return this.authClient.validate(authHeader.substring(7))
                .flatMap(res -> {
                    logger.debug("RES: " + res);
                    if(!res) {
                        exchange.getResponse()
                                .setStatusCode(HttpStatus.UNAUTHORIZED);
                        return Mono.empty();
                    }
                    return chain.filter(exchange);
                });
    }
}
