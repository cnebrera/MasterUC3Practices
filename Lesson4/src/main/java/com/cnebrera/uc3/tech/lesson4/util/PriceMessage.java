package com.cnebrera.uc3.tech.lesson4.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Represents a new price message
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class PriceMessage
{
	/** Attribute - Market */
	@JsonView({JacksonViews.Lesson4View.class})
	private final String market ;
	/** Attribute - Product */
	@JsonView({JacksonViews.Lesson4View.class})
	private final String product ;
	/** Attribute - Instrument name */
	@JsonView({JacksonViews.Lesson4View.class})
	private final String instrumentName ;
	
	/** Attribute - Level */
	@JsonView({ JacksonViews.Lesson4View.class })
	private final int level;
	/** Attribute - Price */
	@JsonView({ JacksonViews.Lesson4View.class })
	private final double price;
	/** Attribute - Volume */
	@JsonView({ JacksonViews.Lesson4View.class })
	private final int volume;
	
	/** Attribute - Price was sent */
	private boolean priceWasSent ;

	/**
	 * @param market 		 with the market
	 * @param product 		 with the product
	 * @param instrumentName with the instrument
	 * @param level 		 with the level
	 * @param price 		 with the price
	 * @param volume 		 with the volume
	 */
	public PriceMessage(final String market, final String product, final String instrumentName, final int level, final double price, final int volume)
	{
		this.market 		= market ;
		this.product 		= product ;
		this.instrumentName = instrumentName ;

		this.level 			= level ;
		this.price 			= price ;
		this.volume 		= volume ;
		
		this.priceWasSent	= false ;
	}
	
	/**
	 * @return the market
	 */
	@JsonProperty("market")
	public String getMarket()
	{
		return this.market ;
	}

	/**
	 * @return the product
	 */
	@JsonProperty("product")
	public String getProduct()
	{
		return this.product ;
	}

	/**
	 * @return the instrument
	 */
	@JsonProperty("instrumentName")
	public String getInstrumentName()
	{
		return this.instrumentName ;
	}

	/**
	 * @return the level
	 */
	@JsonProperty("level")
	public int getLevel()
	{
		return this.level ;
	}

	/**
	 * @return the price
	 */
	@JsonProperty("price")
	public double getPrice()
	{
		return this.price ;
	}

	/**
	 * @return the volume
	 */
	@JsonProperty("volume")
	public int getVolume()
	{
		return this.volume ;
	}

	@Override
	public String toString()
	{
		try
		{
			return JacksonMapper.getInstance().writerWithDefaultPrettyPrinter().writeValueAsString(this) ;
		}
		catch (JsonProcessingException jsonProcessingExc)
		{
			return "Exception converting to JSON string: " + jsonProcessingExc.getMessage() ;
		}
	}

	/**
	 * @return true if the price was sent
	 */
	public boolean isPriceWasSent()
	{
		return this.priceWasSent ;
	}

	/**
	 * Reset the price
	 */
	public void priceWasSent()
	{
		this.priceWasSent = true ;
	}
}
