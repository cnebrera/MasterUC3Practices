package com.cnebrera.uc3.tech.lesson4.handlers.websocket ;

import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.util.SimpleBroadcaster;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketEventListenerAdapter;
import org.atmosphere.websocket.WebSocketStreamingHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnebrera.uc3.tech.lesson4.handlers.IPricesListener;
import com.cnebrera.uc3.tech.lesson4.handlers.PricesPublisher;

/**
 * Atmosphere handler for Web Sockets
 * --------------------------------------
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */

// TODO 1
public class WebSocketHandler extends WebSocketStreamingHandlerAdapter
{
	/** Logger for this class */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class) ;
	
	/** Attribute - Prices Listener - Web sockets */
	private IPricesListener pricesListener ;
	
	@Override
    public void onOpen(final WebSocket webSocket)
	{
		// TODO 2
		
		// TODO 3
		
    	// Add event listener when disconnecting
        webSocket.resource().addEventListener(new WebSocketEventListenerAdapter()
        {
            @Override
            public void onDisconnect(AtmosphereResourceEvent event)
            {
            	// TODO 4
            	
            	// TODO 5
            }
        });
    }
}
