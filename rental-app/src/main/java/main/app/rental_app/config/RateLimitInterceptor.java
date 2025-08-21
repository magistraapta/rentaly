package main.app.rental_app.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Bucket globalRateLimitBucket;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Try to consume a token from the bucket
        ConsumptionProbe probe = globalRateLimitBucket.tryConsumeAndReturnRemaining(1);
        
        if (probe.isConsumed()) {
            // Request is allowed, add remaining tokens to response headers
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            response.addHeader("X-Rate-Limit-Reset", String.valueOf(probe.getNanosToWaitForReset() / 1_000_000_000));
            return true;
        } else {
            // Request is blocked due to rate limit
            long waitForRefill = probe.getNanosToWaitForReset() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Try again in " + waitForRefill + " seconds.");
            
            log.warn("Rate limit exceeded for request: {} {}", request.getMethod(), request.getRequestURI());
            return false;
        }
    }
}
