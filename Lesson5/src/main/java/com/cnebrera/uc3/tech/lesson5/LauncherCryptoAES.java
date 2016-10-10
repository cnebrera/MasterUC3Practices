package com.cnebrera.uc3.tech.lesson5;

import com.cnebrera.uc3.tech.lesson5.util.Lesson5Exception;
import com.cnebrera.uc3.tech.lesson5.util.crypto.AESCrypto;

/**
 * Launcher class - Crypto - AES
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class LauncherCryptoAES
{
	/**
	 * @throws Lesson5Exception with an occurred exception
	 * 
	 */
	private void doExample() throws Lesson5Exception
	{
		// Message to encode/decode
		final String msg 	  	  = "Hello world!" ;
		
		// Printout the message
		System.out.println("Message to be encoded: " + msg) ;
		
		// Create a new instance of AESCrypto
		final AESCrypto aesCrypto = AESCrypto.createNewInstance() ;
		
		// Encode directly to the array
		final byte[] msgEncoded	  = aesCrypto.encode(msg.getBytes()) ;
		
		// Printout the encoded message
		System.out.println("Message encoded: " + new String(msgEncoded)) ;
		
		// Decode the encoded message
		final byte[] msgDecoded	  = aesCrypto.decode(msgEncoded) ;
		
		// Printout the decoded message
		System.out.println("Message decoded: " + new String(msgDecoded)) ;
	}
	
	/**
	 * @param args with the input arguments
	 * @throws Lesson5Exception with an occurred exception
	 */
	public static void main(String[] args) throws Lesson5Exception
	{
		final LauncherCryptoAES launcherCryptoAES = new LauncherCryptoAES() ;

		launcherCryptoAES.doExample() ;
	}
}
