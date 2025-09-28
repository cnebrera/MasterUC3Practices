package es.uc3m.fintech.lesson4.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SSL Configuration for Lesson 5 - HTTPS Support.
 * This configuration provides helpful logging for SSL setup.
 * The actual SSL configuration is handled by Spring Boot through application.properties.
 *
 * @author Mario Cao
 */
// TODO 1: Add the Spring annotation that marks this class as a configuration class
// HINT: This annotation tells Spring that this class contains @Bean methods
public class SslConfig {

    /**
     * Log SSL configuration information on startup.
     */
    @Bean
    public SslConfigurationLogger sslConfigurationLogger() {
        // TODO 2: Create and return a new SslConfigurationLogger instance
        // HINT: This bean will automatically log SSL information when the application starts
    }

    /**
     * Helper class to log SSL configuration on application startup.
     */
    public static class SslConfigurationLogger {
        private static final Logger LOGGER = LoggerFactory.getLogger(SslConfigurationLogger.class);

        public SslConfigurationLogger() {
            logSslInfo();
        }

        private void logSslInfo() {
            LOGGER.info("🔒 SSL Configuration:");
            LOGGER.info("   - HTTPS enabled: true");
            LOGGER.info("   - Server port: 8443");
            LOGGER.info("   - Certificate: Self-signed (development only)");
            LOGGER.info("   - WebSocket: WSS (secure)");
            LOGGER.info("   - Access URL: https://localhost:8443");
            LOGGER.warn("⚠️  Using self-signed certificate - browser will show security warning");
            LOGGER.warn("⚠️  Click 'Advanced' → 'Proceed to localhost' to continue");
        }
    }
}
