package es.uc3m.fintech.lesson10.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Motor de precios del UC3M Exchange.
 *
 * Genera precios sintéticos (random walk) y gestiona las suscripciones activas:
 * cada suscripción recibe periódicamente un MarketDataIncrementalRefresh (35=X)
 * con los símbolos que ha solicitado.
 *
 * @author Mario Cao
 */
public class MarketDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketDataService.class);

    private static final Map<String, Double> INITIAL_PRICES = Map.of(
            "TSLA", 400.0,
            "NVDA", 200.0,
            "AAPL", 230.0,
            "MSFT", 420.0,
            "AMZN", 190.0);

    private final long intervalMs;
    private final Random random;
    private final Map<String, Double> currentPrices = new ConcurrentHashMap<>(INITIAL_PRICES);
    private final Map<String, ScheduledFuture<?>> subscriptions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "MarketDataService");
        thread.setDaemon(true);
        return thread;
    });

    public MarketDataService() {
        this(1000, 42);
    }

    /** Permite a los tests publicar más rápido y con otra semilla. */
    MarketDataService(long intervalMs, long seed) {
        this.intervalMs = intervalMs;
        this.random = new Random(seed);
    }

    /** Símbolos que cotizan en el UC3M Exchange. */
    public Set<String> knownSymbols() {
        return new TreeSet<>(INITIAL_PRICES.keySet());
    }

    public boolean isKnownSymbol(String symbol) {
        return INITIAL_PRICES.containsKey(symbol);
    }

    /** Indica si existe una suscripción activa con ese MDReqID en esa sesión. */
    public boolean isActive(SessionID sessionID, String mdReqId) {
        return subscriptions.containsKey(key(sessionID, mdReqId));
    }

    /**
     * Da de alta una suscripción y empieza a enviar actualizaciones de precio
     * de los símbolos indicados.
     */
    public void subscribe(SessionID sessionID, String mdReqId, List<String> symbols) {
        List<String> requested = List.copyOf(symbols);
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(
                () -> publish(sessionID, mdReqId, requested), 0, intervalMs, TimeUnit.MILLISECONDS);
        ScheduledFuture<?> previous = subscriptions.put(key(sessionID, mdReqId), task);
        if (previous != null) {
            previous.cancel(false);
        }
        LOGGER.info("Subscription {} started for {}", mdReqId, requested);
    }

    /**
     * Cancela una suscripción.
     *
     * @return true si la suscripción existía
     */
    public boolean unsubscribe(SessionID sessionID, String mdReqId) {
        ScheduledFuture<?> task = subscriptions.remove(key(sessionID, mdReqId));
        if (task == null) {
            return false;
        }
        task.cancel(false);
        LOGGER.info("Subscription {} cancelled", mdReqId);
        return true;
    }

    /** Cancela todas las suscripciones de una sesión (por ejemplo, tras un Logout). */
    public void unsubscribeAll(SessionID sessionID) {
        String prefix = sessionID + "/";
        subscriptions.keySet().stream()
                .filter(k -> k.startsWith(prefix))
                .toList()
                .forEach(k -> subscriptions.remove(k).cancel(false));
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }

    /**
     * Construye el mensaje MarketDataIncrementalRefresh (35=X) para una suscripción.
     *
     * @param mdReqId identificador de la suscripción a la que responde el mensaje
     * @param prices  último precio negociado (Trade) de cada símbolo, en orden
     */
    public MarketDataIncrementalRefresh buildIncrementalRefresh(String mdReqId, Map<String, Double> prices) {
        // TODO 2.4: Construye el 35=X con una entrada por símbolo (ver "Rules of Engagement").
        throw new UnsupportedOperationException("TODO 2.4");
    }

    private void publish(SessionID sessionID, String mdReqId, List<String> symbols) {
        try {
            Map<String, Double> prices = new LinkedHashMap<>();
            for (String symbol : symbols) {
                prices.put(symbol, nextPrice(symbol));
            }
            send(buildIncrementalRefresh(mdReqId, prices), sessionID);
        } catch (Exception e) {
            LOGGER.error("Error publishing market data for {}: {}", mdReqId, e.toString());
        }
    }

    /** Envía un mensaje al cliente de la sesión indicada. */
    protected void send(Message message, SessionID sessionID) throws SessionNotFound {
        Session.sendToTarget(message, sessionID);
    }

    private double nextPrice(String symbol) {
        return currentPrices.compute(symbol, (s, price) -> {
            double next = price + (random.nextDouble() - 0.5) * price * 0.01;
            return Math.max(0.01, Math.round(next * 100.0) / 100.0);
        });
    }

    private static String key(SessionID sessionID, String mdReqId) {
        return sessionID + "/" + mdReqId;
    }
}
