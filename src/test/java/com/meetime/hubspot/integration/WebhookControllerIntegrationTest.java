package com.meetime.hubspot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.webhook.ContactCreatedWebhook;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@ActiveProfiles("test")
@AutoConfigureMockMvc
class WebhookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OAuthProperties oAuthProperties;

    @Test
    void testReceiveWebhook() throws Exception {
        ContactCreatedWebhook webhook = new ContactCreatedWebhook(1L, 2L, 3L, 0L, 0L, "contact.creation", 0L, 0L, "", "");

        ContactCreatedWebhook[] webhookArray = new ContactCreatedWebhook[]{webhook};
        String requestBody = new ObjectMapper().writeValueAsString(webhookArray);

        String clientSecret = "secret";
        String signature = DigestUtils.sha256Hex(clientSecret + requestBody);

        when(oAuthProperties.getClientSecret()).thenReturn("secret");

        mockMvc.perform(post("/api/webhook/receive")
                        .header("X-HubSpot-Signature", signature)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void testReceiveWebhookWithInvalidSignature() throws Exception {
        ContactCreatedWebhook webhook = new ContactCreatedWebhook(1L, 2L, 3L, 0L, 0L, "contact.creation", 0L, 0L, "", "");

        ContactCreatedWebhook[] webhookArray = new ContactCreatedWebhook[]{webhook};
        String requestBody = new ObjectMapper().writeValueAsString(webhookArray);

        String clientSecret = "secret";
        String signature = DigestUtils.sha256Hex(clientSecret + requestBody);

        when(oAuthProperties.getClientSecret()).thenReturn("other_secret");

        mockMvc.perform(post("/api/webhook/receive")
                        .header("X-HubSpot-Signature", signature)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testReceiveWebhookWithInvalidMethod() throws Exception {
        ContactCreatedWebhook webhook = new ContactCreatedWebhook(1L, 2L, 3L, 0L, 0L, "contact.creation", 0L, 0L, "", "");

        ContactCreatedWebhook[] webhookArray = new ContactCreatedWebhook[]{webhook};
        String requestBody = new ObjectMapper().writeValueAsString(webhookArray);

        String clientSecret = "secret";
        String signature = DigestUtils.sha256Hex(clientSecret + requestBody);

        when(oAuthProperties.getClientSecret()).thenReturn("secret");

        mockMvc.perform(get("/api/webhook/receive")
                        .header("X-HubSpot-Signature", signature)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testReceiveWebhookNotFound() throws Exception {
        ContactCreatedWebhook webhook = new ContactCreatedWebhook(1L, 2L, 3L, 0L, 0L, "contact.creation", 0L, 0L, "", "");

        ContactCreatedWebhook[] webhookArray = new ContactCreatedWebhook[]{webhook};
        String requestBody = new ObjectMapper().writeValueAsString(webhookArray);

        String clientSecret = "secret";
        String signature = DigestUtils.sha256Hex(clientSecret + requestBody);

        when(oAuthProperties.getClientSecret()).thenReturn("secret");

        mockMvc.perform(get("/webhook/receive")
                        .header("X-HubSpot-Signature", signature)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

}