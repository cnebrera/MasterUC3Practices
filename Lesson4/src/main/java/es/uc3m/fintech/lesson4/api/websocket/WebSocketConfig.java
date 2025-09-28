package es.uc3m.fintech.lesson4.api.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket Configuration for Spring Boot.
 *
 * @author Mario Cao
 */
// TODO 1: Enable WebSocket support in Spring Boot
// HINT: Use @Configuration and @EnableWebSocket to enable WebSocket functionality
// WebSockets need special configuration unlike REST controllers
public class WebSocketConfig implements WebSocketConfigurer {

  private final WebSocketController webSocketHandler;

  // This Injects the WebSocket handler connects the configuration to the actual WebSocket logic
  public WebSocketConfig(WebSocketController webSocketHandler) {
    this.webSocketHandler = webSocketHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry
      // TODO 2: Register WebSocket handler for the endpoint
      // HINT: Use registry.addHandler() to map the WebSocket endpoint "/prices/websocket"
      // This is where clients will connect to establish WebSocket connections
      .setAllowedOrigins("*"); // Allow all origins for development purposes
  }
}
