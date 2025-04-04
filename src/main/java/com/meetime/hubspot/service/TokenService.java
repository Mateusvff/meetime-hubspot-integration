package com.meetime.hubspot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meetime.hubspot.dto.auth.TokenInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(WRITE_DATES_AS_TIMESTAMPS);
    private static final String TOKEN_FILE = "tokens.json";

    public void writeToFile(TokenInformation token) {
        try {
            mapper.writeValue(new File(TOKEN_FILE), token);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save token to token file", e);
        }
    }

}