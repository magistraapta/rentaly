package main.app.rental_app.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    @Value("${app.ratelimit.global.limit:100}")
    private int globalLimit;

    @Value("${app.ratelimit.global.duration:10000}")
    private int globalDuration;

    @Value("${app.ratelimit.auth.limit:10}")
    private int authLimit;

    @Value("${app.ratelimit.auth.duration:60000}")
    private int authDuration;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket getBucket(String key) {
        return buckets.computeIfAbsent(key, this::createBucket);
    }

    public Bucket getAuthBucket() {
        return getBucket("auth");
    }

    public Bucket getGlobalBucket() {
        return getBucket("global");
    }

    private Bucket createBucket(String key) {
        if ("auth".equals(key)) {
            // Stricter rate limit for authentication endpoints
            Bandwidth limit = Bandwidth.classic(authLimit, Refill.greedy(authLimit, Duration.ofMillis(authDuration)));
            return Bucket.builder().addLimit(limit).build();
        } else {
            // Default global rate limit
            Bandwidth limit = Bandwidth.classic(globalLimit, Refill.greedy(globalLimit, Duration.ofMillis(globalDuration)));
            return Bucket.builder().addLimit(limit).build();
        }
    }

    public void resetBucket(String key) {
        buckets.remove(key);
    }
}
