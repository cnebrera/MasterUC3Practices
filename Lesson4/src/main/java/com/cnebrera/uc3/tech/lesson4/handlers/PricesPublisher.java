package com.cnebrera.uc3.tech.lesson4.handlers;

/**
 * Prices publisher
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public final class PricesPublisher
{
    /** Static reference and single instance for this class */
    private static final PricesPublisher INSTANCE = new PricesPublisher() ;
	
	/** Thread to simulate price generation */
	private Thread simulatorThread ;
	/** Simulator to generate random prices */
	private PriceSimulator simulator ;
	
	/**
	 * Private constructor
	 */
	private PricesPublisher()
	{
		// Empty constructor
	}
	
	/**
	 * @return the singleton for this class
	 */
	public static PricesPublisher getInstance()
	{
		return PricesPublisher.INSTANCE ;
	}
	
	/**
	 * Start the publisher
	 * @param sleepTime with the sleep time
	 */
	public void start(final int sleepTime)
	{
		// Start the message generation thread
		this.simulator  	 		= new PriceSimulator(sleepTime) ;
		this.simulatorThread 		= new Thread(this.simulator, "PriceGenerator") ;
		this.simulatorThread.start() ;
	}
	
	/**
	 * @param pricesListener with a new prices listener
	 */
	public void addPricesListener(final IPricesListener pricesListener)
	{
		this.simulator.addPricesListener(pricesListener) ;
	}
	
	/**
	 * @param pricesListener with a new prices listener
	 */
	public void removePricesListener(final IPricesListener pricesListener)
	{
		this.simulator.removePricesListener(pricesListener) ;
	}
	
	/**
	 * Stop the publisher
	 */
	public void stop()
	{
		this.simulator.stop() ;
	}
}
