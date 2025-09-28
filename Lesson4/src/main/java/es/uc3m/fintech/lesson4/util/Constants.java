package es.uc3m.fintech.lesson4.util;

/** Constants class. */
public final class Constants {
  /** Instrument - Prefix - Market. */
  public static final String PREFIX_INSTRUMENT_MARKET = "market_1";

  /** Instrument - Prefix - Product. */
  public static final String PREFIX_INSTRUMENT_PRODUCT = "product_1";

  /** Instrument - Prefix - Name. */
  public static final String PREFIX_INSTRUMENT_NAME = "instrument_1";

  /** Launcher - Default Sleep time. */
  public static final int DEFAULT_SLEEP_TIME = 0;

  /** Maximum number of price levels for instruments. */
  public static final int MAX_PRICE_LEVELS = 3;

  /** Maximum volume for price simulation. */
  public static final int MAX_VOLUME = 1000;

  /** Maximum price percentage (100%). */
  public static final double MAX_PRICE_PERCENTAGE = 100.0d;

  /** Long polling timeout in milliseconds (30 seconds). */
  public static final long LONG_POLLING_TIMEOUT = 30000;

  /** Minimum width for startup message box. */
  public static final int APP_MSG_MIN_WIDTH = 50;

  /** Border padding for startup message box. */
  public static final int APP_MSG_BORDER_PADDING = 2;

  /** Private constructor. */
  private Constants() {
    // Empty constructor
  }
}
