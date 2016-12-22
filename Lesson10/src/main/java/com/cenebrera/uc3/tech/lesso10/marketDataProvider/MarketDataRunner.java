package com.cenebrera.uc3.tech.lesso10.marketDataProvider;

import com.cenebrera.uc3.tech.lesso10.acceptor.AcceptorRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by alexvarela on 20/12/16.
 */
public class MarketDataRunner
{
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(MarketDataRunner.class);

    public static void main(String[] args)
    {
        //Read the info from a xml and populate the class
        URL url = AcceptorRunner.class.getClassLoader().getResource("acceptor.cfg");
        URL prices = AcceptorRunner.class.getClassLoader().getResource("BBVA.prices");

        // MarketDataProvider is your class that implements the Application interface
        PriceReader priceReader = new PriceReader(prices);
        try
        {
            priceReader.init();
        }
        catch (URISyntaxException e)
        {
            LOGGER.error("error ",e);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Application application = new MarketDataProvider(priceReader);

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

        DefaultMessageFactory messageFactory = new DefaultMessageFactory();


        Acceptor acceptor = null;
        try
        {
            acceptor = new SocketAcceptor
                    (application, storeFactory, settings, logFactory, messageFactory);
            acceptor.start();
        }
        catch (ConfigError configError)
        {
            configError.printStackTrace();
        }
        LOGGER.debug("Start MarketDataRunner");


    }
}
