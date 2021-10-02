package com.cnebrera.uc3.tech.lesson4.handlers.websocket;

import com.cnebrera.uc3.tech.lesson4.handlers.IPricesListener;
import com.cnebrera.uc3.tech.lesson4.util.PriceMessage;

import org.atmosphere.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Prices Listener for WebSocket
 * --------------------------------------
 *
 * @author Francisco Manuel Benitez Chico
 * --------------------------------------
 */
public class PricesListenerWebsocket implements IPricesListener {
    /**
     * Logger of the class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PricesListenerWebsocket.class);

    /**
     * Attribute - WebSocket instance
     */
    private final WebSocket webSocket;

    /**
     * Protected constructor
     *
     * @param webSocket with the web socket
     */
    protected PricesListenerWebsocket(final WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public void newPriceChange(final PriceMessage priceMessage) {
        try {
            if (this.webSocket.isOpen()) {
                this.webSocket.write(priceMessage.toString());
            }
        } catch (IOException ioException) {
            PricesListenerWebsocket.LOGGER.error("IOException while sending a message price as Websocket transport", ioException);
        }
    }
}
