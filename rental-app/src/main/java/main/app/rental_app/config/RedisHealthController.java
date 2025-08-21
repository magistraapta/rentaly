package main.app.rental_app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/redis")
@RequiredArgsConstructor
@Slf4j
public class RedisHealthController {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkRedisHealth() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test Redis connection by setting and getting a test key
            String testKey = "health_check_" + System.currentTimeMillis();
            redisTemplate.opsForValue().set(testKey, "test_value");
            Object retrievedValue = redisTemplate.opsForValue().get(testKey);
            redisTemplate.delete(testKey);
            
            if ("test_value".equals(retrievedValue)) {
                response.put("status", "UP");
                response.put("message", "Redis is working correctly");
                response.put("timestamp", System.currentTimeMillis());
                log.info("Redis health check passed");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "DOWN");
                response.put("message", "Redis read/write test failed");
                log.error("Redis health check failed: read/write test failed");
                return ResponseEntity.status(503).body(response);
            }
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("message", "Redis connection failed: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            log.error("Redis health check failed", e);
            return ResponseEntity.status(503).body(response);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getRedisInfo() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Get Redis server info
            Object info = redisTemplate.getConnectionFactory().getConnection().info();
            response.put("status", "UP");
            response.put("info", info);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("message", "Failed to get Redis info: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            log.error("Failed to get Redis info", e);
            return ResponseEntity.status(503).body(response);
        }
    }
}
