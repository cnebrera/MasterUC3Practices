package com.cnebrera.uc3.tech.lesson2.tcp;

import com.cnebrera.uc3.tech.lesson2.util.VariableSizeMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * TCP server that send messages of variable size to a client
 */
public class TCPVarSizeServer
{
    public static void main(String argv[]) throws Exception
    {
        // TODO 1 create the acceptor socket

        // TODO 2 accept a connection and get the connection socket

        // TODO 3 send the messages to the connected client socket
    }

    /**
     * Send messages to a client given the client connection socket
     *
     * @param connectionSocket the client connection socket
     * @throws IOException exception thrown if there is an input-output problem
     * @throws InterruptedException exception thrown if interrupted while sleeping
     */
    private static void sendMessagesToClient(final Socket connectionSocket) throws IOException, InterruptedException
    {
        // Get the output stream
        final OutputStream outputStream = connectionSocket.getOutputStream();

        // The buffer to write the msg size header, the message size will be an integer (4 bytes)
        final ByteBuffer headerBuffer = ByteBuffer.allocate(4);

        while (true)
        {
            // Clear the buffer
            headerBuffer.clear();

            // Generate random message
            final VariableSizeMessage rndMsg = VariableSizeMessage.generateRandomMsg(8);

            // Convert to binary
            final ByteBuffer binaryMessage = rndMsg.toBinary();

            // Serialize the msg size
            headerBuffer.putInt(binaryMessage.position());

            // Flip the binary message prior to writing to adjust position to 0 and limit to the end of the buffer
            binaryMessage.flip();

            System.out.println("About to send msg of size " + binaryMessage.position() + headerBuffer.position());

            // TODO 4 Write the header with the message size

            // TODO 5 Write the contents

            // TODO 6 Force it to be sent without batching

            System.out.println("Message sent: " + rndMsg.toString());

            // Wait a bit
            Thread.sleep(1000);
        }
    }
}
