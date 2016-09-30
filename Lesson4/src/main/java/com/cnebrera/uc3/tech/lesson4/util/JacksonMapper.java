package com.cnebrera.uc3.tech.lesson4.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ------------------------------------------------
 * @author Francisco Manuel Benitez Chico (XE30001)
 * ------------------------------------------------
 */
public class JacksonMapper extends ObjectMapper 
{
	/** Serial Version UID */
	private static final long serialVersionUID = 3753496109727081104L ;
	
	/** Private static instance */
	private static ObjectMapper INSTANCE = new JacksonMapper();

	public static ObjectMapper getInstance()
	{
		return INSTANCE ;
	}

	/**
	 * Private constructor
	 */
	private JacksonMapper()
	{
		this.setDefaultTyping(new FilteredTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL).init(JsonTypeInfo.Id.MINIMAL_CLASS, null)
																							  	   .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)) ;
		this.setSerializationInclusion(JsonInclude.Include.NON_NULL) ;
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) ;

		this.setConfig(getSerializationConfig().withView(JacksonViews.Lesson4View	.class)) ;
	}

	protected static class FilteredTypeResolverBuilder extends DefaultTypeResolverBuilder
	{
		/** Serial Version UID */
		private static final long serialVersionUID = -8145899696439248804L;

		/**
		 * @param defaultTyping with the default typing
		 */
		public FilteredTypeResolverBuilder(final DefaultTyping defaultTyping)
		{
			super(defaultTyping) ;
		}

		/**
		 * @param javaTypeSource with the java type
		 * @return true if it is an expected java type
		 */
		public boolean useForType(final JavaType javaTypeSource)
		{
			boolean use = super.useForType(javaTypeSource) ;
			if (use)
			{
					  use = !javaTypeSource.getRawClass().equals(PriceMessage.class) ;
			}
			
			return use;
		}
	}
}