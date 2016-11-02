package com.cnebrera.uc3.tech.lesson8;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.cnebrera.uc3.tech.lesson8.jaxb.JAXBHandler;
import com.cnebrera.uc3.tech.lesson8.util.Constants;

/**
 * Launcher class - XSD Generator from JAXB Class
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class XSDGeneratorLauncher extends AbstractXMLLauncher
{
	/**
	 * @throws IOException   with an occurred exception
	 * @throws JAXBException with an occurred exception
	 */
	protected void generateXSDfromJAXBClass() throws IOException, JAXBException
	{
		// Create a new instance of the JAXB Handler
		final JAXBHandler jaxbHandler = new JAXBHandler() ;
		
		// Generate the schema content
		jaxbHandler.generateSchema(Constants.NAMESPACE_URI, Constants.SUGGESTED_FILE_NAME) ;
	}
	
	/**
	 * @param args 			 with the input arguments
	 * @throws IOException   with an occurred exception
	 * @throws JAXBException with an occurred exception
	 */
	public static void main(final String[] args) throws IOException, JAXBException
	{
		final XSDGeneratorLauncher xsdGeneratorLauncher = new XSDGeneratorLauncher() ;
		
		xsdGeneratorLauncher.generateXSDfromJAXBClass() ;
	}
}
