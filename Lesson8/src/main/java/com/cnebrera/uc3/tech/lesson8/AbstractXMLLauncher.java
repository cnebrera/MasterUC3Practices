package com.cnebrera.uc3.tech.lesson8;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.cnebrera.uc3.tech.lesson8.jaxb.JAXBHandler;
import com.cnebrera.uc3.tech.lesson8.xjc.StudentLessons;

/**
 * Abstract Launcher class
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public abstract class AbstractXMLLauncher
{
	/**
	 * @param filePath		 with the file path
	 * @throws JAXBException with an occurred exception
	 * @throws SAXException  with an occurred exception
	 */
	protected void handleXML(final String filePath) throws JAXBException, SAXException
	{
		final File file = new File(filePath) ;
		
		// Create a new instance of the JAXB Handler
		JAXBHandler jaxbHandler 			= new JAXBHandler() ;
		
		// Firstly, convert the XML to Object
		final StudentLessons studentLessons = jaxbHandler.convertToObject(file) ;
		System.out.println(studentLessons) ;
		
		// Finally, convert the outcome object to xml again
		final String xmlContent 			= jaxbHandler.convertToXml(studentLessons) ;
		System.out.println(xmlContent) ;
	}
}
