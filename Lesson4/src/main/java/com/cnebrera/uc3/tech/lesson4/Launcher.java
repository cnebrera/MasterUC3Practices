package com.cnebrera.uc3.tech.lesson4 ;

import java.io.File;

import org.atmosphere.container.Jetty9AsyncSupportWithWebSocket;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnebrera.uc3.tech.lesson4.handlers.PricesPublisher;
import com.cnebrera.uc3.tech.lesson4.util.Constants;

/**
 * Launcher class
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class Launcher 
{
	/** Logger of the class */
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class) ;
    
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
    		Launcher.LOGGER.warn("Setting the default sleep time {}", sleepTime) ;
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
        final HttpConfiguration http_config = new HttpConfiguration() ;
        http_config.addCustomizer(new ForwardedRequestCustomizer()) ;

        // Set the port and idle timeout
        final ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config)) ;
        http.setPort(8080) ;
        http.setIdleTimeout(30000) ;

        // Set the connectors
        server.setConnectors(new Connector[]{http}) ;

        // Set the resource handler
        final ResourceHandler resource_handler = new ResourceHandler() ;
        resource_handler.setDirectoriesListed(true) ;
        resource_handler.setWelcomeFiles(new String[]{ "index.html" }) ;

        // This project loads all resources from src/main/resources/webapp
        resource_handler.setResourceBase(new File("src/main/resources/webapp").getAbsolutePath()) ;

        // Generate a new instance of the ServletHolder -> AthmosphereServlet
        final ServletHolder atmosphereServletHolder = new ServletHolder(AtmosphereServlet.class);

        // Set initial parameters: annotation package, content type and WebSocket support

        // TODO 1
        
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
    	final Launcher launcher = new Launcher() ;
    	
    	// Process the input arguments
    	launcher.generateProcess(args) ;
    }
}
