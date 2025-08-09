package org.rl.gateway.config;

import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.result.view.MustacheViewResolver;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactivefeign.utils.HttpStatus;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;

/**
 * Exception handler for the gateway
 */
@ControllerAdvice
@Slf4j
public class GatewayExceptionHandler {

    /**
     * In case we get an error with an appropriate HTTP response code, redirect to the frontend page with the info
     * @param exception Exception with the status code
     * @return Redirect view
     */
    @ExceptionHandler(ResponseStatusException.class)
    public View handle(ResponseStatusException exception) {
        HttpStatusCode statusCode = exception.getStatusCode();
        String message = UriComponentsBuilder.newInstance()
                .path("/err")
                .queryParam("code", statusCode.value())
                .queryParam("message", HttpStatus.getStatusText(statusCode.value()))
                .build().encode().toUriString();
        log.debug(message);

        return (View) Rendering.redirectTo(message)
                .build()
                .view();

    }
}
