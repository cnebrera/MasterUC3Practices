package es.uc3m.fintech.lesson4.service;

import es.uc3m.fintech.lesson4.model.IPricesListener;
import es.uc3m.fintech.lesson4.model.PriceMessage;
import es.uc3m.fintech.lesson4.util.Constants;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Price simulator.
 *
 * @author Mario Cao
 */
@Component
public class PriceSimulator {

  private static final Logger LOGGER = LoggerFactory.getLogger(PriceSimulator.class);

  /** Attribute - Random instance */
  private final Random randomInstance;

  /** Event listener (websocket or long-polling) - Thread-safe list */
  private final List<IPricesListener> pricesListenerList;

  /** Is running the simulator? */
  private volatile boolean isRunning;

  /** Default constructor for Spring */
  public PriceSimulator() {
    this.pricesListenerList = new CopyOnWriteArrayList<>();
    this.randomInstance = new Random();
    this.isRunning = false; // Will be started by PricesPublisher
  }

  /**
   * Start the simulator with specified sleep time.
   *
   * @param sleepTime sleep time in milliseconds
   */
  public void start(long sleepTime) {
    this.isRunning = true;
    new Thread(
            () -> {
              while (this.isRunning) {
                // Generate a new price message
                this.newPriceMessage();

                // Check if sleep is necessary
                if (sleepTime > 0) {
                  try {
                    Thread.sleep(sleepTime);
                  } catch (InterruptedException e) {
                    LOGGER.warn("Price simulator interrupted", e);
                    Thread.currentThread().interrupt();
                    break;
                  }
                }
              }
            },
            "PriceSimulator")
        .start();
  }

  /**
   * @param pricesListener with a new prices listener.
   */
  public void addPricesListener(final IPricesListener pricesListener) {
    this.pricesListenerList.add(pricesListener);
    LOGGER.debug("Added price listener. Total listeners: {}", pricesListenerList.size());
  }

  /**
   * @param pricesListener with a prices listener to be removed.
   */
  public void removePricesListener(final IPricesListener pricesListener) {
    this.pricesListenerList.remove(pricesListener);
    LOGGER.debug("Removed price listener. Total listeners: {}", pricesListenerList.size());
  }

  /** Stop the simulator. */
  public void stop() {
    this.isRunning = false;
    LOGGER.info("Price simulator stopped");
  }

  /** Generate a new price message. */
  private void newPriceMessage() {
    // Create a random price level change
    final int level = this.randomInstance.nextInt(Constants.MAX_PRICE_LEVELS);
    final int newVolume = this.randomInstance.nextInt(Constants.MAX_VOLUME);
    final double newPrice = this.randomInstance.nextDouble() * Constants.MAX_PRICE_PERCENTAGE;

    // New instance of PriceMessage
    final PriceMessage priceMessage =
        new PriceMessage(
            Constants.PREFIX_INSTRUMENT_MARKET,
            Constants.PREFIX_INSTRUMENT_PRODUCT,
            Constants.PREFIX_INSTRUMENT_NAME,
            level,
            newPrice,
            newVolume);

    // Notify all listeners - CopyOnWriteArrayList is thread-safe for iteration
    for (final IPricesListener pricesListener : pricesListenerList) {
      try {
        pricesListener.newPriceChange(priceMessage);
      } catch (Exception e) {
        LOGGER.warn("Error notifying listener: {}", e.getMessage(), e);
      }
    }
  }
}
