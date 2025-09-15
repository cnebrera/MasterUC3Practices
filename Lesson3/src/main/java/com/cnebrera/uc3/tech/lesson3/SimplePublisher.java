package com.cnebrera.uc3.tech.lesson3;

import io.aeron.Aeron;
import io.aeron.Publication;
import org.agrona.BitUtil;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;

import java.util.concurrent.TimeUnit;

/**
 * A very simple Aeron publisher application which publishes a fixed size message on a fixed channel and stream.
 */
public class SimplePublisher
{
    /**
     * Main method for launching the process.
     *
     * @param args passed to the process.
     * @throws InterruptedException if the thread sleep delay is interrupted.
     */
    public static void main(final String[] args) throws InterruptedException
    {
        // Allocate enough buffer size to hold maximum message length
        // The UnsafeBuffer class is part of the Agrona library and is used for efficient buffer management
        final UnsafeBuffer buffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(512, BitUtil.CACHE_LINE_LENGTH));

        // The channel (an endpoint identifier) to send the message to
        final String channel = Constants.CHANNEL;

        // A unique identifier for a stream within a channel. Stream ID 0 is reserved
        // for internal use and should not be used by applications.
        final int streamId = Constants.STREAM_ID;

        System.out.println("Publishing to " + channel + " on stream id " + streamId);

        // Create a context, needed for client connection to media driver
        // A separate media driver process needs to be running prior to starting this application
        final Aeron.Context ctx = new Aeron.Context();

        // Create an Aeron instance with client-provided context configuration and connect to the
        // media driver, and create a Publication. The Aeron and Publication classes implement
        // AutoCloseable, and will automatically clean up resources when this try block is finished.
        try (Aeron aeron = Aeron.connect(ctx);
             Publication publication = aeron.addPublication(channel, streamId))
        {
            // Wait for 5 seconds to connect to a subscriber
            final long deadlineNs = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
            while (!publication.isConnected())
            {
                if ((deadlineNs - System.nanoTime()) < 0)
                {
                    System.out.println("Failed to connect to subscriber");
                    return;
                }

                Thread.sleep(1);
            }

            // Sending multiple messages
            for(int i=0; i < Constants.NUM_MESSAGES; i++){

                // Create a string message to send and put it on the buffer
                final String message = "Hello World!";
                final byte[] messageBytes = message.getBytes();
                buffer.putBytes(0, messageBytes);

                // Try to publish the buffer. 'offer' is a non-blocking call.
                // If it returns less than 0, the message was not sent, and the offer should be retried.
                final long result = publication.offer(buffer, 0, messageBytes.length);

                if (result < 0L)
                {
                    if (result == Publication.BACK_PRESSURED)
                    {
                        System.out.println(" Offer failed due to back pressure");
                    }
                    else if (result == Publication.NOT_CONNECTED)
                    {
                        System.out.println(" Offer failed because publisher is not connected to subscriber");
                    }
                    else if (result == Publication.ADMIN_ACTION)
                    {
                        System.out.println("Offer failed because of an administration action in the system");
                    }
                    else if (result == Publication.CLOSED)
                    {
                        System.out.println("Offer failed publication is closed");
                    }
                    else
                    {
                        System.out.println(" Offer failed due to unknown reason");
                    }
                }
                else
                {
                    System.out.println(i + " yay !! " + message + " sent");
                }

            }

            System.out.println("Done sending.");
        }
    }
}
