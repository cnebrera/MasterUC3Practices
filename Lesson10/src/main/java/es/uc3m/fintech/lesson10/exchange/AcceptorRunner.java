package es.uc3m.fintech.lesson10.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import java.io.InputStream;
import java.util.Map;

/**
 * Arranca el UC3M Exchange (acceptor). No hay TODOs en esta clase.
 *
 * @author Mario Cao
 */
public class AcceptorRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptorRunner.class);

    /** Usuarios autorizados. En un sistema real vendrían de una base de datos. */
    static final Map<String, String> USERS = Map.of("student", "uc3m-fix");

    public static void main(String[] args) throws Exception {
        SessionSettings settings;
        try (InputStream cfg = AcceptorRunner.class.getClassLoader().getResourceAsStream("acceptor.cfg")) {
            settings = new SessionSettings(cfg);
        }

        MarketDataService marketDataService = new MarketDataService();
        Application application = new AcceptorApp(marketDataService, USERS);

        Acceptor acceptor = new SocketAcceptor(application, new FileStoreFactory(settings), settings,
                new FileLogFactory(settings), new DefaultMessageFactory());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            acceptor.stop();
            marketDataService.shutdown();
        }));

        acceptor.start();
        LOGGER.info("UC3M Exchange listening. Symbols: {}", marketDataService.knownSymbols());
        Thread.currentThread().join();
    }
}
