package es.uc3m.fintech.lesson10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

/**
 * Acceptor Runner - Infrastructure setup (no TODOs here)
 * This class sets up and starts the FIX acceptor
 * 
 * @author Mario Cao
 */
public class AcceptorRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(AcceptorRunner.class);
    private static SessionSettings settings;

    public static void main(String[] args) {
        // Load configuration
        // TODO 1.1 (Part 1): Get URL to acceptor configuration file (acceptor.cfg)
        // Hint: Use getClassLoader().getResource() to locate the config file
        URL acceptorConfig = null;

        // Initialize market data service for Part 2
        MarketDataService marketDataService = new MarketDataService();
        marketDataService.init();

        // Create the application (where the business logic and TODOs are)
        Application application = new AcceptorApp(marketDataService);

        try {
            // Setup QuickFIX session
            // TODO 1.2 (Part 1): Create SessionSettings from configuration file
            // Hint: Convert the acceptorConfig URL to a File, then create a FileInputStream
            //       and pass it to the SessionSettings constructor
            //       Steps: URL -> toURI() -> File -> FileInputStream -> SessionSettings
        } catch (Exception e) {
            LOGGER.error("Error loading acceptor configuration", e);
            return;
        }

        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        // Start the acceptor
        Acceptor acceptor = null;
        try {
            acceptor = new SocketAcceptor(
                    application, storeFactory, settings, logFactory, messageFactory);
            acceptor.start();
            LOGGER.info("Acceptor started successfully");

            // Keep running until interrupted
            Thread.currentThread().join();
        } catch (ConfigError | InterruptedException e) {
            LOGGER.error("Error starting acceptor", e);
        } finally {
            if (acceptor != null) {
                acceptor.stop(true);
            }
        }
    }

    public static SessionSettings getSettings() {
        return settings;
    }
}
