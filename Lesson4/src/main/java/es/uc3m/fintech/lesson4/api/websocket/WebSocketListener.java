package es.uc3m.fintech.lesson4.api.websocket;

import es.uc3m.fintech.lesson4.model.IPricesListener;
import es.uc3m.fintech.lesson4.model.PriceMessage;
import es.uc3m.fintech.lesson4.util.JacksonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Spring WebSocket Prices Listener.
 *
 * @author Mario Cao
 */
public class WebSocketListener implements IPricesListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketListener.class);

  private final WebSocketSession webSocketSession;
  private volatile boolean isActive = true;

  public WebSocketListener(WebSocketSession webSocketSession) {
    this.webSocketSession = webSocketSession;
  }

  /** Mark this listener as inactive (when connection is closed) */
  public void deactivate() {
    this.isActive = false;
  }

  @Override
  public void newPriceChange(PriceMessage priceMessage) {
    // Check if listener is still active before processing
    if (!isActive) {
      LOGGER.debug("WebSocket listener is inactive, skipping price update");
      return;
    }

    try {
      if (webSocketSession != null && webSocketSession.isOpen()) {
        // Convert price message to JSON
        String jsonMessage = JacksonMapper.getInstance().writeValueAsString(priceMessage);

        // Send the message through WebSocket
        webSocketSession.sendMessage(new TextMessage(jsonMessage));

        LOGGER.debug(
            "Sent price update to WebSocket session {}: {}", webSocketSession.getId(), jsonMessage);
      } else {
        LOGGER.debug(
            "WebSocket session {} is closed or null, skipping price update",
            webSocketSession != null ? webSocketSession.getId() : "null");
        // Deactivate this listener since the session is closed
        deactivate();
      }
    } catch (Exception e) {
      LOGGER.warn(
          "Error sending price update to WebSocket session {}: {}",
          webSocketSession != null ? webSocketSession.getId() : "null",
          e.getMessage());
      // Deactivate this listener on error
      deactivate();
    }
  }
}
