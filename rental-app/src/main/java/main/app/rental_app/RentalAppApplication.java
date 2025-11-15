package main.app.rental_app;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class RentalAppApplication {

	public static void main(String[] args) {
		// Load .env file before Spring Boot starts
		try {
			Dotenv dotenv = Dotenv.configure()
					.directory(".") // Look for .env in current directory
					.ignoreIfMissing() // Don't fail if .env file doesn't exist
					.load();
			
			int loadedCount = 0;
			// Set system properties from .env file
			for (var entry : dotenv.entries()) {
				String key = entry.getKey();
				String value = entry.getValue();
				// Only set if not already set as system property or environment variable
				if (System.getProperty(key) == null && System.getenv(key) == null) {
					System.setProperty(key, value);
					loadedCount++;
					// Log sensitive keys without exposing values
					if (key.contains("SECRET") || key.contains("KEY") || key.contains("PASSWORD")) {
						log.debug("Loaded sensitive property from .env: {} (value hidden)", key);
					} else {
						log.debug("Loaded property from .env: {} = {}", key, value);
					}
				}
			}
			
			log.info("Successfully loaded {} properties from .env file", loadedCount);
		} catch (Exception e) {
			log.warn("Could not load .env file: {}. Using system environment variables instead.", e.getMessage());
		}
		
		SpringApplication.run(RentalAppApplication.class, args);
	}

}
