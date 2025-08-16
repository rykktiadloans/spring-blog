package org.rl.gateway.route;

import com.github.tomakehurst.wiremock.matching.ContentPattern;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rl.gateway.clients.AuthClient;
import org.rl.gateway.config.ServicesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import wiremock.org.apache.hc.core5.http.HttpHeaders;
import wiremock.org.apache.hc.core5.http.HttpStatus;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        //properties = {"services.api=http://127.0.0.1:${test.port}"}
)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@ExtendWith(MockitoExtension.class)
public class RouteTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ServicesConfig servicesConfig;

    @MockitoBean
    private AuthClient authClient;

    @TestConfiguration
    static class RouteTestsConfig {
        public RouteTestsConfig(@Value("${wiremock.server.port}") int wiremockPort,
                ServicesConfig servicesConfig) {
            servicesConfig.setApiUrl("http://localhost:" + wiremockPort);
            servicesConfig.setAuthUrl("http://localhost:" + wiremockPort);
            servicesConfig.setFrontendUrl("http://localhost:" + wiremockPort);

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
    public void canGetSinglePost() {
        stubFor(get(urlEqualTo("/api/v1/posts/1"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/api/v1/posts/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canCreatePost() {
        stubFor(post(urlEqualTo("/api/v1/posts"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_CREATED))
        );

        this.webTestClient.post()
                .uri("/api/v1/posts")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer garbled")
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void cantCreatePostIfUnauthorized() {
        stubFor(post(urlEqualTo("/api/v1/posts"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_CREATED))
        );

        this.webTestClient.post()
                .uri("/api/v1/posts")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }



}
