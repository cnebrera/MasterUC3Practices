package es.uc3m.fintech.lesson4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import es.uc3m.fintech.lesson4.service.PricesPublisher;
import es.uc3m.fintech.lesson4.util.Constants;

/**
 * Spring Boot Application.
 *
 * @author Mario Cao
 */
// TODO 1: Enable Spring Boot auto-configuration
// HINT: Use @SpringBootApplication to enable auto-configuration, component scanning, and configuration properties
public class Application {
    /**
     * Logger of the class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    /**
     * Prices publisher instance.
     */
    // TODO 2: Inject the PricesPublisher service
    // HINT: Use @Autowired to let Spring inject the PricesPublisher bean
    // This demonstrates dependency injection - Spring manages the object lifecycle
    private PricesPublisher pricesPublisher;

    /**
     * Process command line arguments and start the price publisher.
     *
     * @param args command line arguments
     * @return the sleep time for price updates
     */
    private static int processInputArguments(final String[] args) {
        int sleepTime = Constants.DEFAULT_SLEEP_TIME;
        if (args == null || args.length != 1) {
            // Use default sleep time
            LOGGER.info("Using default sleep time: {} ms", Constants.DEFAULT_SLEEP_TIME);
        } else {
            sleepTime = Integer.valueOf(args[0]);
        }
        return sleepTime;
    }

    /**
     * Start the price publisher when the application is ready.
     * 
     * @param event ApplicationReadyEvent
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(final ApplicationReadyEvent event) {
        // Get sleep time from system properties or use default
        String appArgs = System.getProperty("app.args", "");
        String[] args = appArgs.isEmpty() ? null : appArgs.split(" ");
        int sleepTime = processInputArguments(args);

        // TODO 3: Start the price publishing service
        // HINT: Call pricesPublisher.start() to begin generating and publishing price updates
        // This connects the main application to the real-time messaging system
        
        String message = String.format("🚀 App started with sleep time %d ms", sleepTime);
        int width = Math.max(Constants.APP_MSG_MIN_WIDTH, message.length() + Constants.APP_MSG_BORDER_PADDING * 2);
        String topBorder = "╔" + "═".repeat(width - Constants.APP_MSG_BORDER_PADDING) + "╗";
        String bottomBorder = "╚" + "═".repeat(width - Constants.APP_MSG_BORDER_PADDING) + "╝";
        String middleLine = String.format("║ %-" + (width - Constants.APP_MSG_BORDER_PADDING * 2) + "s ║", message);

        LOGGER.info("\n{}\n{}\n{}\n", topBorder, middleLine, bottomBorder);
    }

    /**
     * Main method to start the Spring Boot application.
     * 
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        // Store args in system properties so they can be accessed later
        if (args != null && args.length > 0) {
            System.setProperty("app.args", String.join(" ", args));
        }

        // TODO 4: Bootstrap the Spring Boot application
        // HINT: Use SpringApplication.run() to start the Spring Boot application
        // This initializes the Spring context, starts the web server, and serves static files
    }
}
