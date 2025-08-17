package org.rl.gateway.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
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
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@ExtendWith(MockitoExtension.class)
public class FrontendRouteTests {
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
    public void canGetHome() {
        stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canGetPostsList() {
        stubFor(get(urlEqualTo("/posts"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/posts")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void canGetPostPage() {
        stubFor(get(urlMatching("/posts/.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/posts/54")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void canGetFrontendAsset() {
        stubFor(get(urlMatching("/assets/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/assets/pictureframe.png")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canGetLoginPage() {
        stubFor(get(urlMatching("/owner/login"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/owner/login")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canGetNewpostPage() {
        stubFor(get(urlMatching("/owner/newpost"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/owner/newpost")
                .header("Authorization", "Bearer token")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void errorGetNewpostPageWhenUnauthorized() {
        stubFor(get(urlMatching("/owner/newpost"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/owner/newpost")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isUnauthorized();
    }
    @Test
    public void canGetResourcesPage() {
        stubFor(get(urlMatching("/owner/resources"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/owner/resources")
                .header("Authorization", "Bearer token")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void errorGetResourcesPageWhenUnauthorized() {
        stubFor(get(urlMatching("/owner/resources"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/owner/resources")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void canGetErrorPage() {
        stubFor(get(urlMatching("/err"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/err")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void canGetFrontendDocs() {
        stubFor(get(urlEqualTo("/v3"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/frontend/v3")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }
}
