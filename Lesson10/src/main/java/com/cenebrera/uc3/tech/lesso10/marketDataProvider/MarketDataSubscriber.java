package com.cenebrera.uc3.tech.lesso10.marketDataProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.fix44.MarketDataIncrementalRefresh;

/**
 * Created by alexvarela on 20/12/16.
 */
public class MarketDataSubscriber extends quickfix.fix44.MessageCracker implements Application
{
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(MarketDataSubscriber.class);


    public void onCreate(SessionID sessionID)
    {
        LOGGER.debug("On Create Session ID [{}]", sessionID);
    }

    public void onLogon(SessionID sessionID)
    {
        LOGGER.debug("On Logon Session ID [{}]", sessionID);
    }

    public void onLogout(SessionID sessionID)
    {
        LOGGER.debug("On Logout Session ID [{}]", sessionID);
    }

    public void toAdmin(Message message, SessionID sessionID)
    {
        LOGGER.debug("To admin Session ID [{}], Message [{}]", sessionID, message);
    }

    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon
    {
        LOGGER.debug("From admin Session ID [{}], Message [{}]", sessionID, message);
    }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend
    {
        LOGGER.debug("To app Session ID [{}], Message [{}]", sessionID, message);
    }

    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType
    {
        crack44((quickfix.fix44.Message) message, sessionID);
    }

    @Override
    public void onMessage(MarketDataIncrementalRefresh message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue
    {
        // TODO receive market data incremental
    }
}
