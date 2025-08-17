package org.rl.gateway.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rl.gateway.clients.AuthClient;
import org.rl.gateway.config.ServicesConfig;
import org.rl.shared.model.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import wiremock.org.apache.hc.core5.http.HttpHeaders;
import wiremock.org.apache.hc.core5.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@ExtendWith(MockitoExtension.class)
public class AuthRouteTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ServicesConfig servicesConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthClient authClient;

    @TestConfiguration
    static class RouteTestsConfig {
        public RouteTestsConfig(@Value("${wiremock.server.port}") int wiremockPort,
                                ServicesConfig servicesConfig) {
            servicesConfig.setAuthUrl("http://localhost:" + wiremockPort);

        }
    }

    @BeforeEach
    public void setupAuthClient() {
        when(this.authClient.validate(ArgumentMatchers.any(String.class)))
                .thenAnswer(invocationOnMock -> {
                    String token = invocationOnMock.getArgument(0, String.class);
                    if(token != null && !token.isEmpty()) {
                        return Mono.just(true);
                    }
                    return Mono.just(false);
                });
    }
    @Test
    public void canLogin() throws JsonProcessingException {
        UserRequest user = new UserRequest("username", "password");
        String userSerialized = this.objectMapper.writeValueAsString(user);
        stubFor(post(urlEqualTo("/api/v1/auth/login"))
                .withRequestBody(containing(userSerialized))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.post()
                .uri("/api/v1/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userSerialized))
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void canGetExpiration() {
        stubFor(post(urlMatching("/api/v1/auth/expiration.*"))
                .withQueryParam("token", equalTo("sample.token"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.post()
                .uri("/api/v1/auth/expiration?token=sample.token")
                .header("Authorization", "Bearer sample.token")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void gettingExpirationErrorsOnUnauthorized() {
        stubFor(post(urlMatching("/api/v1/auth/expiration.*"))
                .withQueryParam("token", equalTo("sample.token"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.post()
                .uri("/api/v1/auth/expiration?token=sample.token")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }
    @Test
    public void canGetAuthDocs() {
        stubFor(get(urlEqualTo("/v3"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/auth/v3")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }
}
