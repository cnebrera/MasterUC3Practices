package com.cnebrera.uc3.tech.lesson4.handlers;

import com.cnebrera.uc3.tech.lesson4.util.PriceMessage;

/**
 * Interface Prices Listener
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public interface IPricesListener
{
	/**
	 * Add a new price change to the listener
	 * @param priceMessage with the price message
	 */
	void newPriceChange(final PriceMessage priceMessage) ;
}
