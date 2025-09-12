package es.uc3m.fintech.lesson2.mcast;

import es.uc3m.fintech.lesson2.message.MarketMessage;
import es.uc3m.fintech.lesson2.util.PerformanceMetrics;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import org.apache.commons.cli.*;

/**
 * Multicast client that receives variable-size market data messages from a
 * multicast group.
 * This client joins a multicast group and receives messages with a 4-byte
 * length header
 * followed by the message payload. It includes performance metrics collection
 * and
 * supports configurable duration, port, and multicast address.
 * 
 * Usage: java McastClient [options]
 * -a, --address Multicast address to join (default: 224.0.0.1)
 * -p, --port Multicast port (default: 8888)
 * -d, --duration Duration to receive messages in seconds (default: 10)
 * 
 * @author Mario Cao
 * @version 1.0
 */
public class McastClient {
    /** Multicast address that we are going to join as receiver */
    static final String GROUP_ADDRESS = "224.0.0.1";

    /** Port the receiver is going to use */
    static final int PORT = 8888;

    public static void main(String[] args) throws UnknownHostException, IOException {
        Options options = new Options();
        options.addOption(
                Option.builder("p")
                        .longOpt("port")
                        .hasArg()
                        .desc("Multicast port (default: 8888)")
                        .build());
        options.addOption(
                Option.builder("d")
                        .longOpt("duration")
                        .hasArg()
                        .desc("Duration in seconds (default: 10)")
                        .build());
        options.addOption(
                Option.builder("a")
                        .longOpt("address")
                        .hasArg()
                        .desc("Multicast address (default: 224.0.0.1)")
                        .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            int port = Integer.parseInt(cmd.getOptionValue("port", String.valueOf(PORT)));
            int durationSeconds = Integer.parseInt(cmd.getOptionValue("duration", "10"));
            String multicastAddress = cmd.getOptionValue("address", GROUP_ADDRESS);

            runClient(multicastAddress, port, durationSeconds);

        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            formatter.printHelp("McastClient", options);
            System.exit(1);
        }
    }

    /**
     * Runs the multicast client with the specified connection parameters
     * 
     * @param multicastAddress the multicast address to join
     * @param port             the port number to receive messages on
     * @param durationSeconds  how long to receive messages (in seconds)
     * @throws UnknownHostException if the multicast address is invalid
     * @throws IOException          if there's an error with network operations
     */
    private static void runClient(String multicastAddress, int port, int durationSeconds)
            throws UnknownHostException, IOException {
        // Get the address that we are going to connect to.
        final InetAddress address = InetAddress.getByName(multicastAddress);

        // Create a buffer of bytes, which will be used to store the received messages
        final ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

        // TODO 1: Create a new MulticastSocket for the given port

        // TODO 2: Join multicast group by address
        // Hint: Use clientSocket.joinGroup(InetSocketAddress, NetworkInterface) to join the multicast group
        // Create InetSocketAddress with the multicast address and port, pass null for NetworkInterface to use default

        System.out.printf("Joined multicast group %s:%d%n", multicastAddress, port);
        System.out.printf("Receiving messages for %d seconds...%n", durationSeconds);

        // Add performance metrics (similar to TCP client)
        PerformanceMetrics metrics = new PerformanceMetrics("Multicast");

        long endTime = System.currentTimeMillis() + (durationSeconds * 1000L);

        try {
            while (System.currentTimeMillis() < endTime) {
                long startTime = System.nanoTime();

                // TODO 3: Receive the next message by calling receiveMessage
                MarketMessage message = null;

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
        } catch (Exception e) {
            System.out.println("Multicast connection closed: " + e.getMessage());
        } finally {
            metrics.printSummary();
            System.out.println("Client disconnected");
        }
    }

    /**
     * Receive the next datagram for the given multicast socket
     *
     * @param receiveBuffer the reusable buffer that will be used to store the
     *                      message binary contents
     * @param clientSocket  the multicast client socket
     * @throws IOException exception thrown if there is a problem in the
     *                     input/output
     */
    private static MarketMessage receiveMessage(
            final ByteBuffer receiveBuffer, final MulticastSocket clientSocket) throws IOException {
        // Clear the receive buffer
        receiveBuffer.clear();

        // Create a datagram to receive the next message
        final DatagramPacket msgPacket = new DatagramPacket(receiveBuffer.array(), receiveBuffer.capacity());

        // TODO 4: Receive the next message from the client socket

        // Set the limits of the buffer with the received datagram information
        receiveBuffer.limit(msgPacket.getLength());

        // TODO 5: Deserialize the message content (replace return null with the
        // message)
        return null;
    }

    // Add the same printMessage method as TCP client:
    private static void printMessage(MarketMessage message, long messageCount) {
        String msgStr = message.toString();
        if (msgStr.length() > 120) {
            msgStr = msgStr.substring(0, 97) + "...";
        }
        System.out.printf("Msg #%d: %s%n", messageCount, msgStr);
    }
}
