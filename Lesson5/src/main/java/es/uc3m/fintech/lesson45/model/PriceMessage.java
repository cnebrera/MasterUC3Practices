package es.uc3m.fintech.lesson45.model;

import java.util.Objects;

/**
 * Represents a new price message.
 *
 * <p>Each message carries a sequence number ({@code seq}) assigned by the simulator. Consecutive
 * messages have consecutive sequence numbers, so a client can detect lost messages by looking for
 * gaps in the sequence.
 *
 * @author Mario Cao
 */
public final class PriceMessage {
  /** Attribute - Sequence number (1, 2, 3...). */
  private final long seq;

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

  /**
   * Constructor.
   *
   * @param seq with the sequence number
   * @param market with the market
   * @param product with the product
   * @param instrumentName with the instrument
   * @param level with the level
   * @param price with the price
   * @param volume with the volume
   */
  public PriceMessage(
      final long seq,
      final String market,
      final String product,
      final String instrumentName,
      final int level,
      final double price,
      final int volume) {
    this.seq = seq;
    this.market = market;
    this.product = product;
    this.instrumentName = instrumentName;
    this.level = level;
    this.price = price;
    this.volume = volume;
  }

  /**
   * @return the sequence number
   */
  public long getSeq() {
    return this.seq;
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

  /**
   * Serialises the message as JSON (same format that Spring uses for the REST responses).
   *
   * @return the message in JSON format
   */
  public String toJson() {
    return "{\"seq\":" + seq
        + ",\"market\":" + quote(market)
        + ",\"product\":" + quote(product)
        + ",\"instrumentName\":" + quote(instrumentName)
        + ",\"level\":" + level
        + ",\"price\":" + price
        + ",\"volume\":" + volume
        + "}";
  }

  private static String quote(final String value) {
    return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
  }

  @Override
  public String toString() {
    return String.format(
        "PriceMessage{seq=%d, market='%s', product='%s', instrumentName='%s', level=%d, price=%.2f, volume=%d}",
        seq, market, product, instrumentName, level, price, volume);
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
    return seq == that.seq
        && level == that.level
        && Double.compare(that.price, price) == 0
        && volume == that.volume
        && Objects.equals(market, that.market)
        && Objects.equals(product, that.product)
        && Objects.equals(instrumentName, that.instrumentName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq, market, product, instrumentName, level, price, volume);
  }
}
