package es.uc3m.fintech.lesson2.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import org.apache.commons.cli.*;

import es.uc3m.fintech.lesson2.message.MarketMessage;

/**
 * TCP server that streams variable-size market data messages to a single
 * client.
 *
 * Each message is sent as a frame: a 4-byte length header (big-endian) followed
 * by the binary payload.
 * The server generates random MarketMessage instances with configurable
 * metadata size and sends them
 * at a specified rate (messages per second) to the connected client.
 *
 * Usage: java TCPServer [-p port] [-r rate] [-s metadata-size]
 * -p, --port Port number to listen on (default: 6789)
 * -r, --rate Messages per second to send (default: 1)
 * -s, --metadata-size Maximum metadata size in bytes (default: 100)
 *
 * @author Mario Cao
 * @version 1.0
 */
public class TCPServer {

    /** Port the server is going to listen on */
    static final int PORT = 6789;

    /** Rate (msg/s) the server is going to send messages at */
    static final int RATE = 1;

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        // Remove mode option - no longer needed
        options.addOption(
                Option.builder("p").longOpt("port").hasArg().desc("Port number (default: 6789)").build());
        options.addOption(
                Option.builder("r")
                        .longOpt("rate")
                        .hasArg()
                        .desc("Messages per second (default: 1)")
                        .build());
        options.addOption(
                Option.builder("s")
                        .longOpt("metadata-size")
                        .hasArg()
                        .desc("Metadata size (default: 100)")
                        .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            int port = Integer.parseInt(cmd.getOptionValue("port", String.valueOf(PORT)));
            int rate = Integer.parseInt(cmd.getOptionValue("rate", String.valueOf(RATE)));
            int metadataSize = Integer.parseInt(
                    cmd.getOptionValue(
                            "metadata-size", String.valueOf(MarketMessage.DEFAULT_MAX_METADATA_SIZE)));

            runServer(port, metadataSize, rate);

        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            formatter.printHelp("TCPServer", options);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Runs the TCP server with the specified parameters
     * 
     * @param port            the port number to listen on
     * @param maxMetadataSize the maximum size of metadata in generated messages
     * @param rate            the rate of messages to send per second
     * @throws IOException          if there's an error with network operations
     * @throws InterruptedException if the thread is interrupted during sleep
     */
    private static void runServer(int port, int maxMetadataSize, int rate)
            throws IOException, InterruptedException {
                System.out.printf("[Server] Listening on port %d (%d msg/s)%n", port, rate);

        try (
            // TODO 1.1: Create the server socket with the given port
        ) {
            // TODO 1.2: Accept a connection and get the connection socket

            // TODO 1.3: Send messages to the connected client socket
        }
    }

    /**
     * Sends messages to the connected client socket
     * 
     * @param client          the connected client socket
     * @param maxMetadataSize the maximum size of metadata in generated messages
     * @param ratePerSec      the rate of messages to send per second
     * @throws IOException          if there's an error with network operations
     * @throws InterruptedException if the thread is interrupted during sleep
     */
    private static void sendMessages(Socket client, int maxMetadataSize, int ratePerSec)
            throws IOException, InterruptedException {
        final OutputStream out = client.getOutputStream();
        final long nanosPerMsg = ratePerSec > 0 ? 1_000_000_000L / ratePerSec : 0L;
        long nextDeadline = System.nanoTime();
        long sent = 0;

        try {
            while (true) {
                // TODO 2.1: Generate variable-size message (modify argument)
                MarketMessage message = MarketMessage.generateRandomMsg(0);

                ByteBuffer payload = message.toBinary();
                int messageSize = payload.limit();

                // TODO 2.2: Add protocol header (4-byte message size)
                // Hint: Use ByteBuffer to allocate a 4 bytes, put the message size (int) into it and cover it to byte array

                // TODO 2.3: Write the header to the output stream (reliable delivery)

                // Write the payload to the output stream
                out.write(payload.array());
                out.flush();

                sent++;
                System.out.printf("Sent msg #%d: %d bytes%n", sent, payload.remaining());
                printMessage(message, sent);

                if (nanosPerMsg > 0) {
                    nextDeadline += nanosPerMsg;
                    long wait = nextDeadline - System.nanoTime();
                    if (wait > 0) {
                        Thread.sleep(wait / 1_000_000L, (int) (wait % 1_000_000L));
                    }
                }
            }
        } catch (IOException e) {
            if (e.getMessage().contains("Broken pipe") || e.getMessage().contains("Connection reset")) {
                System.out.printf("[Server] Client disconnected. Sent %d messages.%n", sent);
            } else {
                System.out.printf(
                        "[Server] Connection error: %s. Sent %d messages.%n", e.getMessage(), sent);
            }
        }
    }

    private static void printMessage(MarketMessage message, long messageCount) {
        String msgStr = message.toString();
        if (msgStr.length() > 120) {
            msgStr = msgStr.substring(0, 97) + "...";
        }
        System.out.printf("Msg #%d: %s%n", messageCount, msgStr);
    }
}
