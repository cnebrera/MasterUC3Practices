package es.uc3m.fintech.lesson4.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * Simple Jackson ObjectMapper configuration for messaging.
 *
 * @author Mario Cao
 */
@Component
public final class JacksonMapper {
  /** Thread-safe static instance. */
  private static final ObjectMapper INSTANCE = createMapper();

  /**
   * @return the configured ObjectMapper instance.
   */
  public static ObjectMapper getInstance() {
    return INSTANCE;
  }

  /**
   * Private constructor.
   */
  private JacksonMapper() {
    // Empty constructor
  }

  /**
   * Creates a configured ObjectMapper for messaging.
   *
   * @return configured ObjectMapper.
   */
  private static ObjectMapper createMapper() {
    ObjectMapper mapper = new ObjectMapper();

    // Exclude null values from JSON.
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    // Don't fail on unknown properties (useful for API evolution).
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return mapper;
  }
}
