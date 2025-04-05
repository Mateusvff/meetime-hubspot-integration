package com.meetime.hubspot.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public Bucket configBucketRateLimiter() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(110)
                .refillIntervally(110, Duration.ofSeconds(10))
                .build();

        return Bucket.builder().addLimit(limit).build();
    }

}