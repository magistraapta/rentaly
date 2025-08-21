package main.app.rental_app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${app.ratelimit.global.limit:100}")
    private int globalLimit;

    @Value("${app.ratelimit.global.duration:10000}")
    private int globalDuration;

    @Value("${app.ratelimit.auth.limit:10}")
    private int authLimit;

    @Value("${app.ratelimit.auth.duration:60000}")
    private int authDuration;

    public boolean isAllowed(String key, String type) {
        String redisKey = "rate_limit:" + type + ":" + key;
        
        int limit = "auth".equals(type) ? authLimit : globalLimit;
        int duration = "auth".equals(type) ? authDuration : globalDuration;
        
        try {
            // Get current count from Redis
            String currentCountStr = (String) redisTemplate.opsForValue().get(redisKey);
            int currentCount = currentCountStr != null ? Integer.parseInt(currentCountStr) : 0;
            
            if (currentCount < limit) {
                // Increment counter
                redisTemplate.opsForValue().increment(redisKey);
                
                // Set expiration if this is the first request
                if (currentCount == 0) {
                    redisTemplate.expire(redisKey, Duration.ofMillis(duration));
                }
                
                log.debug("Rate limit check passed for key: {}, type: {}, current: {}/{}", 
                         key, type, currentCount + 1, limit);
                return true;
            } else {
                log.warn("Rate limit exceeded for key: {}, type: {}, limit: {}", key, type, limit);
                return false;
            }
        } catch (Exception e) {
            log.error("Error checking rate limit for key: {}, type: {}", key, type, e);
            // In case of Redis error, allow the request (fail-open)
            return true;
        }
    }

    public boolean isAllowed(String key) {
        return isAllowed(key, "global");
    }

    public boolean isAuthAllowed(String key) {
        return isAllowed(key, "auth");
    }

    public void resetLimit(String key, String type) {
        String redisKey = "rate_limit:" + type + ":" + key;
        redisTemplate.delete(redisKey);
        log.debug("Reset rate limit for key: {}, type: {}", key, type);
    }

    public long getRemainingTokens(String key, String type) {
        String redisKey = "rate_limit:" + type + ":" + key;
        
        int limit = "auth".equals(type) ? authLimit : globalLimit;
        
        try {
            String currentCountStr = (String) redisTemplate.opsForValue().get(redisKey);
            int currentCount = currentCountStr != null ? Integer.parseInt(currentCountStr) : 0;
            return Math.max(0, limit - currentCount);
        } catch (Exception e) {
            log.error("Error getting remaining tokens for key: {}, type: {}", key, type, e);
            return limit;
        }
    }

    public long getTimeToReset(String key, String type) {
        String redisKey = "rate_limit:" + type + ":" + key;
        
        try {
            Long ttl = redisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
            return ttl != null ? ttl : 0;
        } catch (Exception e) {
            log.error("Error getting TTL for key: {}, type: {}", key, type, e);
            return 0;
        }
    }
}
