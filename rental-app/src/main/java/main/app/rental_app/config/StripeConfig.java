package main.app.rental_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class StripeConfig {
    
    @Value("${stripe.secret.key:}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            log.warn("Stripe secret key is not configured. Stripe functionality will not work.");
            return;
        }
        Stripe.apiKey = stripeSecretKey;
        log.info("Stripe API key configured successfully");
    }
}
