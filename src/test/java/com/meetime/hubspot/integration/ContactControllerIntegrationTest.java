package com.meetime.hubspot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.contact.CreateContactRequest;
import com.meetime.hubspot.domain.contact.Property;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"hubspot.api.url=http://localhost:8089"})
@AutoConfigureMockMvc
public class ContactControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OAuthProperties oAuthProperties;

    @MockitoBean
    private Bucket bucket;

    @BeforeEach
    void setup() {
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @Test
    void testCreateContact() throws Exception {
        Property properties = new Property("email", "", "", "", "", "", "");
        CreateContactRequest createContactRequest = new CreateContactRequest(properties, null);
        String requestBody = new ObjectMapper().writeValueAsString(createContactRequest);

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
        Mockito.when(bucket.tryConsume(1)).thenReturn(false);

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