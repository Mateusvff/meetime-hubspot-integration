package com.meetime.hubspot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.contact.CreateContactRequest;
import com.meetime.hubspot.domain.contact.Property;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"hubspot.api.url=http://localhost:8082"})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ContactControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OAuthProperties oAuthProperties;

    @MockitoBean
    private Bucket bucket;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8082));
        wireMockServer.start();
        configureFor("localhost", 8082);
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @Test
    @Sql("/insert-token-data.sql")
    void testCreateContact() throws Exception {
        when(bucket.tryConsume(1)).thenReturn(true);

        Property properties = new Property("email", "", "", "", "", "", "");
        CreateContactRequest createContactRequest = new CreateContactRequest(properties, null);
        String requestBody = new ObjectMapper().writeValueAsString(createContactRequest);

        when(oAuthProperties.getClientId()).thenReturn("client_id");
        when(oAuthProperties.getClientSecret()).thenReturn("client_secret");

        // Mocking the Refresh Token HubSpot API Call
        WireMock.stubFor(WireMock.post(WireMock.urlPathEqualTo("/oauth/v1/token"))
                .withQueryParam("grant_type", WireMock.equalTo("refresh_token"))
                .withQueryParam("client_id", WireMock.equalTo("client_id"))
                .withQueryParam("client_secret", WireMock.equalTo("client_secret"))
                .withQueryParam("refresh_token", WireMock.equalTo("refresh_token"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\": \"access_token\", \"refresh_token\": \"refresh_token\", \"expires_in\": 3600}")));

        WireMock.stubFor(WireMock.post(WireMock.urlPathEqualTo("/crm/v3/objects/contacts"))
                .withHeader("Authorization", WireMock.equalTo("Bearer access_token"))
                .withHeader("Content-Type", WireMock.equalTo("application/json"))
                .withRequestBody(WireMock.equalTo(requestBody))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)));

        mockMvc.perform(post("/api/contacts/create")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated());

    }

    @Test
    void testCreateContactWithRateLimitExceeded() throws Exception {
        when(bucket.tryConsume(1)).thenReturn(false);

        Property properties = new Property("email", "", "", "", "", "", "");
        CreateContactRequest createContactRequest = new CreateContactRequest(properties, null);
        String requestBody = new ObjectMapper().writeValueAsString(createContactRequest);

        mockMvc.perform(post("/api/contacts/create")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void testCreateContactWithInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/contacts/create")
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateContactWithInvalidContentType() throws Exception {
        Property properties = new Property("email", "", "", "", "", "", "");
        CreateContactRequest createContactRequest = new CreateContactRequest(properties, null);
        String requestBody = new ObjectMapper().writeValueAsString(createContactRequest);

        mockMvc.perform(post("/api/contacts/create")
                        .contentType("application/xml")
                        .content(requestBody))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void testCreateContactWithInvalidMethod() throws Exception {
        mockMvc.perform(get("/api/contacts/create")
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testCreateContactNotFound() throws Exception {
        Property properties = new Property("email", "", "", "", "", "", "");
        CreateContactRequest createContactRequest = new CreateContactRequest(properties, null);
        String requestBody = new ObjectMapper().writeValueAsString(createContactRequest);

        mockMvc.perform(post("/api/contacts")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

}