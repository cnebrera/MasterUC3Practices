package es.uc3m.fintech.lesson10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.MDReqID;
import quickfix.field.MsgType;
import quickfix.fix50sp2.MarketDataRequest;

/**
 * Acceptor Application - Business logic with TODOs
 * 
 * Part 1: Basic FIX connection and message logging
 * Part 2: Market data provider functionality
 * 
 * @author Mario Cao
 */
public class AcceptorApp extends MessageCracker implements Application {
    private final static Logger LOGGER = LoggerFactory.getLogger(AcceptorApp.class);
    private final MarketDataService marketDataService;

    public AcceptorApp(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    public void onCreate(SessionID sessionID) {
        LOGGER.debug("Session created: {}", sessionID);
    }

    public void onLogon(SessionID sessionID) {
        LOGGER.info("Client logged on: {}", sessionID);
    }

    public void onLogout(SessionID sessionID) {
        LOGGER.debug("Client logged out: {}", sessionID);
    }

    public void toAdmin(Message message, SessionID sessionID) {
        LOGGER.debug("Sending admin message: {}", message.getClass().getSimpleName());
    }

    public void fromAdmin(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        LOGGER.debug("Received admin message: {}", message.getClass().getSimpleName());
    }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        LOGGER.debug("Sending application message: {}", message.getClass().getSimpleName());
    }

    public void fromApp(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        // TODO 1.5 (Part 1): Log the message type when receiving any message
        // Hint: Use message.getHeader().getString(MsgType.FIELD)
        String msgType = null;

        LOGGER.info("Received message type: {}", msgType);

        // Route market data messages to specific handlers
        crack(message, sessionID);
    }

    /**
     * When a client requests market data, start sending price updates
     */
    public void onMessage(MarketDataRequest message, SessionID sessionID) {
        try {
            // TODO 2.2 (Part 2): Extract MDReqID value from the message
            String mdReqId = null;

            LOGGER.info("Received market data request: {}", mdReqId);

            // Start sending price updates
            this.marketDataService.start(sessionID, mdReqId);
        } catch (Exception e) {
            LOGGER.error("MDReqID not found in market data request", e);
        }
    }
}
