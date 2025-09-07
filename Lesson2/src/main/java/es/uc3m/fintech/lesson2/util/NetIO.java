package es.uc3m.fintech.lesson2.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for reliable network I/O operations.
 * Provides methods to ensure complete data reading from input streams,
 * handling partial reads and stream closures gracefully.
 * 
 * @author Mario Cao
 * @version 1.0
 */
public final class NetIO {
    /**
     * Private constructor to prevent instantiation of utility class
     */
    private NetIO() {
    }

    /**
     * Reads exactly the specified number of bytes from the input stream.
     * This method blocks until all requested bytes are read or an error occurs.
     * 
     * @param in  the input stream to read from
     * @param buf the buffer to store the read bytes
     * @param off the offset in the buffer where to start storing bytes
     * @param len the number of bytes to read
     * @throws IOException  if an I/O error occurs during reading
     * @throws EOFException if the stream is closed before all bytes are read
     */
    public static void readFully(final InputStream in, final byte[] buf, final int off, final int len)
            throws IOException {
        int n = 0;
        while (n < len) {
            int r = in.read(buf, off + n, len - n);
            if (r < 0)
                throw new EOFException("Stream closed");
            n += r;
        }
    }
}