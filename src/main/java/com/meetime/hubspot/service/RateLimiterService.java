package com.meetime.hubspot.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final Bucket bucket;

    public RateLimiterService() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(110)
                .refillIntervally(110, Duration.ofSeconds(10))
                .build();

        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }

}