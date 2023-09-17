package com.cnebrera.uc3.tech.lesson2.tcp.client;

import com.cnebrera.uc3.tech.lesson2.util.VariableSizeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * TCP client that read messages of variable size from a server
 */
public class TCPVarSizeClient {
    public static void main(String argv[]) throws Exception {
        // TODO 1 Create the client socket
        

        // TODO 2 Get the input stream
        

        // Read messages non stop
        while (true) {
            // TODO 3 call read message with the input stream
            
        }
    }

    /**
     * Send messages into the input stream
     * 
     * @param inputStream the input stream connected to the socket
     * @throws IOException exception if there is an input output problem
     */
    private static void readMessages(final InputStream inputStream) throws IOException {
        // The buffer to read the header
        final byte[] header = new byte[4];

        // TODO 4 Wait to have at least the header


        // TODO 5 Read the header
        

        final int msgSize = ByteBuffer.wrap(header).getInt();

        System.out.println("Read MsgSize " + msgSize);

        // The buffer to read the message bytes
        final byte[] msgBytes = new byte[msgSize];

        // TODO 6 Wait for the whole message to be ready
        

        // TODO 7 read the message bytes
        

        // Create the message
        final VariableSizeMessage msg = VariableSizeMessage.readMsgFromBinary(msgSize, ByteBuffer.wrap(msgBytes));

        System.out.println("Msg received " + msg);
    }
}
