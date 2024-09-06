package com.cnebrera.uc3.tech.lesson3;

import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SigInt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A very simple Aeron subscriber application which can receive small non-fragmented messages
 * on a fixed channel and stream ID. The DataHandler method 'printStringMessage' is called when data
 * is received.
 */
public class SimpleSubscriber
{
    /**
     * Main method for launching the process.
     *
     * @param args passed to the process.
     */
    public static void main(final String[] args)
    {
        final int expectedMessages = Constants.NUM_MESSAGES;

        final AtomicInteger successfullyReceived = new AtomicInteger(0);

        // Maximum number of message fragments to receive during a single 'poll' operation
        final int fragmentLimitCount = 10;

        // The channel (an endpoint identifier) to receive messages from
        final String channel = Constants.CHANNEL;

        // A unique identifier for a stream within a channel. Stream ID 0 is reserved
        // for internal use and should not be used by applications.
        final int streamId = Constants.STREAM_ID;

        System.out.println("Subscribing to " + channel + " on stream id " + streamId);

        final AtomicBoolean running = new AtomicBoolean(true);
        // Register a SIGINT handler for graceful shutdown.
        SigInt.register(() -> running.set(false));

        // fragmentHandler method is called for every new datagram received
        final FragmentHandler fragmentHandler =
                (buffer, offset, length, header) ->
                {
                    final byte[] data = new byte[length];
                    buffer.getBytes(offset, data);

                    // TODO Convert received data to a string message

                    // TODO Print received message

                    System.out.printf(
                        "Received message to stream %d from session %x term id %x term offset %d (%d@%d)%n",
                        streamId, header.sessionId(),
                        header.termId(), header.termOffset(), length, offset);

                    int num = successfullyReceived.incrementAndGet();
                    if(num >= expectedMessages) {
                        // Received the intended message, time to exit the program
                        running.set(false);
                    }
                };

        // Create a context, needed for client connection to media driver
        // A separate media driver process need to run prior to running this application
        final Aeron.Context ctx = new Aeron.Context();

        // Create an Aeron instance with client-provided context configuration, connect to the
        // media driver, and add a subscription for the given channel and stream using the supplied
        // dataHandler method, which will be called with new messages as they are received.
        // The Aeron and Subscription classes implement AutoCloseable, and will automatically
        // clean up resources when this try block is finished.
        try (Aeron aeron = Aeron.connect(ctx);
             Subscription subscription = aeron.addSubscription(channel, streamId))
        {
            final IdleStrategy idleStrategy = new BackoffIdleStrategy(
                    100, 10, TimeUnit.MICROSECONDS.toNanos(1), TimeUnit.MICROSECONDS.toNanos(100));

            // Try to read the data from subscriber
            while (running.get())
            {
                // poll delivers messages to the dataHandler as they arrive
                // and returns number of fragments read, or 0
                // if no data is available.
                final int fragmentsRead = subscription.poll(fragmentHandler, fragmentLimitCount);
                // Give the IdleStrategy a chance to spin/yield/sleep to reduce CPU
                // use if no messages were received.
                idleStrategy.idle(fragmentsRead);
            }

            System.out.println("Shutting down...");

        }
    }
}
