package com.cnebrera.uc3.tech.lesson8.util;

import java.io.File;

/**
 * Constants Class
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public final class Constants
{
	/** Constant - Java folder */
	public static final String JAVA_FOLDER		      			  = "src" + File.separator + "main" + File.separator + "java" ;
	/** Constant - Resources folder */
	public static final String RESOURCES_FOLDER	      			  = "src" + File.separator + "main" + File.separator + "resources" ;
	
	/** Constant - File input */
	public static final String XSD_FILE_INPUT	      			  = Constants.RESOURCES_FOLDER + File.separator + "practice8.xsd" ;
	/** Constant - Folder output */
	public static final String FOLDER_OUTPUT 	      			  = Constants.JAVA_FOLDER ;
	/** Constant - Package name output files */
	public static final String PACKAGE_NAME_OUTPUT    			  = "com.cnebrera.uc3.tech.lesson8.xjc" ;
	
	/** Constant - Valid XML - File Path */
	public static final String VALID_XML_FILE_PATH    			  = Constants.RESOURCES_FOLDER + File.separator + "valid.xml" ;
	/** Constant - Invalid XML - File Path */
	public static final String INVALID_XML_FILE_PATH  			  = Constants.RESOURCES_FOLDER + File.separator + "invalid.xml" ;
	
	/** Constant - Namespace URI */
	public static final String NAMESPACE_URI 		 			  = "practice8" ;
	/** Constant - Suggested File Name */
	public static final String SUGGESTED_FILE_NAME    			  = "schema1.xsd" ;
	
	/** Constant - Velocity template name */
	public static final String VELOCITY_TEMPLATE_NAME 			  = Constants.RESOURCES_FOLDER + File.separator + "velocityClassExample.vm" ;
	/** Constant - Velocity Class Generated - Package Name */
	public static final String VELOCITY_CLASS_GENERATED_PACKAGE   = "com.cnebrera.uc3.tech.lesson8.velocity" ;
	/** Constant - Velocity Class Generated - Class Name */
	public static final String VELOCITY_CLASS_GENERATED_CLASSNAME = "VelocityClassExample" ;
	
	/**
	 * Private constructor
	 */
	private Constants()
	{
		// Empty constructor
	}
}
