package es.uc3m.fintech.lesson10.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Arranca el cliente de market data (initiator) y ejecuta un pequeño guion
 * de demostración. No hay TODOs en esta clase.
 *
 * @author Mario Cao
 */
public class InitiatorRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitiatorRunner.class);

    public static void main(String[] args) throws Exception {
        SessionSettings settings;
        try (InputStream cfg = InitiatorRunner.class.getClassLoader().getResourceAsStream("initiator.cfg")) {
            settings = new SessionSettings(cfg);
        }

        InitiatorApp application = new InitiatorApp("student", "uc3m-fix");
        Initiator initiator = new SocketInitiator(application, new FileStoreFactory(settings), settings,
                new FileLogFactory(settings), new DefaultMessageFactory());
        initiator.start();

        try {
            if (!application.awaitLogon(15, TimeUnit.SECONDS)) {
                LOGGER.error("Could not log on to the UC3M Exchange (is the acceptor running?)");
                return;
            }

            // 1. Suscripción válida
            application.subscribe("MD-1", "TSLA", "NVDA");
            ticker(application, 5);

            // 2. Suscripciones que el exchange debe rechazar
            application.subscribe("MD-2", "ACME");
            application.subscribe("MD-1", "AAPL");
            TimeUnit.SECONDS.sleep(1);
            LOGGER.info("MD-2 reject reason: {} | duplicated MD-1 reject reason: {}",
                    application.getRejectReason("MD-2"), application.getRejectReason("MD-1"));

            // 3. Baja de la suscripción: los precios dejan de llegar
            application.unsubscribe("MD-1", "TSLA", "NVDA");
            TimeUnit.SECONDS.sleep(1);
            long before = application.getUpdatesReceived();
            TimeUnit.SECONDS.sleep(3);
            LOGGER.info("Updates received after unsubscribe: {}", application.getUpdatesReceived() - before);
        } finally {
            initiator.stop();
        }
    }

    private static void ticker(InitiatorApp application, int seconds) throws InterruptedException {
        for (int i = 0; i < seconds; i++) {
            TimeUnit.SECONDS.sleep(1);
            LOGGER.info("TICKER  TSLA {}  |  NVDA {}",
                    format(application.getLastPrice("TSLA")), format(application.getLastPrice("NVDA")));
        }
    }

    private static String format(Double price) {
        return price == null ? "  -   " : String.format("%7.2f", price);
    }
}
