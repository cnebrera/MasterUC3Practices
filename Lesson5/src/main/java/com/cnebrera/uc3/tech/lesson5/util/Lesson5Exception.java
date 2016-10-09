package com.cnebrera.uc3.tech.lesson5.util;

public class Lesson5Exception extends Exception
{
	/** Constant - Serial Version - UID */
	private static final long serialVersionUID = 1L ;

	/**
	 * Constructor of the class
	 *
	 * @param message message for the exception
	 */
	public Lesson5Exception(final String message)
	{
		super(message);
	}

	/**
	 * Constructor of the class
	 *
	 * @param cause throwable exception cause
	 */
	public Lesson5Exception(final Throwable cause)
	{
		super(cause);
	}

	/**
	 * Construct an exception given the exception message and the cause of the exception
	 *
	 * @param message exception message
	 * @param cause throwable cause of the exception
	 */
	public Lesson5Exception(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
