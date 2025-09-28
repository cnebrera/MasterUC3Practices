package es.uc3m.fintech.lesson5.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Helper class for RSA encoding / decoding and to handle signatures
 * --------------------------------------
 * The instance of the class will contain the private key of the application and the public key of the other trusted
 * applications. The options have been simplified to allow:
 * - Sign messages with the owned private key
 * - Verify signatures coming from trusted applications (trusted public keys)
 * - Encode messages with the public key of a trusted application
 * - Decode messages that have been encoded with the owned public key
 */
public class RSACrypto {
    /**
     * Size of the supported RSA Key
     */
    public static final int RSA_KEY_SIZE = 1024;
    /**
     * Encoding type for the signature
     */
    public static final String SIGNATURE_CODEC = "SHA1withRSA";
    /**
     * Encoding type for RSA
     */
    public static final String RSA_CODEC = "RSA";

    /**
     * The key pair (public and private keys)
     */
    private final KeyPair keyPair;
    /**
     * Decoder to decode messages encoded with the own private key
     */
    private final Cipher ownPrivKeyDecoder;
    /**
     * Decoder to encode messages decoded with the own public key
     */
    private final Cipher ownPublKeyDecoder;

    /**
     * Create a new instance of the RSA cryptography helper class
     *
     * @throws Exception exception thrown if there is any issue with the private key
     */
    public RSACrypto() throws Exception {
        // TODO 1: Generate key pair

        // TODO 2: Initialize ciphers
    }

    /**
     * Generate a new public/private key pair
     */
    private KeyPair generateKeyPair() throws Exception {
        try {
            // TODO 3: Generate RSA key pair
        } catch (final NoSuchAlgorithmException e) {
            throw new Exception("Error generating key pair", e);
        }
    }

    /**
     * @param mode with the decrypt or encrypt mode
     * @param key  with the key
     * @return the cipher
     * @throws Exception with an occurred exception
     */
    private Cipher initCipher(final int mode, final Key key) throws Exception {
        try {
            // TODO 4: Initialize cipher with given mode and key
        } catch (final NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new Exception("Error initializing cipher classes", e);
        }
    }


    /**
     * Encode the given message with the public key of the given application. It has to be already registered
     *
     * @param msg the message to encode
     * @return the encoded message
     * @throws Exception exception thrown if the application is not registered or the message cannot be encoded for any reason
     */
    public byte[] encodeWithPubKey(final byte[] msg) throws Exception {
        try {
            // TODO 5: Encrypt message with public key
        } catch (final IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("Unexpected error encoding RSA message", e);
        }
    }

    /**
     * Decode the given message using our own private key
     *
     * @param msg the message to decode
     * @return the decoded message
     * @throws Exception exception thrown if there is any problem
     */
    public byte[] decodeWithOwnPrivKey(final byte[] msg) throws Exception {
        try {
            // TODO 6: Decrypt message with private key
        } catch (final IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("Unexpected error decoding RSA message", e);
        }
    }
}
