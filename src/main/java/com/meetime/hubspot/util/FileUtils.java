package com.meetime.hubspot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class FileUtils {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS);

    public static <T> T readFromFile(String path, Class<T> clazz) {
        try {
            File file = new File(path);
            if (!file.exists()) throw new RuntimeException("File does not exist: " + path);
            return mapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from file: " + path, e);
        }
    }

    public static void writeToFile(String path, Object object) {
        try {
            mapper.writeValue(new File(path), object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + path, e);
        }
    }

}