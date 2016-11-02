package com.cnebrera.uc3.tech.lesson8.velocity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.cnebrera.uc3.tech.lesson8.util.Constants;

/**
 * Velocity Handler
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class VelocityHandler
{
	/**
	 * @param packageName with the package name
	 * @param className	  with the class name
	 * Generate the class from the velocity template
	 */
	public void generateClassFromVelocityTemplate(final String packageName, final String className)
	{
		// TODO 1
        
		// TODO 2
		
		// TODO 3
        
		// TODO 4
        
		// TODO 5
	}
	
	/**
	 * @return a new instance of the velocity engine
	 */
	private VelocityEngine initializeVelocityEngine()
	{
		final VelocityEngine velocityEngine = new VelocityEngine() ;

		// TODO 6
        
        return velocityEngine ;
	}
	
	/**
	 * @param packageName with the package name
	 * @param className	  with the class name
	 * @return the velocity context
	 */
	private VelocityContext setContextParameters(final String packageName, final String className)
	{
		final VelocityContext context = new VelocityContext() ;
		
		// TODO 7
        
        return context ;
	}
	
	/**
	 * @return the class imports
	 */
	private List<String> generateClassImports()
	{
		final List<String> classImports = new ArrayList<String>() ;
		
		// TODO 8
        
        return classImports ;
	}
	
	/**
	 * @return the string values
	 */
	private List<String> generateStringValues()
	{
        final List<String> stringValues = new ArrayList<String>() ;
        
		// TODO 9
        
        return stringValues ;
	}
	
	/**
	 * @param packageName with the package name
	 * @param className	  with the class name
	 * @param writer 	  with the writer
	 */
	private void storeFileInProject(final String packageName, final String className, final StringWriter writer)
	{
		final File outputFile = new File(Constants.JAVA_FOLDER 					  + File.separator +
										 packageName.replace(".", File.separator) + File.separator +  
										 className + ".java") ;
		FileWriter fileWriter = null ;
				
		try
		{
			fileWriter = new FileWriter(outputFile) ;
			fileWriter.write(writer.toString());
		}
		catch (IOException ioException)
		{
			throw new RuntimeException("IOException while storing the generated file: " + ioException.getMessage()) ;
		}
		finally
		{
			if (fileWriter != null)
			{
				try
				{
					fileWriter.close() ;
				}
				catch (IOException ioException)
				{
					throw new RuntimeException("IOException while closing the generated file: " + ioException.getMessage()) ;
				}
			}
		}
	}
}
