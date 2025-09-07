package es.uc3m.fintech.lesson2.message;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Represents a flexible, variable-size market data message supporting dynamic
 * instrument
 * representation and extensible metadata for diverse trading scenarios.
 * 
 * @author Mario Cao
 * @version 1.0
 */
public class MarketMessage {
    public static final int MSG_BINARY_SIZE_NO_METADATA = 35;

    public static final int DEFAULT_MAX_METADATA_SIZE = 100;

    /** Random number generator */
    private static final Random RND = new Random();

    /** Price with 4 decimal places precision */
    private final double price;

    /** Volume in lots */
    private final int volume;

    /** Instrument symbol (e.g., "AAPL", "MSFT", "EURUSD") */
    private final String instrument;

    /** Timestamp in nanoseconds for latency measurement */
    private final long timestamp;

    /** Variable-length metadata field (binary blobs, FIX extensions, etc.) */
    private final byte[] metadata;

    public MarketMessage(
            String instrument, double price, int volume, long timestamp, byte[] metadata) {
        this.instrument = instrument;
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    /** Convert the message into binary */
    public ByteBuffer toBinary() {
        // Convert the message String contents to binary
        final byte[] instrumentInBinary = instrument.getBytes();

        // Calculate msg size: String length + 4 (volume) + 8 (price) + 8 (timestamp) +
        // 4 (string length) + 4 (metadata length) + metadata bytes
        final int msgSize = instrumentInBinary.length + 28 + (metadata != null ? metadata.length : 0);

        // Write the contents (NO size header - that's handled by the protocol layer)
        final ByteBuffer resultBuffer = ByteBuffer.allocate(msgSize); // No +4 for size header
        resultBuffer.putInt(this.volume);
        resultBuffer.putDouble(this.price);
        resultBuffer.putLong(this.timestamp);
        resultBuffer.putInt(instrumentInBinary.length);
        resultBuffer.put(instrumentInBinary);
        resultBuffer.putInt(metadata != null ? metadata.length : 0);
        if (metadata != null && metadata.length > 0) {
            resultBuffer.put(metadata);
        }

        resultBuffer.flip();
        return resultBuffer;
    }

    /**
     * Read the msg from binary given the message length and the buffer containing
     * the message
     *
     * @param data the byte array containing the message
     * @return the read message
     */
    public static MarketMessage fromBinary(final byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        // Read the fixed-size header fields
        int volume = buffer.getInt();
        double price = buffer.getDouble();
        long timestamp = buffer.getLong();
        int stringLength = buffer.getInt();

        // Read the variable-length instrument string
        byte[] instrumentBinary = new byte[stringLength];
        buffer.get(instrumentBinary);
        String instrument = new String(instrumentBinary);

        // Read the variable-length metadata
        int metadataLength = buffer.getInt();
        byte[] metadata = null;
        if (metadataLength > 0) {
            metadata = new byte[metadataLength];
            buffer.get(metadata);
        }

        return new MarketMessage(instrument, price, volume, timestamp, metadata);
    }

    /** Generates a realistic market data message with proper instrument symbols */
    public static MarketMessage generateRandomMsg() {
        return generateRandomMsg(DEFAULT_MAX_METADATA_SIZE); // Default: 0-100 bytes metadata
    }

    /**
     * Generates a realistic market data message with configurable metadata size
     *
     * @param minMetadataSize minimum metadata size in bytes
     * @param maxMetadataSize maximum metadata size in bytes
     */
    public static MarketMessage generateRandomMsg(int maxMetadataSize) {
        int metadataSize = RND.nextInt(maxMetadataSize + 1);

        // Generate random asset IDs from INST001-INST999
        int numInstruments = 50; // Number of instruments to include
        String[] instruments = new String[numInstruments];
        for (int i = 0; i < numInstruments; i++) {
            int assetId = RND.nextInt(999) + 1; // Random number 1-999
            instruments[i] = String.format("INST%03d", assetId);
        }

        String instrument = instruments[RND.nextInt(instruments.length)];

        // Realistic price ranges based on instrument type
        double price;
        if (instrument.contains("USD")) {
            // Forex pairs: 0.5 to 2.0
            price = 0.5 + RND.nextDouble() * 1.5;
        } else if (instrument.startsWith("BTC") || instrument.startsWith("ETH")) {
            // Crypto: 1000 to 100000
            price = 1000 + RND.nextDouble() * 99000;
        } else {
            // Stocks: 10 to 1000
            price = 10.0 + RND.nextDouble() * 990.0;
        }
        price = Math.round(price * 10000.0) / 10000.0; // 4 decimal places

        // Realistic volume (1-10000 lots)
        int volume = 1 + RND.nextInt(10000);

        // Current timestamp in nanoseconds
        long timestamp = System.nanoTime();

        // Generate metadata with uniform distribution between min and max
        byte[] metadata = generateMetadata(metadataSize);

        return new MarketMessage(instrument, price, volume, timestamp, metadata);
    }

    /** Generates metadata with uniform distribution between min and max size */
    private static byte[] generateMetadata(int metadataSize) {
        if (metadataSize <= 0) {
            return null; // No metadata
        }

        // Uniform distribution: minSize to maxSize (inclusive)
        byte[] metadata = new byte[metadataSize];
        RND.nextBytes(metadata); // Fill with random bytes

        return metadata;
    }

    /** Generates a price update with small variation (simulating tick movement) */
    public static MarketMessage generatePriceUpdate(MarketMessage previous, double maxPriceChange) {
        double priceChange = (RND.nextDouble() - 0.5) * 2 * maxPriceChange;
        double newPrice = Math.max(0.01, previous.price + priceChange);
        newPrice = Math.round(newPrice * 10000.0) / 10000.0; // 4 decimal places

        return new MarketMessage(
                previous.instrument,
                newPrice,
                previous.volume,
                System.nanoTime(),
                previous.metadata // Keep the same metadata for price updates
        );
    }

    // Getters for performance measurement
    public String getInstrument() {
        return instrument;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getMetadata() {
        return metadata;
    }

    /** Get the total message size in bytes */
    public int getMessageSize() {
        return 28 + instrument.getBytes().length + (metadata != null ? metadata.length : 0);
    }

    @Override
    public String toString() {
        return String.format(
                "MarketData{instrument='%s', price=%.4f, volume=%d, timestamp=%d, metadata=(%d bytes)}",
                instrument, price, volume, timestamp, metadata != null ? metadata.length : 0);
    }
}
