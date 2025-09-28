package es.uc3m.fintech.lesson5.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Helper class to perform AES cryptography.
 * --------------------------------------
 * This class is thread safe
 */
public class AESCrypto {
    /**
     * Random value for the key generation
     */
    private static final Random RND = new Random(System.currentTimeMillis());

    /**
     * Size of the AES key in bytes
     */
    public static final int KEY_SIZE = 16;

    /**
     * The AES key that will be used to encode / decode
     */
    private final byte[] aesKey;
    /**
     * The Cipher that will encode the messages
     */
    private final Cipher cipher;
    /**
     * The Cipher that will decode the messages
     */
    private final Cipher decipher;

    /**
     * Create a new instance for AES encoding / decoding, the instance will use the
     * provided key
     *
     * @return the created instance
     * @throws Exception exception thrown if the instance cannot be created
     */
    public static AESCrypto createNewInstance() throws Exception {
        // TODO 1: Generate a random AES key and create new instance
    }

    /**
     * Create a new instance given the AES key that will be used to encode / decode
     *
     * @param key the key to encode and decode
     * @throws Exception exception thrown if there is a problem creating the
     *                   internal encoding classes
     */
    public AESCrypto(final byte[] key) throws Exception {
        // TODO 2: Store the key

        // TODO 3: Create the secret key spec

        try {
            // TODO 4: Initialize cipher for encryption and decryption
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new Exception("Error creating AES encoding classes", e);
        }
    }

    /**
     * Return the AES key used by this encoder / decoder
     */
    public byte[] getAESKey() {
        return this.aesKey;
    }

    /**
     * Encode the given message and return the AES encoding with the key used during
     * creation of the class
     *
     * @param msg the message to be encoded
     * @return the encoded message
     * @throws Exception exception thrown if there is a problem encoding the message
     */
    public byte[] encode(final byte[] msg) throws Exception {
        synchronized (this.cipher) {
            try {
                // TODO 5: Encrypt the message using the cipher
            } catch (final IllegalBlockSizeException | BadPaddingException e) {
                throw new Exception("Unexpected error performing AES encode", e);
            }
        }
    }

    /**
     * Decode the given message and return the AES decoding with the key used during
     * creation of the class
     *
     * @param msg the message to be decoded
     * @return the decoded message
     * @throws Exception exception thrown if there is a problem decoding the message
     */
    public byte[] decode(final byte[] msg) throws Exception {
        synchronized (this.decipher) {
            try {
                // TODO 6: Decrypt the message using the decipher
            } catch (final IllegalBlockSizeException | BadPaddingException e) {
                throw new Exception("Unexpected error performing AES decode", e);
            }
        }
    }
}
