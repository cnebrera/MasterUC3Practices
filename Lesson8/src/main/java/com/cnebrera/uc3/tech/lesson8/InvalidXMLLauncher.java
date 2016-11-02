package com.cnebrera.uc3.tech.lesson8;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.cnebrera.uc3.tech.lesson8.util.Constants;

/**
 * Launcher class - Invalid XML
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class InvalidXMLLauncher extends AbstractXMLLauncher
{
	/**
	 * @param args 			 with the input arguments
	 * @throws JAXBException with an occurred exception
	 * @throws SAXException  with an occurred exception
	 */
	public static void main(final String[] args) throws JAXBException, SAXException
	{
		final AbstractXMLLauncher validInstanceLauncher = new InvalidXMLLauncher() ;
		
		validInstanceLauncher.handleXML(Constants.INVALID_XML_FILE_PATH) ;
	}
}
