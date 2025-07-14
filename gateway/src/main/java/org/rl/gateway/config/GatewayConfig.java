package org.rl.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.rl.gateway.filters.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.beans.Customizer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Configuration of the Spring Cloud Gateway
 */
@Configuration
@Slf4j
public class GatewayConfig {
    @Value("${services.api}")
    private String apiUrl;
    @Value("${services.frontend}")
    private String frontendUrl;
    @Value("${services.auth}")
    private String authUrl;

    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Gateway route configuration
     * @param builder Builder object for the routes
     * @return Routes
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route.path("/api/v1/auth/**")
                        .uri(authUrl))
                .route(route -> route.path("/api/v1/posts", "/api/v1/posts/**", "/api/v1/resources", "/api/v1/resources/**")
                        .and()
                        .method(HttpMethod.POST, HttpMethod.PUT)
                        .filters(filter -> filter.filter(securityFilter))
                        .uri(apiUrl))
                .route(route -> route.path("/api/v1/posts", "/api/v1/posts/**", "/api/v1/resources", "/api/v1/resources/**")
                        .and()
                        .method(HttpMethod.GET)
                        .uri(apiUrl))
                .route(route -> route.path("/resources/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.TRACE, HttpMethod.HEAD)
                        .filters(filter -> filter
                                .prefixPath("/api/v1")
                        )
                        .uri(apiUrl))
                .route(route -> route.path("/", "/posts", "/posts/**", "/assets/**", "/favicon.ico", "/owner/login")
                        .uri(frontendUrl))
                .route(route -> route.path("/owner/newpost")
                        .filters(filter -> filter.filter(securityFilter))
                        .uri(frontendUrl))
                .build();
    }

    /**
     * Register an HTTPS redirect filter.
     * TODO: remove
     * @deprecated It's not really deprecated but it just doesn't really seem to work.
     * @return HTTPS redirect filter
     */
    @Bean
    public WebFilter httpsRedirectFilter() {
        return new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                URI originalUri = exchange.getRequest().getURI();
                log.debug("Hello from filter!");

                //here set your condition to http->https redirect
                List<String> forwardedValues = exchange.getRequest().getHeaders().get("x-forwarded-proto");
                if (forwardedValues != null && forwardedValues.contains("http")) {
                    try {
                        URI mutatedUri = new URI("https",
                                originalUri.getUserInfo(),
                                originalUri.getHost(),
                                originalUri.getPort(),
                                originalUri.getPath(),
                                originalUri.getQuery(),
                                originalUri.getFragment());
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                        response.getHeaders().setLocation(mutatedUri);
                        return Mono.empty();
                    } catch (URISyntaxException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
                return chain.filter(exchange);
            }
        };
    }

}
