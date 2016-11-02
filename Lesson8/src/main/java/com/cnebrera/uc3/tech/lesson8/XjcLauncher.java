package com.cnebrera.uc3.tech.lesson8 ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cnebrera.uc3.tech.lesson8.util.Constants;

/**
 * Launcher class - XJC Launcher
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class XjcLauncher
{
	/**
	 * Generate Classes from XSD
	 * @throws IOException with an occurred exception
	 * @throws InterruptedException 
	 */
	private void generateClassesFromXsd()
	{
		try
		{
			// TODO 1
			
			// TODO 2
			
			// TODO 3
		}
	    catch (IOException ioException)
		{
	    	ioException.printStackTrace() ;	
		}
		catch (InterruptedException interruptedExc)
		{
			interruptedExc.printStackTrace() ;
		}
	}
	
	/**
	 * @param process with the process
	 * @throws IOException with an occurred exception
	 */
	private void printOutput(final Process process) throws IOException
	{
		final StringBuffer output   = new StringBuffer() ;
		final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())) ;

		String line 				= reader.readLine() ;

		while (line != null)
		{
			output.append(line + "\n") ;
			
			line = reader.readLine() ;
		}
		
		System.out.println(output.toString()) ;
	}

	/**
	 * @param args with the input arguments
	 */
	public static void main(final String[] args)
	{
		final XjcLauncher xjcLauncher = new XjcLauncher() ;
		
		xjcLauncher.generateClassesFromXsd() ;
	}
}
