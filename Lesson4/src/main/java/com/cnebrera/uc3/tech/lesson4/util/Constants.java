package com.cnebrera.uc3.tech.lesson4.util;

/**
 * Constants class
 */
public class Constants
{
	/** Instrument - Suffix */
	public static final String SUFFIX_INSTRUMENT		 = "1" ; 
	/** Instrument - Prefix - Market */
	public static final String PREFIX_INSTRUMENT_MARKET  = "market_"     + Constants.SUFFIX_INSTRUMENT ;
	/** Instrument - Prefix - Product */
	public static final String PREFIX_INSTRUMENT_PRODUCT = "product_"    + Constants.SUFFIX_INSTRUMENT ;
	/** Instrument - Prefix - Name */
	public static final String PREFIX_INSTRUMENT_NAME	 = "instrument_" + Constants.SUFFIX_INSTRUMENT ;
	
	/** Launcher - Default Sleep time */
	public static final int DEFAULT_SLEEP_TIME	 	 	 = 1 ;

	/** Instrument - Price levels */
	public static final int INSTRUMENT_PRICE_LEVELS 	 = 3 ;
	
	/** Instrument - Volume constant */
	public static final int VOLUME_CONSTANT    		     = 1000 ;
	
	/** One hundred percent - Double constant */
	public static final double ONE_HUNDRED_PER 		     = 100.0d ;
	
	/**
	 * Private constructor
	 */
	private Constants()
	{
		// Empty constructor
	}
}
