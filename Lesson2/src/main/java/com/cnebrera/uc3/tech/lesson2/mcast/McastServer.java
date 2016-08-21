package com.cnebrera.uc3.tech.lesson2.mcast;

import com.cnebrera.uc3.tech.lesson2.util.VariableSizeMessage;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * Multicast server that send messages of variable size into a multicast group
 */
public class McastServer
{
    /** Multicast address that we are going to join as receiver */
    final static String INET_ADDR = "224.0.0.3";
    /** Port the receiver is going to use */
    final static int PORT = 8888;

    public static void main(final String[] args) throws IOException, InterruptedException
    {
        // Get the address that we are going to connect to.
        final InetAddress addr = InetAddress.getByName(INET_ADDR);

        // TODO 1 Open a new DatagramSocket, which will be used to send the data.

        while (true)
        {
            // TODO 2 Call send message with the address and the created DatagramSocket
        }
    }

    /**
     * Send a message to the given multicast address
     * @param addr the address to send the message into
     * @param serverSocket the server socket that will send the message
     * @throws IOException
     * @throws InterruptedException
     */
    private static void sendMessage(final InetAddress addr, final DatagramSocket serverSocket) throws IOException, InterruptedException
    {
        // Generate random message
        final VariableSizeMessage rndMsg = VariableSizeMessage.generateRandomMsg(8);

        // Convert the message to binary
        final ByteBuffer binaryMessage = rndMsg.toBinary();
        binaryMessage.flip();

        // TODO 3 Create the datagram with the message

        System.out.println("About to send message datagram of size " + binaryMessage.limit());

        // TODO 4 Send it

        // Wait a bit
        Thread.sleep(1000);
    }
}
