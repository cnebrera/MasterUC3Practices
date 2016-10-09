package com.cnebrera.uc3.tech.lesson5 ;

import java.io.File;

import org.atmosphere.container.Jetty9AsyncSupportWithWebSocket;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnebrera.uc3.tech.lesson5.handlers.PricesPublisher;
import com.cnebrera.uc3.tech.lesson5.util.Constants;

/**
 * Launcher class - Web (Websocket + LongPolling)
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class LauncherWeb 
{
	/** Logger of the class */
    private static final Logger LOGGER = LoggerFactory.getLogger(LauncherWeb.class) ;
    
    /**
     * 
     * @param args 		 with the input arguments
     * @throws Exception with an occurred exception
     */
	protected void generateProcess(final String[] args) throws Exception
	{
    	// Process the input arguments
    	final int sleepTime = this.processInputArguments(args) ;
    	
    	// Start the publisher
    	PricesPublisher.getInstance().start(sleepTime) ;
    	
    	// Launch the Jetty server
    	this.launchJettyServer() ;
	}
    
	/**
     * @param args with the input arguments
     * @return the sleep time
     */
    private int processInputArguments(final String[] args)
    {
    	int sleepTime = Constants.DEFAULT_SLEEP_TIME ;
    	if (args == null || args.length != 1)
		{
    		LauncherWeb.LOGGER.warn("Setting the default sleep time {}", sleepTime) ;
		}
    	else
    	{
    		sleepTime = Integer.valueOf(args[0]) ;
		}
			
		return sleepTime ;
    }
    
    /**
     * @throws Exception with an occurred exception while launching the server
     */
    private void launchJettyServer() throws Exception
    {
    	// New instance of Server
    	final Server server = new Server() ;

    	// Set the new HTTP Configuration if necessary
        final HttpConfiguration https_config = new HttpConfiguration() ;
        
    	// TODO 1
        
        // TODO 2

        // TODO 3

        // Set the connectors
        server.setConnectors(new Connector[]{https}) ;

        // Set the resource handler
        final ResourceHandler resource_handler = new ResourceHandler() ;
        resource_handler.setDirectoriesListed(true) ;
        resource_handler.setWelcomeFiles(new String[]{ "index.html" }) ;

        // This project loads all resources from src/main/resources/webapp
        resource_handler.setResourceBase(new File("src/main/resources/webapp").getAbsolutePath()) ;

        // Generate a new instance of the ServletHolder -> AthmosphereServlet
        final ServletHolder atmosphereServletHolder = new ServletHolder(AtmosphereServlet.class);

        // Set initial parameters: annotation package, content type and WebSocket support
        atmosphereServletHolder.setInitParameter(ApplicationConfig.ANNOTATION_PACKAGE, "com.cnebrera.uc3.tech.lesson5.handlers.long_polling," +
        																			   "com.cnebrera.uc3.tech.lesson5.handlers.websocket") ;
        atmosphereServletHolder.setInitParameter(ApplicationConfig.WEBSOCKET_CONTENT_TYPE, "application/json") ;
        atmosphereServletHolder.setInitParameter(ApplicationConfig.PROPERTY_COMET_SUPPORT, Jetty9AsyncSupportWithWebSocket.class.getName()) ;

        // Set the Async Support as true
        atmosphereServletHolder.setAsyncSupported(true) ;

        // Create a new instance of the Servlet Context Handler
        final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS) ;
        servletContextHandler.addServlet(atmosphereServletHolder, "/prices/*") ;

        // Create a new instance of the Handler List
        final HandlerList handlers = new HandlerList() ;
        handlers.setHandlers(new Handler[] { resource_handler, servletContextHandler }) ;

        // Set the handler
        server.setHandler(handlers) ;
        
        // Stop at shutdown as true
        server.setStopAtShutdown(true) ;
        
        // Start and join the server
        server.start() ;
        server.join() ;    	
    }
    
	/**
	 * @param args with the input arguments
	 * @throws Exception with an occurred exception
	 */
    public static void main(String[] args) throws Exception 
    {
    	// New instance of Launcher
    	final LauncherWeb launcher = new LauncherWeb() ;
    	
    	// Process the input arguments
    	launcher.generateProcess(args) ;
    }
}
