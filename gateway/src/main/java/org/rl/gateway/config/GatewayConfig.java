package org.rl.gateway.config;

import org.rl.gateway.filters.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.beans.Customizer;

@Configuration
public class GatewayConfig {
    @Value("${services.api}")
    private String apiUrl;
    @Value("${services.frontend}")
    private String frontendUrl;
    @Value("${services.auth}")
    private String authUrl;

    @Autowired
    private SecurityFilter securityFilter;
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route.path("/api/v1/auth/**")
                        .uri(authUrl))
                .route(route -> route.path("/api/v1/posts", "/api/v1/posts/**")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(filter -> filter.filter(securityFilter))
                        .uri(apiUrl))
                .route(route -> route.path("/api/v1/posts", "/api/v1/posts/**")
                        .and()
                        .method(HttpMethod.GET)
                        .uri(apiUrl))
                .route(route -> route.path("/", "/posts", "/posts/**", "/assets/**", "/favicon.ico")
                        .uri(frontendUrl))
                .build();
    }
}
