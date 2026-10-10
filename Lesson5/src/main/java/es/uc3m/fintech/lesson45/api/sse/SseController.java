package es.uc3m.fintech.lesson45.api.sse;

import es.uc3m.fintech.lesson45.service.PricesPublisher;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Server-Sent Events (SSE) controller.
 *
 * <p>The client opens a single HTTP request and the server keeps the response open, sending one
 * event per price update (Content-Type: text/event-stream).
 *
 * @author Mario Cao
 */
@RestController
@RequestMapping("/prices")
public class SseController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SseController.class);

  @Autowired private PricesPublisher pricesPublisher;

  /** Emitters of the SSE connections currently open, so they can be closed on shutdown. */
  private final Set<SseEmitter> activeEmitters = ConcurrentHashMap.newKeySet();

  // TODO 1: Create the HTTP endpoint mapping for Server-Sent Events
  // HINT: Use @GetMapping with the path "/sse" and produces = MediaType.TEXT_EVENT_STREAM_VALUE
  // The "text/event-stream" content type is what tells the browser that this is an SSE stream

  public SseEmitter sse() {
    LOGGER.info("SSE connection opened");

    // The emitter keeps the HTTP response open. 0 = no timeout
    SseEmitter emitter = new SseEmitter(0L);

    // TODO 2: Create a listener for this specific SSE connection
    // HINT: Create a new SseListener passing the emitter, and store it in a variable called "listener"


    // TODO 3: Register the listener to receive real-time price updates
    // HINT: Do the same as in WebSocketController

    activeEmitters.add(emitter);

    // Clean up when the browser closes the connection
    Runnable cleanup =
        () -> {
          listener.deactivate();
          pricesPublisher.removePricesListener(listener);
          activeEmitters.remove(emitter);
          LOGGER.info("SSE connection closed");
        };
    emitter.onCompletion(cleanup);
    emitter.onTimeout(cleanup);
    emitter.onError(error -> cleanup.run());

    return emitter;
  }

  /**
   * Close every open SSE stream when the application starts shutting down.
   *
   * <p>SSE responses never finish by themselves, so without this the graceful shutdown of the web
   * server (enabled by default in Spring Boot) would wait its whole timeout for them.
   */
  @EventListener(ContextClosedEvent.class)
  public void closeActiveEmitters() {
    LOGGER.info("Closing {} SSE connection(s) on shutdown", activeEmitters.size());
    for (SseEmitter emitter : activeEmitters) {
      try {
        emitter.complete();
      } catch (Exception e) {
        LOGGER.debug("Could not complete SSE emitter on shutdown: {}", e.getMessage());
      }
    }
  }
}
