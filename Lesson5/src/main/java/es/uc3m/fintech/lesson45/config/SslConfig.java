package es.uc3m.fintech.lesson45.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SSL information logger.
 *
 * <p>Only active when HTTPS is enabled ({@code server.ssl.enabled=true}). The actual SSL
 * configuration is done by Spring Boot through application.properties.
 *
 * @author Mario Cao
 */
@Configuration
@ConditionalOnProperty(name = "server.ssl.enabled", havingValue = "true")
public class SslConfig {

  /**
   * Log SSL configuration information on startup.
   *
   * @param port server port
   * @return the logger bean
   */
  @Bean
  public SslConfigurationLogger sslConfigurationLogger(@Value("${server.port:8443}") int port) {
    return new SslConfigurationLogger(port);
  }

  /** Helper class to log SSL configuration on application startup. */
  public static class SslConfigurationLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(SslConfigurationLogger.class);

    public SslConfigurationLogger(int port) {
      LOGGER.info("🔒 HTTPS enabled - access URL: https://localhost:{}", port);
      LOGGER.warn("⚠️  Self-signed certificate: the browser will show a security warning");
    }
  }
}
