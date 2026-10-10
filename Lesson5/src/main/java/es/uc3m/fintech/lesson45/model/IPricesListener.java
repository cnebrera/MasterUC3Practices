package es.uc3m.fintech.lesson45.model;

/**
 * Interface Prices Listener.
 *
 * @author Mario Cao
 */
public interface IPricesListener {
  /**
   * Add a new price change to the listener
   *
   * @param priceMessage with the price message
   */
  void newPriceChange(PriceMessage priceMessage);
}
