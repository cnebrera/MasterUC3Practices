package com.cnebrera.uc3.tech.lesson5;

import com.cnebrera.uc3.tech.lesson5.util.Lesson5Exception;
import com.cnebrera.uc3.tech.lesson5.util.crypto.RSACrypto;

/**
 * Launcher class - Crypto - AES
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class LauncherCryptoRSA
{
	/**
	 * @throws Lesson5Exception with an occurred exception
	 * 
	 */
	private void doExample() throws Lesson5Exception
	{
		// Message to encode/decode
		final String msg 	  	   = "Hello world!" ;
		
		// Printout the message
		System.out.println("Message to be encoded: " + msg) ;
		
        // Create a new instance of RSACrypto
        final RSACrypto rsaCrypto  = new RSACrypto() ;
        
        final byte[] msgEncoded	   = rsaCrypto.encodeWithPubKey(msg.getBytes()) ;
        
		// Printout the encoded message
		System.out.println("Message encoded: " + new String(msgEncoded)) ;
		
		// Decode the encoded message
		final byte[] msgDecoded	   = rsaCrypto.decodeWithOwnPrivKey(msgEncoded) ;
		
		// Printout the decoded message
		System.out.println("Message decoded: " + new String(msgDecoded)) ;
	}
	
	/**
	 * @param args with the input arguments
	 * @throws Lesson5Exception with an occurred exception
	 */
	public static void main(String[] args) throws Lesson5Exception
	{
		final LauncherCryptoRSA launcherCryptoRSA = new LauncherCryptoRSA() ;

		launcherCryptoRSA.doExample() ;
	}
}
