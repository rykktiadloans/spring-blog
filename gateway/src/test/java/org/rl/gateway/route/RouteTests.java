package org.rl.gateway.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.rl.shared.model.request.UserRequest;
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
import org.springframework.web.reactive.function.BodyInserters;
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

    @Autowired
    private ObjectMapper objectMapper;

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
    public void canGetListOfPosts() {
        stubFor(get(urlMatching("/api/v1/posts.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.get()
                .uri("/api/v1/posts")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
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
    public void canGetListOfAnyPosts() {
        stubFor(post(urlMatching("/api/v1/posts/anystate.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.post()
                .uri("/api/v1/posts/anystate")
                .header("Authorization", "Bearer token")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canGetAnySinglePost() {
        stubFor(post(urlEqualTo("/api/v1/posts/anystate/1"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.post()
                .uri("/api/v1/posts/anystate/1")
                .header("Authorization", "Bearer token")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void errorWhenGettingListOfAnyPostsUnauthorized() {
        stubFor(post(urlMatching("/api/v1/posts/anystate.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.post()
                .uri("/api/v1/posts/anystate")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void errorWhenGettingAnySinglePostUnauthorized() {
        stubFor(post(urlEqualTo("/api/v1/posts/anystate/1"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.post()
                .uri("/api/v1/posts/anystate/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void canGetResources() {
        stubFor(get(urlMatching("/api/v1/resources/.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG.toString())
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.get()
                .uri("/api/v1/resources/bang.png")
                .accept(MediaType.IMAGE_JPEG)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canGetResourcesWithShorthand() {
        stubFor(get(urlMatching("/api/v1/resources/.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG.toString())
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.get()
                .uri("/resources/bang.png")
                .accept(MediaType.IMAGE_JPEG)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canGetPostsofResources() {
        stubFor(get(urlMatching("/api/v1/resources/.*/posts"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.get()
                .uri("/api/v1/resources/bang.png/posts")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canGetListOfResources() {
        stubFor(post(urlMatching("/api/v1/resources/list"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.post()
                .uri("/api/v1/resources/list")
                .header("Authorization", "Bearer token")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void errorWhenGetListOfResourcesUnauthorized() {
        stubFor(post(urlMatching("/api/v1/resources/list"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.post()
                .uri("/api/v1/resources/list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void canCreateResource() {
        stubFor(post(urlEqualTo("/api/v1/resources"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_CREATED))
        );

        this.webTestClient.post()
                .uri("/api/v1/resources")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer garbled")
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void cantCreateResourceIfUnauthorized() {
        stubFor(post(urlEqualTo("/api/v1/resource"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_CREATED))
        );

        this.webTestClient.post()
                .uri("/api/v1/resources")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void canDeleteResource() {
        stubFor(delete(urlMatching("/api/v1/resources/.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.delete()
                .uri("/api/v1/resources/name")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer garbled")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void cantDeleteResourceIfUnauthorized() {
        stubFor(delete(urlEqualTo("/api/v1/resources/.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(HttpStatus.SC_CREATED))
        );

        this.webTestClient.delete()
                .uri("/api/v1/resources/name")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
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

    @Test
    public void canGetRss() {
        stubFor(get(urlEqualTo("/api/v1/rss"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_RSS_XML_VALUE)
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.get()
                .uri("/api/v1/rss")
                .accept(MediaType.APPLICATION_RSS_XML)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void canGetRssWithShorthand() {
        stubFor(get(urlEqualTo("/api/v1/rss"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_RSS_XML_VALUE)
                        .withStatus(HttpStatus.SC_OK))
        );

        this.webTestClient.get()
                .uri("/rss")
                .accept(MediaType.APPLICATION_RSS_XML)
                .exchange()
                .expectStatus().isOk();
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
    public void canGetApiDocs() {
        stubFor(get(urlEqualTo("/v3"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/api/v3")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void canGetAuthDocs() {
        stubFor(get(urlEqualTo("v3"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .withStatus(HttpStatus.SC_OK)));

        this.webTestClient.get()
                .uri("/auth/v3")
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
