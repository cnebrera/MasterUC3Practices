package com.cenebrera.uc3.tech.lesso10.acceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

/**
 * Created by alexvarela on 19/12/16.
 */
public class QuickFixAcceptor implements Application
{
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(QuickFixAcceptor.class);
    
    
    
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
        LOGGER.debug("From app Session ID [{}], Message [{}]", sessionID, message);
    }
}
