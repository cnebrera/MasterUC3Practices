package com.cnebrera.uc3.tech.lesson5.handlers.long_polling;

import java.io.IOException;

import org.atmosphere.cpr.AtmosphereResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnebrera.uc3.tech.lesson5.handlers.IPricesListener;
import com.cnebrera.uc3.tech.lesson5.util.PriceMessage;

/**
 * Prices Listener for Long Polling
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class PricesListenerLongPolling implements IPricesListener
{
	/** Logger of the class */
    private static final Logger LOGGER = LoggerFactory.getLogger(PricesListenerLongPolling.class) ;
    
    /** Pending price to be sent */
    private PriceMessage pendingPricesMessage ;
    
	/**
	 * Protected constructor
	 */
    protected PricesListenerLongPolling()
    {
    	super() ;
    }
    
    @Override
    public void newPriceChange(final PriceMessage priceMessage)
    {
    	this.pendingPricesMessage = priceMessage ;
    }

	/**
     * @param atmosphereResponse with the Atmosphere response
     */
	protected void sendPendingPricesChange(final AtmosphereResponse atmosphereResponse)
	{
		try
		{
			if (this.pendingPricesMessage != null)
			{
				// Verify if the price was not sent previously
				if (!this.pendingPricesMessage.isPriceWasSent())
				{
					atmosphereResponse.getWriter().write(this.pendingPricesMessage.toString()) ;
				}
					
				// This price was sent
				this.pendingPricesMessage.priceWasSent() ;
			}
		}
		catch (IOException ioException)
		{
			PricesListenerLongPolling.LOGGER.error("IOException while sending the pending prices as Long-Polling transport", ioException) ;
		}
	}
}
