package es.uc3m.fintech.lesson4.api.longpolling;

import es.uc3m.fintech.lesson4.model.IPricesListener;
import es.uc3m.fintech.lesson4.model.PriceMessage;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring Long Polling Prices Listener.
 *
 * @author Mario Cao
 */
public class LongPollingListener implements IPricesListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(LongPollingListener.class);

  private final CompletableFuture<PriceMessage> priceFuture;
  private final String requestId;

  public LongPollingListener(CompletableFuture<PriceMessage> priceFuture, String requestId) {
    this.priceFuture = priceFuture;
    this.requestId = requestId;
  }

  @Override
  public void newPriceChange(PriceMessage priceMessage) {
    // Skip if already completed
    if (priceFuture.isDone()) {
      return;
    }

    try {
      // Complete the future with the price message
      boolean completed = priceFuture.complete(priceMessage);
      if (completed) {
        LOGGER.debug(
            "Long polling request {} completed with price update: {}", requestId, priceMessage);
      }
    } catch (Exception e) {
      LOGGER.error("Error completing long polling request {}", requestId, e);
      if (!priceFuture.isDone()) {
        priceFuture.completeExceptionally(e);
      }
    }
  }
}
