package es.uc3m.fintech.lesson10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

/**
 * Initiator Runner - Infrastructure setup (no TODOs here)
 * This class sets up and starts the FIX initiator
 * 
 * @author Mario Cao
 */
public class InitiatorRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(InitiatorRunner.class);
    private static SessionSettings settings;

    public static void main(String[] args) {
        // Load configuration
        // TODO 1.3 (Part 1): Get URL to initiator configuration file (initiator.cfg)
        // Hint: See TODO 1.1 - same approach, different filename
        URL initiatorConfig = null;

        // Create the application (where the business logic and TODOs are)
        Application application = new InitiatorApp();

        try {
            // Setup QuickFIX session
            // TODO 1.4 (Part 1): Create SessionSettings from configuration file
            // Hint: See TODO 1.2 - same approach, use initiatorConfig
        } catch (Exception e) {
            LOGGER.error("Error loading initiator configuration", e);
            return;
        }

        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings); // Simpler for demos
        MessageFactory messageFactory = new DefaultMessageFactory();

        // Start the initiator
        Initiator initiator = null;
        try {
            initiator = new SocketInitiator(
                    application, storeFactory, settings, logFactory, messageFactory);
            initiator.start();
            LOGGER.info("Initiator started successfully");

            // Keep running until interrupted
            Thread.currentThread().join();
        } catch (ConfigError | InterruptedException e) {
            LOGGER.error("Error starting initiator", e);
        } finally {
            if (initiator != null) {
                initiator.stop(true);
            }
        }
    }

    public static SessionSettings getSettings() {
        return settings;
    }
}
