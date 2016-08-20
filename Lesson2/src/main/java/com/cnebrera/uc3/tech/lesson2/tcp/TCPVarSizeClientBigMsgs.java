package com.cnebrera.uc3.tech.lesson2.tcp;

import com.cnebrera.uc3.tech.lesson2.util.VariableSizeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by cnebrera on 17/08/16.
 */
public class TCPVarSizeClientBigMsgs
{
    public static void main(String argv[]) throws Exception
    {
        // TODO 1 Create the client socket

        // TODO 2 Get the input stream

        while(true)
        {
            // TODO 3 Call send messages with the input stream
        }
    }

    private static void readMessage(final InputStream inputStream) throws IOException
    {
        // The buffer to read the header
        final byte [] header = new byte[4];

        // TODO 4 Wait to have at least the header

        // TODO 5 Read the header

        final int msgSize = ByteBuffer.wrap(header).getInt();

        System.out.println("Read MsgSize " + msgSize);

        // The buffer to read the message bytes
        final byte [] msgBytes = new byte[msgSize];

        // Read the message in "buckets"
        int numBytesRead = 0;

        // We need at least the header to know how long is the message
        while(numBytesRead < msgBytes.length)
        {
            // Read the next bytes
            final int bytesToRead = msgBytes.length - numBytesRead < 128 ? (msgBytes.length - numBytesRead) : 128;

            // TODO 6 read the message bytes
        }

        // Create the message
        final VariableSizeMessage msg = VariableSizeMessage.readMsgFromBinary(msgSize, ByteBuffer.wrap(msgBytes));

        System.out.println("Msg received " + msg);
    }
}
