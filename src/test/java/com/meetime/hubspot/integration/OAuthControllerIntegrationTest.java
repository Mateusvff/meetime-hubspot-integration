package com.meetime.hubspot.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.auth.AuthorizationURL;
import com.meetime.hubspot.service.OAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"hubspot.api.url=http://localhost:8089"})
@AutoConfigureMockMvc
public class OAuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OAuthProperties oAuthProperties;

    private OAuthService oAuthService;

    @BeforeEach
    void setup() {
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @Test
    void testGenerateAuthorizationUrl() throws Exception {
        AuthorizationURL authorizationURL = new AuthorizationURL("https://app.hubspot.com/oauth/authorize?client_id=client_id&redirect_uri=http://localhost:8080/test-callback&scope=test_scope");

        when(oAuthService.retrieveAuthorizationUrl()).thenReturn(authorizationURL);

        mockMvc.perform(get("/oauth/authorize"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("authorization_url").value("https://app.hubspot.com/oauth/authorize?client_id=client_id&redirect_uri=http://localhost:8080/test-callback&scope=test_scope"));
    }

    @Test
    void testHandleCallback() throws Exception {
        when(oAuthProperties.getClientId()).thenReturn("client_id");
        when(oAuthProperties.getClientSecret()).thenReturn("client_secret");
        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/test-callback");

        WireMock.stubFor(WireMock.post(WireMock.urlPathEqualTo("/oauth/v1/token"))
                .withQueryParam("grant_type", WireMock.equalTo("authorization_code"))
                .withQueryParam("client_id", WireMock.equalTo("client_id"))
                .withQueryParam("client_secret", WireMock.equalTo("client_secret"))
                .withQueryParam("redirect_uri", WireMock.equalTo("http://localhost:8080/test-callback"))
                .withQueryParam("code", WireMock.equalTo("authorization_code"))
                .withHeader("Content-Type", WireMock.equalTo("application/x-www-form-urlencoded"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\": \"access_token\", \"refresh_token\": \"refresh_token\", \"expires_in\": 3600}")));

        mockMvc.perform(get("/oauth/callback")
                        .queryParam("code", "authorization_code"))
                .andExpect(status().isOk());
    }

    @Test
    void testHandleCallbackWithErrorWhileCallingHubspot() throws Exception {
        when(oAuthProperties.getClientId()).thenReturn("client_id");
        when(oAuthProperties.getClientSecret()).thenReturn("client_secret");
        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/test-callback");

        WireMock.stubFor(WireMock.post(WireMock.urlPathEqualTo("/oauth/v1/token"))
                .withQueryParam("grant_type", WireMock.equalTo("authorization_code"))
                .withQueryParam("client_id", WireMock.equalTo("client_id"))
                .withQueryParam("client_secret", WireMock.equalTo("client_secret"))
                .withQueryParam("redirect_uri", WireMock.equalTo("http://localhost:8080/test-callback"))
                .withQueryParam("code", WireMock.equalTo("authorization_code"))
                .withHeader("Content-Type", WireMock.equalTo("application/x-www-form-urlencoded"))
                .willReturn(WireMock.aResponse()
                        .withStatus(400)));

        mockMvc.perform(get("/oauth/callback")
                        .queryParam("code", "authorization_code"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status").value(500))
                .andExpect(jsonPath("error").value("Internal Server Error"))
                .andExpect(jsonPath("message").value("Error while exchanging authorization code for access token"))
        ;
    }

    @Test
    void testHandleCallbackWithInvalidMethod() throws Exception {
        mockMvc.perform(post("/oauth/callback")
                        .queryParam("code", "authorization_code"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testHandleCallbackNotFound() throws Exception {
        mockMvc.perform(post("/oauth/calback")
                        .queryParam("code", "authorization_code"))
                .andExpect(status().isNotFound());
    }

}