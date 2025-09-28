package es.uc3m.fintech.lesson5.crypto;

import java.security.SecureRandom;

public class RandomMessageGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length the length of the generated string
     * @return the random string
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random byte array of the specified length.
     *
     * @param length the length of the byte array
     * @return the random byte array
     */
    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }
}
