package es.uc3m.fintech.lesson2.tcp;

import es.uc3m.fintech.lesson2.message.MarketMessage;
import es.uc3m.fintech.lesson2.util.NetIO;
import es.uc3m.fintech.lesson2.util.PerformanceMetrics;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import org.apache.commons.cli.*;

/**
 * TCP client for receiving market data messages from a server.
 *
 * Supports both fixed-size and variable-size messages, including robust
 * handling
 * of partial reads for large messages. Designed for stepwise student
 * improvement:
 * 1. Fixed-size messages (no header)
 * 2. Variable-size messages (with 4-byte length header)
 * 3. Handling large messages and partial reads
 *
 * Usage: java TCPClient [-h host] [-p port] [-d duration]
 * -h, --host Server host (default: localhost)
 * -p, --port Server port (default: 6789)
 * -d, --duration Duration in seconds (default: 10)
 *
 * @author Mario Cao
 * @version 1.0
 */
public class TCPClient {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        // Remove mode option - no longer needed
        options.addOption(
                Option.builder("h")
                        .longOpt("host")
                        .hasArg()
                        .desc("Server host (default: localhost)")
                        .build());
        options.addOption(
                Option.builder("p").longOpt("port").hasArg().desc("Server port (default: 6789)").build());
        options.addOption(
                Option.builder("d")
                        .longOpt("duration")
                        .hasArg()
                        .desc("Duration in seconds (default: 10)")
                        .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            String host = cmd.getOptionValue("host", "localhost");
            int port = Integer.parseInt(cmd.getOptionValue("port", "6789"));
            int duration = Integer.parseInt(cmd.getOptionValue("duration", "10"));

            runClient(host, port, duration);

        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            formatter.printHelp("TCPClient", options);
            System.exit(1);
        }
    }

    /**
     * Runs the TCP client with the specified connection parameters
     * 
     * @param host            the server hostname to connect to
     * @param port            the server port to connect to
     * @param durationSeconds how long to receive messages (in seconds)
     * @throws Exception if there's an error during client operation
     */
    private static void runClient(String host, int port, int durationSeconds) throws Exception {
        System.out.printf("Connecting to %s:%d for %d seconds%n", host, port, durationSeconds);

        try (
            // TODO 1.1: Create the client socket for the given host and port
        ) {
            System.out.println("Connected to server");

            // TODO 1.2: Get the input stream from the socket

            PerformanceMetrics metrics = new PerformanceMetrics("TCP");
            System.out.println("Receiving messages...");
            long endTime = System.currentTimeMillis() + (durationSeconds * 1000L);

            try {
                while (System.currentTimeMillis() < endTime) {
                    long startTime = System.nanoTime();

                    // TODO 2.1: Read a variable-size message (4-byte header + payload)
                    MarketMessage message = readFixMessage(inputStream);

                    printMessage(message, metrics.getMessageCount());

                    // Metrics:Record the latency
                    long endTime_ns = System.nanoTime();
                    long latency = endTime_ns - startTime;
                    metrics.recordMessage(latency);
                    if (metrics.getMessageCount() % 100 == 0) {
                        System.out.printf(
                                "Received %d messages, throughput: %.2f msg/s%n",
                                metrics.getMessageCount(), metrics.getCurrentThroughput());
                    }
                }

                System.out.printf("Completed receiving messages after %d seconds%n", durationSeconds);
            } catch (IOException e) {
                System.out.println("Connection closed by server: " + e.getMessage());
            } finally {
                metrics.printSummary();
                System.out.println("Client disconnected");
            }
        }
    }

    /**
     * Read a fixed-size message (24 bytes, no header)
     * 
     * @param inputStream the input stream to read from
     * @return the parsed MarketMessage
     * @throws IOException if there's an error reading the message
     */
    private static MarketMessage readFixMessage(InputStream inputStream) throws IOException {
        // Create buffer for 35 bytes
        byte[] msgBytes = new byte[MarketMessage.MSG_BINARY_SIZE_NO_METADATA];

        // Use NetIO.readFully to read exactly 35 bytes
        NetIO.readFully(inputStream, msgBytes, 0, msgBytes.length);

        // Parse the message using MarketMessage.fromBinary
        return MarketMessage.fromBinary(msgBytes);
    }

    /**
     * Read a variable-size message (4-byte header + payload)
     * 
     * @param inputStream the input stream to read from
     * @return the parsed MarketMessage
     * @throws IOException if there's an error reading the message
     */
    private static MarketMessage readVarMessage(InputStream inputStream) throws IOException {
        byte[] header = new byte[4];

        // TODO 2.2: Read 4-byte header to get payload length using NetIO.readFully

        // TODO 2.3: Get message length from header using ByteBuffer.getInt() (replace 0
        // with the message length)
        int msgSize = 0;

        byte[] msgBytes = new byte[msgSize];

        // TODO 2.4: Read the payload bytes using NetIO.readFully

        // TODO 2.5: Parse the message using MarketMessage.fromBinary (replace return
        // null with the message)
        return null;
    }

    private static void printMessage(MarketMessage message, long messageCount) {
        String msgStr = message.toString();
        if (msgStr.length() > 120) {
            msgStr = msgStr.substring(0, 97) + "...";
        }
        System.out.printf("Msg #%d: %s%n", messageCount, msgStr);
    }
}
