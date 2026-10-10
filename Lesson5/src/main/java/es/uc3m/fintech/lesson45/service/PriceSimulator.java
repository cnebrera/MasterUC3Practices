package es.uc3m.fintech.lesson45.service;

import es.uc3m.fintech.lesson45.model.IPricesListener;
import es.uc3m.fintech.lesson45.model.PriceMessage;
import es.uc3m.fintech.lesson45.util.Constants;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Price simulator.
 *
 * <p>Generates random prices in a dedicated thread and notifies all the registered listeners. The
 * random generator is initialised with the student's NIA (property {@code practice.nia}), so the
 * sequence of generated prices is the same in every execution for a given NIA.
 *
 * @author Mario Cao
 */
@Component
public class PriceSimulator {

  private static final Logger LOGGER = LoggerFactory.getLogger(PriceSimulator.class);

  /** Student NIA, used as seed of the random generator. */
  @Value("${practice.nia:0}")
  private long nia;

  private final List<IPricesListener> pricesListenerList;

  private Random randomInstance;

  private long seq;

  private volatile boolean isRunning;

  public PriceSimulator() {
    this.pricesListenerList = new CopyOnWriteArrayList<>();
    this.isRunning = false; // Will be started by PricesPublisher
  }

  public void start(long sleepTime) {
    if (this.nia <= 0) {
      LOGGER.warn(
          "\n\n  ⚠️  practice.nia is not configured in application.properties."
              + "\n      Set your NIA before taking any measurement.\n");
    }
    this.randomInstance = new Random(this.nia);
    this.seq = 0;
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

  public void addPricesListener(final IPricesListener pricesListener) {
    this.pricesListenerList.add(pricesListener);
    LOGGER.debug("Added price listener. Total listeners: {}", pricesListenerList.size());
  }

  public void removePricesListener(final IPricesListener pricesListener) {
    this.pricesListenerList.remove(pricesListener);
    LOGGER.debug("Removed price listener. Total listeners: {}", pricesListenerList.size());
  }

  public void stop() {
    this.isRunning = false;
    LOGGER.info("Price simulator stopped");
  }

  private void newPriceMessage() {
    // Create a random price level change
    final int level = this.randomInstance.nextInt(Constants.MAX_PRICE_LEVELS);
    final int newVolume = this.randomInstance.nextInt(Constants.MAX_VOLUME);
    final double newPrice = this.randomInstance.nextDouble() * Constants.MAX_PRICE_PERCENTAGE;
    this.seq++;

    // New instance of PriceMessage
    final PriceMessage priceMessage =
        new PriceMessage(
            this.seq,
            Constants.PREFIX_INSTRUMENT_MARKET,
            Constants.PREFIX_INSTRUMENT_PRODUCT,
            Constants.PREFIX_INSTRUMENT_NAME,
            level,
            newPrice,
            newVolume);

    // Execution proof: log some specific messages of the sequence
    for (final long proofSeq : Constants.PROOF_SEQUENCES) {
      if (this.seq == proofSeq) {
        LOGGER.info(
            String.format(
                Locale.ROOT,
                "[PRUEBA] nia=%d seq=%d level=%d price=%.6f volume=%d",
                this.nia,
                this.seq,
                level,
                newPrice,
                newVolume));
      }
    }

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
