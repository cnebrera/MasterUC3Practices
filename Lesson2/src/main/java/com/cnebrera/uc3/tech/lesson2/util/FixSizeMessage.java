package com.cnebrera.uc3.tech.lesson2.util;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Represents a simple message
 */
public class FixSizeMessage
{
    /** Random number generator */
    private final static Random RND = new Random();

    /** Size of the message in binary, 8 bytes for double, 4 for each int */
    public final static int MSG_BINARY_SIZE = 16;

    /** Intrument ID */
    private final int instrument;

    /** Price */
    private final double price;

    /** Volume */
    private final int volume;

    public FixSizeMessage(int instrument, double price, int volume)
    {
        this.instrument = instrument;
        this.price = price;
        this.volume = volume;
    }

    /**
     * Convert the message into binary and store it in the given buffer.
     *
     * @param buffer the buffer that will store the serialized message
     */
    public void toBinary(final ByteBuffer buffer)
    {
        buffer.putInt(instrument);
        buffer.putDouble(price);
        buffer.putInt(volume);
    }

    /**
     * Read the msg from binary
     * @param buffer the buffer containing the message
     * @return the read message
     */
    public static FixSizeMessage readMsgFromBinary(final ByteBuffer buffer)
    {
        return new FixSizeMessage(
                buffer.getInt(),
                buffer.getDouble(),
                buffer.getInt());
    }

    /** Generates a random message */
    public static FixSizeMessage generateRandomMsg()
    {
        return new FixSizeMessage(RND.nextInt(), RND.nextDouble(), RND.nextInt());
    }

    @Override
    public String toString()
    {
        return "FixSizeMessage{" +
                "instrument=" + instrument +
                ", price=" + price +
                ", volume=" + volume +
                '}';
    }
}
