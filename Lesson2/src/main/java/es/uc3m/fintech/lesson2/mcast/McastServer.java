package es.uc3m.fintech.lesson2.mcast;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import org.apache.commons.cli.*;

import es.uc3m.fintech.lesson2.message.MarketMessage;

/**
 * Multicast server that sends variable-size market data messages to a multicast
 * group.
 *
 * Usage: java McastServer [-a address] [-p port] [-r rate]
 * -a, --address Multicast address to send to (default: 224.0.0.1)
 * -p, --port Multicast port (default: 8888)
 * -r, --rate Message rate per second (default: 1)
 *
 * This server generates random MarketMessage instances and broadcasts them
 * at the specified rate to the given multicast group.
 *
 * @author Mario Cao
 * @version 1.0
 */
public class McastServer {
    /** Multicast address that we are going to join as receiver */
    static final String INET_ADDR = "224.0.0.1"; // All systems multicast

    /** Port the receiver is going to use */
    static final int PORT = 8888;

    /** Rate (msg/s) the server is going to send messages at */
    static final int RATE = 1;

    public static void main(final String[] args) throws IOException, InterruptedException {
        Options options = new Options();
        options.addOption(
                Option.builder("a")
                        .longOpt("address")
                        .hasArg()
                        .desc("Multicast address (default: 224.0.0.1)")
                        .build());
        options.addOption(
                Option.builder("p")
                        .longOpt("port")
                        .hasArg()
                        .desc("Multicast port (default: 8888)")
                        .build());
        options.addOption(
                Option.builder("r")
                        .longOpt("rate")
                        .hasArg()
                        .desc("Message rate per second (default: 1)")
                        .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            String multicastAddress = cmd.getOptionValue("address", String.valueOf(INET_ADDR));
            int port = Integer.parseInt(cmd.getOptionValue("port", String.valueOf(PORT)));
            int ratePerSecond = Integer.parseInt(cmd.getOptionValue("rate", String.valueOf(RATE)));

            runServer(multicastAddress, port, ratePerSecond);

        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            formatter.printHelp("McastServer", options);
            System.exit(1);
        }
    }

    /**
     * Runs the multicast server with the specified parameters
     * 
     * @param multicastAddress the multicast address to send messages to
     * @param port             the port number to send messages to
     * @param ratePerSecond    the rate of messages to send per second
     * @throws IOException          if there's an error with network operations
     * @throws InterruptedException if the thread is interrupted during sleep
     */
    private static void runServer(String multicastAddress, int port, int ratePerSecond)
            throws IOException, InterruptedException {
        // Get the address that we are going to connect to.
        final InetAddress addr = InetAddress.getByName(multicastAddress);

        // TODO 1: Open a new DatagramSocket, which will be used to send the data

        System.out.printf(
                "Starting multicast server on %s:%d at %d msg/s%n", multicastAddress, port, ratePerSecond);

        // Calculate delay between messages based on rate
        long delayMs = 1000L / ratePerSecond; // Convert rate to delay in milliseconds
        long delayNs = delayMs * 1_000_000L; // Convert to nanoseconds for precision

        int sent = 0;

        while (true) {
            sent++;
            long messageStartTime = System.nanoTime();

            // TODO 2: Call send message with the address and the created DatagramSocket

            // Rate control: sleep to maintain the desired rate
            long messageEndTime = System.nanoTime();
            long messageDuration = messageEndTime - messageStartTime;
            long sleepTime = delayNs - messageDuration;
            if (sleepTime > 0) {
                Thread.sleep(sleepTime / 1_000_000L, (int) (sleepTime % 1_000_000L));
            }
        }
    }

    /**
     * Send a message to the given multicast address
     *
     * @param addr         the address to send the message into
     * @param serverSocket the server socket that will send the message
     * @throws IOException
     * @throws InterruptedException
     */
    private static void sendMessage(
            final DatagramSocket serverSocket, final InetAddress addr, int port, int sent)
            throws IOException, InterruptedException {
        // Generate random message
        final MarketMessage rndMsg = MarketMessage.generateRandomMsg();

        // Convert the message to binary
        final ByteBuffer binaryMessage = rndMsg.toBinary();

        // TODO 3: Create the datagram with message content

        // TODO 4: Send the message at the given address and port

        System.out.printf("Sent msg #%d: %d bytes - %s%n", sent, binaryMessage.remaining(), rndMsg.toString());
    }
}
