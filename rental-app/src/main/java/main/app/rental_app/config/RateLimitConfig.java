package main.app.rental_app.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Value("${app.ratelimit.global.limit:100}")
    private int globalLimit;

    @Value("${app.ratelimit.global.duration:10000}")
    private int globalDuration;

    @Bean
    public Bucket globalRateLimitBucket() {
        // Convert milliseconds to seconds for Bucket4j
        Duration duration = Duration.ofMillis(globalDuration);
        
        // Create a bandwidth that allows 'globalLimit' tokens per 'duration'
        Bandwidth limit = Bandwidth.classic(globalLimit, Refill.greedy(globalLimit, duration));
        
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
