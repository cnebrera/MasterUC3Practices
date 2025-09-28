package es.uc3m.fintech.lesson4.model;

import java.util.Objects;

/**
 * Represents a new price message.
 *
 * @author Mario Cao
 */
public final class PriceMessage {
  /** Attribute - Market. */
  private final String market;

  /** Attribute - Product. */
  private final String product;

  /** Attribute - Instrument name. */
  private final String instrumentName;

  /** Attribute - Level. */
  private final int level;

  /** Attribute - Price. */
  private final double price;

  /** Attribute - Volume. */
  private final int volume;

  /** Attribute - Price was sent (for tracking purposes). */
  private volatile boolean priceWasSent;

  /**
   * Constructor.
   *
   * @param market with the market
   * @param product with the product
   * @param instrumentName with the instrument
   * @param level with the level
   * @param price with the price
   * @param volume with the volume
   */
  public PriceMessage(
      final String market,
      final String product,
      final String instrumentName,
      final int level,
      final double price,
      final int volume) {
    this.market = market;
    this.product = product;
    this.instrumentName = instrumentName;
    this.level = level;
    this.price = price;
    this.volume = volume;
    this.priceWasSent = false;
  }

  /**
   * @return the market
   */
  public String getMarket() {
    return this.market;
  }

  /**
   * @return the product
   */
  public String getProduct() {
    return this.product;
  }

  /**
   * @return the instrument
   */
  public String getInstrumentName() {
    return this.instrumentName;
  }

  /**
   * @return the level
   */
  public int getLevel() {
    return this.level;
  }

  /**
   * @return the price
   */
  public double getPrice() {
    return this.price;
  }

  /**
   * @return the volume
   */
  public int getVolume() {
    return this.volume;
  }

  @Override
  public String toString() {
    return String.format(
        "PriceMessage{market='%s', product='%s', instrumentName='%s', level=%d, price=%.2f, volume=%d}",
        market, product, instrumentName, level, price, volume);
  }

  /**
   * @return true if the price was sent
   */
  public boolean wasSent() {
    return this.priceWasSent;
  }

  /** Mark the price as sent. */
  public void markAsSent() {
    this.priceWasSent = true;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceMessage that = (PriceMessage) o;
    return level == that.level
        && Double.compare(that.price, price) == 0
        && volume == that.volume
        && Objects.equals(market, that.market)
        && Objects.equals(product, that.product)
        && Objects.equals(instrumentName, that.instrumentName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(market, product, instrumentName, level, price, volume);
  }
}
