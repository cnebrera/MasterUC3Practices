package es.uc3m.fintech.lesson4.api.longpolling;

import es.uc3m.fintech.lesson4.model.PriceMessage;
import es.uc3m.fintech.lesson4.service.PricesPublisher;
import es.uc3m.fintech.lesson4.util.Constants;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot Long Polling Controller.
 * This implements long polling - waits for the next price update.
 *
 * @author Mario Cao
 */
@RestController
@RequestMapping("/prices")
public class LongPollingController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LongPollingController.class);

  @Autowired private PricesPublisher pricesPublisher;

  /** Long Polling endpoint This waits for the next price update and returns it */
  // TODO 1: Create the HTTP endpoint mapping for long polling
  // HINT: Use @GetMapping to create a REST endpoint that clients can call
  // The path should be "/long-polling" to match the client requests
  public CompletableFuture<ResponseEntity<PriceMessage>> longPolling() {
    LOGGER.debug("Long polling request received");

    // Create a unique request ID
    String requestId = "req_" + System.currentTimeMillis() + "_" + System.nanoTime();

    // Create a CompletableFuture that will complete when a price update arrives
    CompletableFuture<PriceMessage> priceFuture = new CompletableFuture<>();

    // TODO 2: Create a listener that will be notified when a new price arrives
    // HINT: The listener will complete the future when it receives a price update
    // This bridges the event-driven price system with the HTTP request-response model

    // TODO 3: Register the listener to receive price updates
    // HINT: Add the listener to the publisher so it gets notified of new prices
    // This connects the HTTP request to the real-time price system

    return priceFuture
        // TODO 4: Implement timeout handling for long polling
        // HINT: Long polling must have a timeout to prevent connections from hanging forever
        // Use .orTimeout() with Constants.LONG_POLLING_TIMEOUT and TimeUnit.MILLISECONDS
        .thenApply(
            priceMessage -> {
              LOGGER.debug("Long polling request {} completed with price update", requestId);
              pricesPublisher.removePricesListener(listener);
              return ResponseEntity.ok(priceMessage);
            })
        .exceptionally(
            throwable -> {
              LOGGER.debug("Long polling request {} timed out or failed", requestId);
              pricesPublisher.removePricesListener(listener);
              if (throwable instanceof java.util.concurrent.TimeoutException) {
                return ResponseEntity.noContent().build();
              } else {
                LOGGER.error("Error in long polling request {}", requestId, throwable);
                return ResponseEntity.internalServerError().build();
              }
            });
  }
}
