package com.cnebrera.uc3.tech.lesson5.util.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import com.cnebrera.uc3.tech.lesson5.util.Lesson5Exception;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Helper class to perform AES cryptography.
 *
 * This class is thread safe
 */
public class AESCrypto
{
    /** Random value for the key generation */
    private static final Random RND = new Random(System.currentTimeMillis());

    /** Size of the AES key in bytes */
    public static final int KEY_SIZE = 16;

    /** The AES key that will be used to encode / decode */
    private final byte[] aesKey;
    /** The Cipher that will encode the messages */
    private final Cipher cipher;
    /** The Cipher that will decode the messages */
    private final Cipher decipher;

    /**
     * Create a new instance for AES encoding / decoding, the instance will use the provided key
     *
     * @return the created instance
     * @throws Lesson5Exception exception thrown if the instance cannot be created
     */
    public static AESCrypto createNewInstance() throws Lesson5Exception
    {
        // TODO 1
    }
    
    /**
     * Create a new instance given the AES key that will be used to encode / decode
     *
     * @param key the key to encode and decode
     * @throws Lesson5Exception exception thrown if there is a problem creating the internal encoding classes
     */
    public AESCrypto(final byte[] key) throws Lesson5Exception
    {
        // TODO 2

        // TODO 3

        try
        {
            // TODO 4
        }
        catch(final NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e)
        {
            throw new Lesson5Exception("Error creating AES encoding classes", e);
        }
    }

    /**
     * Return the AES key used by this encoder / decoder
     */
    public byte[] getAESKey()
    {
        return this.aesKey;
    }

    /**
     * Encode the given message and return the AES encoding with the key used during creation of the class
     *
     * @param msg the message to be encoded
     * @return the encoded message
     * @throws Lesson5Exception exception thrown if there is a problem encoding the message
     */
    public byte[] encode(final byte[] msg) throws Lesson5Exception
    {
        synchronized (this.cipher)
        {
            try
            {
                // TODO 5
            }
            catch (final IllegalBlockSizeException | BadPaddingException e)
            {
                throw new Lesson5Exception("Unexpected error performing AES encode", e);
            }
        }
    }

    /**
     * Decode the given message and return the AES decoding with the key used during creation of the class
     *
     * @param msg the message to be decoded
     * @return the decoded message
     * @throws Lesson5Exception exception thrown if there is a problem decoding the message
     */
    public byte[] decode(final byte[] msg) throws Lesson5Exception
    {
        synchronized (this.cipher)
        {
            try
            {
                // TODO 6
            }
            catch (final IllegalBlockSizeException | BadPaddingException e)
            {
                throw new Lesson5Exception("Unexpected error performing AES decode", e);
            }
        }
    }
}
