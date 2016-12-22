package com.cenebrera.uc3.tech.lesso10.initiator;

import com.cenebrera.uc3.tech.lesso10.acceptor.AcceptorRunner;
import com.cenebrera.uc3.tech.lesso10.acceptor.QuickFixAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.ApplVerID;
import quickfix.fix42.MarketDataRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by alexvarela on 19/12/16.
 */
public class InitiatorRunner
{

    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(InitiatorRunner.class);
    
    public static void main(String[] args)
    {
        //Read the info from a xml and populate the class
        URL url = InitiatorRunner.class.getClassLoader().getResource("initiator.cfg");
        // FooApplication is your class that implements the Application interface
        Application application = new QuickFixInitiator();

        SessionSettings settings = null;
        try
        {
            settings = new SessionSettings(new FileInputStream(new File(url.toURI())));
        }
        catch (ConfigError configError)
        {
            LOGGER.error("Config Error ", configError);
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("File [{}] nto found", url,e);
        }
        catch (URISyntaxException e)
        {
            LOGGER.error("Uri bad format  url [{}]", url, e);
        }

        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        DefaultDataDictionaryProvider  dac = new DefaultDataDictionaryProvider();
        dac.getApplicationDataDictionary(new ApplVerID("9"));
        MessageFactory messageFactory = new DefaultMessageFactory();

        Initiator initiator = null;
        try
        {
            initiator = new SocketInitiator
                    (application, storeFactory, settings, logFactory, messageFactory);
            initiator.start();
        }
        catch (ConfigError configError)
        {
            configError.printStackTrace();
        }
        LOGGER.debug("Start initiator");

        try
        {
            Thread.sleep(6000);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("InterruptedException ", e);
        }


    }
}
