package com.cnebrera.uc3.tech.lesson8;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.cnebrera.uc3.tech.lesson8.util.Constants;
import com.cnebrera.uc3.tech.lesson8.velocity.VelocityHandler;

/**
 * Launcher class - XSD Generator from JAXB Class
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class VelocityGeneratorLauncher extends AbstractXMLLauncher
{
	/**
	 * @throws IOException   with an occurred exception
	 * @throws JAXBException with an occurred exception
	 */
	protected void generateClassfromVelocity() throws IOException, JAXBException
	{
		// TODO 1
		
		// TODO 2
	}
	
	/**
	 * @param args 			 with the input arguments
	 * @throws IOException   with an occurred exception
	 * @throws JAXBException with an occurred exception
	 */
	public static void main(final String[] args) throws IOException, JAXBException
	{
		final VelocityGeneratorLauncher velocityGeneratorLauncher = new VelocityGeneratorLauncher() ;
		
		velocityGeneratorLauncher.generateClassfromVelocity() ;
	}
}
