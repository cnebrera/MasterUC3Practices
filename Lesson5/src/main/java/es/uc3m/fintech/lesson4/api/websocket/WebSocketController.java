package es.uc3m.fintech.lesson4.api.websocket;

import es.uc3m.fintech.lesson4.model.IPricesListener;
import es.uc3m.fintech.lesson4.service.PricesPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Spring Boot WebSocket Handler.
 *
 * @author Mario Cao
 */
@Component
public class WebSocketController extends TextWebSocketHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

  @Autowired private PricesPublisher pricesPublisher;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    LOGGER.info("WebSocket connection established: {}", session.getId());

    // TODO 1: Create a WebSocket listener for this specific connection
    // HINT: Each WebSocket connection needs its own listener to receive price updates
    // This listener will be called every time a new price arrives and must push it to the browser

    // TODO 2: Register the listener to receive real-time price updates
    // HINT: Add the listener to the publisher so it gets notified of new prices
    // This enables real-time push notifications to the client

    // Store the listener in the session attributes for cleanup
    session.getAttributes().put("pricesListener", pricesListener);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    LOGGER.info("WebSocket connection closed: {} with status: {}", session.getId(), status);

    // Remove the listener from the publisher
    IPricesListener pricesListener =
        (IPricesListener) session.getAttributes().get("pricesListener");
    if (pricesListener != null) {
      // Deactivate the listener first
      if (pricesListener instanceof WebSocketListener) {
        ((WebSocketListener) pricesListener).deactivate();
      }
      pricesPublisher.removePricesListener(pricesListener);
      // Clear the listener from session attributes
      session.getAttributes().remove("pricesListener");
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    LOGGER.error("WebSocket transport error for session: {}", session.getId(), exception);

    // Remove the listener from the publisher on error
    IPricesListener pricesListener =
        (IPricesListener) session.getAttributes().get("pricesListener");
    if (pricesListener != null) {
      // Deactivate the listener first
      if (pricesListener instanceof WebSocketListener) {
        ((WebSocketListener) pricesListener).deactivate();
      }
      pricesPublisher.removePricesListener(pricesListener);
      // Clear the listener from session attributes
      session.getAttributes().remove("pricesListener");
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    // Handle incoming messages from the client
    LOGGER.debug("Received message from session {}: {}", session.getId(), message.getPayload());
  }
}
