package com.cnebrera.uc3.tech.lesson4.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cnebrera.uc3.tech.lesson4.util.Constants;
import com.cnebrera.uc3.tech.lesson4.util.PriceMessage;

/**
 * Price simulator
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class PriceSimulator implements Runnable
{
	/** Attribute - Sleep time in milliseconds */
	private final long sleepTime ;
	/** Attribute - Random instance */
	private final Random randomInstance ;
	
	/** Event listener (websocket or long-polling) */
	private final List<IPricesListener> pricesListenerList ;
	
	/** Attribute - Object synchronized */
	private final Object object ;
	
	/** Is running the thread? */
	private volatile boolean isRunning ;
	
	/**
	 * Public constructor
	 * @param sleepTime with the sleep time
	 */
	public PriceSimulator(final long sleepTime)
	{
		this.sleepTime 			= sleepTime ;
		
		this.pricesListenerList = new ArrayList<IPricesListener>() ;
		this.object				= new Object() ;
		
		this.randomInstance 	= new Random() ;
		this.isRunning			= true ;
	}

	/**
	 * Run implementation
	 */
	public void run()
	{
		while(this.isRunning)
		{
			// Generate a new price message
			this.newPriceMessage();
			
			// Check if sleep is necessary
			if (this.sleepTime > 0)
			{
				try
				{
					Thread.sleep(this.sleepTime);
				}
				catch (InterruptedException interruptedExc)
				{
					interruptedExc.printStackTrace() ;
				}
			}
		}
	}
	
	/**
	 * @param pricesListener with a new prices listener
	 */
	public void addPricesListener(final IPricesListener pricesListener)
	{
		synchronized(this.object)
		{
			this.pricesListenerList.add(pricesListener) ;
		}
	}
	
	/**
	 * @param pricesListener with a prices listener to be removed
	 */
	public void removePricesListener(final IPricesListener pricesListener)
	{
		synchronized(this.object)
		{
			this.pricesListenerList.remove(pricesListener) ;
		}
	}
	
	public void stop()
	{
		this.isRunning = false ;
	}
	
	/**
	 * Generate a new price message
	 */
	private void newPriceMessage()
	{
		// Create a random price level change
		final int level 				= this.randomInstance.nextInt(Constants.INSTRUMENT_PRICE_LEVELS) ;
		final int newVolume 			= this.randomInstance.nextInt(Constants.VOLUME_CONSTANT) ;
		final double newPrice 			= this.randomInstance.nextDouble() * Constants.ONE_HUNDRED_PER ;
		
		// New instance of PriceMessage
		final PriceMessage priceMessage = new PriceMessage(Constants.PREFIX_INSTRUMENT_MARKET,
														   Constants.PREFIX_INSTRUMENT_PRODUCT,
														   Constants.PREFIX_INSTRUMENT_NAME,
														   level,
														   newPrice,
														   newVolume) ;
		// Send the change
		synchronized(this.object)
		{
			for (final IPricesListener pricesListener: this.pricesListenerList)
			{
				pricesListener.newPriceChange(priceMessage) ;
			}
		}
	}
}
