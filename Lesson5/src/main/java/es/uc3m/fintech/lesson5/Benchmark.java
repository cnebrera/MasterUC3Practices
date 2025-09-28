package es.uc3m.fintech.lesson5;

import es.uc3m.fintech.lesson5.crypto.AESCrypto;
import es.uc3m.fintech.lesson5.crypto.RSACrypto;

/**
 * Interactive Crypto Performance Lab
 * 
 * Students will discover the performance differences between symmetric (AES) and asymmetric (RSA) encryption
 * through hands-on experimentation with different message sizes.
 * 
 * Learning Objectives:
 * - Understand why AES is faster than RSA
 * - Discover RSA's data size limitations
 * - Learn why hybrid encryption is used in practice
 * 
 * @author Enhanced for Lesson 5 - Interactive Learning
 */
public class Benchmark {
    
    /**
     * Test messages of different sizes to demonstrate performance differences
     * 
     * Challenge: Can you guess why RSA has a size limit?
     * Hint: Think about the mathematical operations involved...
     */
    private static final String[] TEST_MESSAGES = {
        "Hi",                                    // Small: 2 bytes
        "This is a medium length message",       // Medium: 28 bytes  
        "RSA limitation: It can only encrypt small amounts of data, which is why it's used for ____________?" // Large: 100 bytes (within RSA limit)
    };
    
    /**
     * Number of iterations to run for more accurate timing
     */
    private static final int ITERATIONS = 1000;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(50));
        System.out.println("CRYPTOGRAPHIC PERFORMANCE ANALYSIS");
        System.out.println("=".repeat(50));
        System.out.println("🔬 Algorithm Comparison: AES (Symmetric) vs RSA (Asymmetric)");
        System.out.println("🧪 Test Configuration: " + ITERATIONS + " iterations per measurement");
        System.out.println("🎯 Objective: Measure and compare encryption/decryption performance\n");
        
        try {
            // Test AES Performance
            testAESPerformance();
            
            // Test RSA Performance  
            testRSAPerformance();
            
        } catch (Exception e) {
            System.err.println("Error during performance test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test AES encryption performance
     */
    private static void testAESPerformance() throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("Phase 1: AES (Symmetric) Encryption Analysis");
        System.out.println("=".repeat(60));
        
        AESCrypto aesCrypto = AESCrypto.createNewInstance();
        
        for (int i = 0; i < TEST_MESSAGES.length; i++) {
            String message = TEST_MESSAGES[i];
            byte[] messageBytes = message.getBytes();
            
            System.out.println("\nMessage " + (i + 1) + " (" + (messageBytes.length + 1) + " bytes):");
            System.out.println("Content: \"" + (message.length() > 50 ? message.substring(0, 50) + "..." : message) + "\"");
            
            // Warm up
            aesCrypto.encode(messageBytes);
            aesCrypto.decode(aesCrypto.encode(messageBytes));
            
            // Measure encryption time
            long startTime = System.nanoTime();
            byte[] encrypted = null;
            for (int j = 0; j < ITERATIONS; j++) {
                encrypted = aesCrypto.encode(messageBytes);
            }
            long encryptionTime = System.nanoTime() - startTime;
            
            // Measure decryption time
            startTime = System.nanoTime();
            for (int j = 0; j < ITERATIONS; j++) {
                aesCrypto.decode(encrypted);
            }
            long decryptionTime = System.nanoTime() - startTime;
            
            // Calculate averages
            double avgEncryptionMs = (encryptionTime / 1_000_000.0) / ITERATIONS;
            double avgDecryptionMs = (decryptionTime / 1_000_000.0) / ITERATIONS;
            double totalTimeMs = avgEncryptionMs + avgDecryptionMs;
            
            System.out.printf("  🔒 Encryption: %.3f ms (avg over %d iterations)\n", avgEncryptionMs, ITERATIONS);
            System.out.printf("  🔓 Decryption: %.3f ms (avg over %d iterations)\n", avgDecryptionMs, ITERATIONS);
            System.out.printf("  📊 Total time: %.3f ms\n\n", totalTimeMs);
        }
    }
    
    /**
     * Test RSA encryption performance
     */
    private static void testRSAPerformance() throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("Phase 2: RSA (Asymmetric) Encryption Analysis");
        System.out.println("=".repeat(60));
        
        RSACrypto rsaCrypto = new RSACrypto();
        
        for (int i = 0; i < TEST_MESSAGES.length; i++) {
            String message = TEST_MESSAGES[i];
            byte[] messageBytes = message.getBytes();
            
            System.out.println("\nMessage " + (i + 1) + " (" + (messageBytes.length + 1) + " bytes):");
            System.out.println("Content: \"" + (message.length() > 50 ? message.substring(0, 50) + "..." : message) + "\"");
            
            // Warm up
            byte[] encrypted = rsaCrypto.encodeWithPubKey(messageBytes);
            rsaCrypto.decodeWithOwnPrivKey(encrypted);
            
            // Measure encryption time
            long startTime = System.nanoTime();
            encrypted = null;
            for (int j = 0; j < ITERATIONS; j++) {
                encrypted = rsaCrypto.encodeWithPubKey(messageBytes);
            }
            long encryptionTime = System.nanoTime() - startTime;
            
            // Measure decryption time
            startTime = System.nanoTime();
            for (int j = 0; j < ITERATIONS; j++) {
                rsaCrypto.decodeWithOwnPrivKey(encrypted);
            }
            long decryptionTime = System.nanoTime() - startTime;
            
            // Calculate averages
            double avgEncryptionMs = (encryptionTime / 1_000_000.0) / ITERATIONS;
            double avgDecryptionMs = (decryptionTime / 1_000_000.0) / ITERATIONS;
            double totalTimeMs = avgEncryptionMs + avgDecryptionMs;
            
            System.out.printf("  🔒 Encryption: %.3f ms (avg over %d iterations)\n", avgEncryptionMs, ITERATIONS);
            System.out.printf("  🔓 Decryption: %.3f ms (avg over %d iterations)\n", avgDecryptionMs, ITERATIONS);
            System.out.printf("  📊 Total time: %.3f ms\n", totalTimeMs);
        }
    }
}
