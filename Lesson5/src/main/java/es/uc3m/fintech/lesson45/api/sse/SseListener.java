package es.uc3m.fintech.lesson45.api.sse;

import es.uc3m.fintech.lesson45.model.IPricesListener;
import es.uc3m.fintech.lesson45.model.PriceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Server-Sent Events Prices Listener.
 *
 * <p>Each SSE connection has its own listener, which pushes every new price to the browser as an
 * SSE event. The event id is the sequence number of the message.
 *
 * @author Mario Cao
 */
public class SseListener implements IPricesListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(SseListener.class);

  private final SseEmitter emitter;
  private volatile boolean isActive = true;

  public SseListener(SseEmitter emitter) {
    this.emitter = emitter;
  }

  /** Mark this listener as inactive (when the connection is closed). */
  public void deactivate() {
    this.isActive = false;
  }

  @Override
  public void newPriceChange(PriceMessage priceMessage) {
    if (!isActive) {
      return;
    }
    try {
      String jsonMessage = priceMessage.toJson();
      emitter.send(SseEmitter.event().id(String.valueOf(priceMessage.getSeq())).data(jsonMessage));
    } catch (Exception e) {
      LOGGER.debug("SSE connection closed: {}", e.getMessage());
      deactivate();
      emitter.completeWithError(e);
    }
  }
}
