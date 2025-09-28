package es.uc3m.fintech.lesson4.service;

import es.uc3m.fintech.lesson4.model.IPricesListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Prices publisher.
 *
 * @author Mario Cao
 */
@Component
public final class PricesPublisher {

  /** Simulator to generate random prices */
  @Autowired private PriceSimulator simulator;

  /** Default constructor for Spring */
  public PricesPublisher() {
    // Empty constructor
  }

  /**
   * Start the publisher.
   *
   * @param sleepTime with the sleep time
   */
  public void start(final int sleepTime) {
    // Start the message generation
    this.simulator.start(sleepTime);
  }

  /**
   * @param pricesListener with a new prices listener.
   */
  public void addPricesListener(final IPricesListener pricesListener) {
    this.simulator.addPricesListener(pricesListener);
  }

  /**
   * @param pricesListener with a new prices listener.
   */
  public void removePricesListener(final IPricesListener pricesListener) {
    this.simulator.removePricesListener(pricesListener);
  }

  /** Stop the publisher. */
  public void stop() {
    this.simulator.stop();
  }
}
