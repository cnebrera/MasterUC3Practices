package com.cnebrera.uc3.tech.lesson2.util;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Represents a simple message of variable size once serialized into binary
 */
public class VariableSizeMessage
{
    /** Random number generator */
    private final static Random RND = new Random();

    /** Price */
    private final double price;

    /** Volume */
    private final int volume;

    /** Intrument ID */
    private final String instrument;

    public VariableSizeMessage(String instrument, double price, int volume)
    {
        this.instrument = instrument;
        this.price = price;
        this.volume = volume;
    }

    /**
     * Convert the message into binary
     */
    public ByteBuffer toBinary()
    {
        // Convert the message String contents to binary
        final byte[] intrumentInBinary = instrument.getBytes();

        // Calculate msg size, the String plus the integer and double
        final int msgSize = intrumentInBinary.length + 4 + 8;

        // Write the length of the contents, this is the header
        final ByteBuffer resultBuffer = ByteBuffer.allocate(msgSize);

        // Write the contents
        resultBuffer.putInt(this.volume);
        resultBuffer.putDouble(this.price);
        resultBuffer.put(intrumentInBinary);

        // Get the result buffer
        return resultBuffer;
    }

    /**
     * Read the msg from binary given the message length and the buffer containing the message
     *
     * @param msgLength length of the message
     * @param buffer the buffer containing the message
     * @return the read message
     */
    public static VariableSizeMessage readMsgFromBinary(final int msgLength, final ByteBuffer buffer)
    {
        int volume = buffer.getInt();
        double price = buffer.getDouble();

        final byte[] instrumentBinary = new byte[msgLength - 12];
        buffer.get(instrumentBinary);

        final String instrument = new String(instrumentBinary);

        return new VariableSizeMessage(instrument, price, volume);
    }

    /** Generates a random message with a given string size by appending values several times */
    public static VariableSizeMessage generateRandomMsg(final int numStringAppends)
    {
        final StringBuilder stringBuilder = new StringBuilder("INST");

        for (int i = 0; i < numStringAppends; i++)
        {
            stringBuilder.append("{" + numStringAppends + "}");
        }

        return new VariableSizeMessage(stringBuilder.toString(), RND.nextDouble(), RND.nextInt());
    }

    @Override
    public String toString()
    {
        return "VariableSizeMessage{" +
                "price=" + price +
                ", volume=" + volume +
                ", instrument='" + instrument + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        VariableSizeMessage that = (VariableSizeMessage) o;

        if (Double.compare(that.price, price) != 0)
        {
            return false;
        }
        if (volume != that.volume)
        {
            return false;
        }
        return instrument != null ? instrument.equals(that.instrument) : that.instrument == null;

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(price);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + volume;
        result = 31 * result + (instrument != null ? instrument.hashCode() : 0);
        return result;
    }
}
