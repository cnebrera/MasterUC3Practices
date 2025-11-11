package es.uc3m.fintech.lesson10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.MessageCracker;
import quickfix.fix50sp2.component.Instrument;

/**
 * Initiator Application - Business logic with TODOs
 * 
 * Part 1: Basic FIX connection and message logging
 * Part 2: Market data subscriber functionality
 * 
 * @author Mario Cao
 */
public class InitiatorApp extends MessageCracker implements Application {
    private final static Logger LOGGER = LoggerFactory.getLogger(InitiatorApp.class);
    private int receivedUpdates = 0;

    public void onCreate(SessionID sessionID) {
        LOGGER.debug("Session created: {}", sessionID);
    }

    public void onLogon(SessionID sessionID) {
        LOGGER.info("Successfully connected to server: {}", sessionID);

        try {
            // Wait a bit to ensure session is fully established
            Thread.sleep(2000);

            // Create market data request
            MarketDataRequest request = createMarketDataRequest();

            // Send to acceptor
            Session.sendToTarget(request, sessionID);
            LOGGER.info("Market data request sent for TSLA and NVDA");
        } catch (Exception e) {
            LOGGER.error("Error sending market data request", e);
        }
    }

    public void onLogout(SessionID sessionID) {
        LOGGER.debug("Disconnected from server: {}", sessionID);
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
        // TODO 1.6 (Part 1): Log received message type
        //Hint: Use message.getHeader().getString(MsgType.FIELD)
        String msgType = null;

        LOGGER.info("Received message type: {}", msgType);

        // Route market data messages to specific handlers
        crack((quickfix.fix50sp2.Message) message, sessionID);
    }

    /**
     * 
     * This method constructs the request message with all required fields
     */
    private MarketDataRequest createMarketDataRequest() {
        MarketDataRequest request = new MarketDataRequest();

        // Set required fields
        request.set(new MDReqID("TECH_PORTFOLIO_1"));
        request.set(new SubscriptionRequestType('1')); // Snapshot + updates
        request.set(new MarketDepth(0)); // Full book

        // Add MD Entry Types group (what type of data we want)
        MarketDataRequest.NoMDEntryTypes entryTypes = new MarketDataRequest.NoMDEntryTypes();
        entryTypes.set(new MDEntryType('2')); // Trade
        request.addGroup(entryTypes);
        request.set(new NoMDEntryTypes(1)); // Count of entry types

        // Add Related Symbols group (which symbols we want)
        request.set(new NoRelatedSym(2)); // We're requesting 2 symbols

        // TODO 2.1 (Part 2): Add TSLA and NVDA symbols to your market data request
        //
        // You need to add the symbols you want to receive market data for.
        // For this exercise, you must use "TSLA" and "NVDA" symbols.
        //
        // Steps to add each symbol:
        // 1. Create a MarketDataRequest.NoRelatedSym group for the symbol
        // 2. Create an Instrument object
        // 3. Set the Symbol field of the instrument
        // 4. Set the instrument to the NoRelatedSym group
        // 5. Add the group to the request
        //
        // Your code here - add both TSLA and NVDA:

        return request;
    }

    /**
     * Extract and log the price from incremental refresh messages
     */
    @Override
    public void onMessage(MarketDataIncrementalRefresh message, SessionID sessionID)
            throws FieldNotFound {
        int numEntries = message.getGroupCount(NoMDEntries.FIELD);
        LOGGER.info("Received market data update with {} entries", numEntries);

        // Process all entries in the message
        for (int i = 1; i <= numEntries; i++) {
            MarketDataIncrementalRefresh.NoMDEntries entries = new MarketDataIncrementalRefresh.NoMDEntries();
            message.getGroup(i, entries);

            // TODO 2.4 (Part 2): Extract price (MDEntryPx) and symbol (Symbol) from the entries group
            // Hint: Use entries.get() with field objects and getValue() to extract the actual values
            double price = 0;
            String symbol = null;

            char entryType = entries.get(new MDEntryType()).getValue();
            char updateAction = entries.get(new MDUpdateAction()).getValue();

            // Log each symbol update
            LOGGER.info("📈 Market Data Update - Symbol: {}, Price: {}, Type: {}, Action: {}",
                    symbol, price, entryType, updateAction);

            // Count received updates for logging
            receivedUpdates++;
        }

        // Log summary every 10 updates
        if (receivedUpdates % 10 == 0) {
            LOGGER.info("Received {} total market data updates", receivedUpdates);
        }
    }
}
