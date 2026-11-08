package es.uc3m.fintech.lesson10.client;

import es.uc3m.fintech.lesson10.codec.FixCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;
import quickfix.fixt11.Logon;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.MarketDataRequestReject;
import quickfix.fix50sp2.MessageCracker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Cliente de market data (lado initiator).
 *
 * Parte 1: autenticación en el Logon.
 * Parte 2: suscripción a precios y procesamiento de las respuestas.
 *
 * @author Mario Cao
 */
public class InitiatorApp extends MessageCracker implements Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitiatorApp.class);

    private final String username;
    private final String password;

    /** Último precio recibido por símbolo. */
    private final Map<String, Double> lastPrices = new ConcurrentHashMap<>();
    /** Motivo de rechazo, MDReqRejReason(281), por MDReqID. */
    private final Map<String, Character> rejections = new ConcurrentHashMap<>();
    private final AtomicLong updatesReceived = new AtomicLong();

    private volatile SessionID sessionID;
    private volatile boolean loggedOn;
    private volatile String lastLogoutText;
    private volatile CountDownLatch logonLatch = new CountDownLatch(1);

    public InitiatorApp(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ------------------------------------------------------------------
    // Ciclo de vida de la sesión
    // ------------------------------------------------------------------

    @Override
    public void onCreate(SessionID sessionID) {
        this.sessionID = sessionID;
        LOGGER.info("Session created: {}", sessionID);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        LOGGER.info("Logged on: {}", sessionID);
        loggedOn = true;
        logonLatch.countDown();
    }

    @Override
    public void onLogout(SessionID sessionID) {
        LOGGER.info("Logged out: {}", sessionID);
        loggedOn = false;
        logonLatch = new CountDownLatch(1);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        // TODO 1.1: El UC3M Exchange exige autenticación en el Logon (35=A).

        LOGGER.info("OUT admin {}", FixCodec.toReadable(message.toString()));
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        LOGGER.info("IN  admin {}", FixCodec.toReadable(message.toString()));

        String msgType = message.getHeader().getString(MsgType.FIELD);
        if (MsgType.LOGOUT.equals(msgType) && message.isSetField(Text.FIELD)) {
            lastLogoutText = message.getString(Text.FIELD);
            LOGGER.warn("Logout received: {}", lastLogoutText);
        } else if (MsgType.REJECT.equals(msgType)) {
            LOGGER.warn("Session level Reject (35=3): {}",
                    message.isSetField(Text.FIELD) ? message.getString(Text.FIELD) : "(no text)");
        }
    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        LOGGER.info("OUT app   {}", FixCodec.toReadable(message.toString()));
    }

    @Override
    public void fromApp(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        LOGGER.info("IN  app   {}", FixCodec.toReadable(message.toString()));
        crack(message, sessionID);
    }

    // ------------------------------------------------------------------
    // Market data
    // ------------------------------------------------------------------

    /**
     * Construye una petición de market data (35=V).
     *
     * @param mdReqId                 identificador de la suscripción
     * @param subscriptionRequestType SubscriptionRequestType(263): '1' alta, '2' baja
     * @param symbols                 símbolos solicitados
     */
    public static MarketDataRequest buildMarketDataRequest(String mdReqId, char subscriptionRequestType,
                                                           List<String> symbols) {
        // Campos obligatorios: MDReqID(262), SubscriptionRequestType(263) y MarketDepth(264).
        // MarketDepth = 1: solo se pide el mejor precio (top of book).
        MarketDataRequest request = new MarketDataRequest(
                new MDReqID(mdReqId),
                new SubscriptionRequestType(subscriptionRequestType),
                new MarketDepth(1));

        // MDUpdateType(265) = 1: las actualizaciones llegarán como incrementales (35=X).
        request.set(new MDUpdateType(MDUpdateType.INCREMENTAL_REFRESH));

        // Grupo NoMDEntryTypes(267): qué datos se piden. Una única entrada: Trade (último precio negociado).
        MarketDataRequest.NoMDEntryTypes entryType = new MarketDataRequest.NoMDEntryTypes();
        entryType.set(new MDEntryType(MDEntryType.TRADE));
        request.addGroup(entryType);

        // TODO 2.1: Añade el grupo NoRelatedSym(146) con una entrada por símbolo, en el orden recibido.

        return request;
    }

    @Override
    public void onMessage(MarketDataIncrementalRefresh message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        updatesReceived.incrementAndGet();

        // TODO 2.3a: Actualiza 'lastPrices' con los precios recibidos.
    }

    @Override
    public void onMessage(MarketDataRequestReject message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        // TODO 2.3b: Registra en 'rejections' el motivo del rechazo de la suscripción.
    }

    // ------------------------------------------------------------------
    // API usada por InitiatorRunner y por los tests. No modificar.
    // ------------------------------------------------------------------

    public void subscribe(String mdReqId, String... symbols) throws SessionNotFound {
        Session.sendToTarget(buildMarketDataRequest(mdReqId,
                SubscriptionRequestType.SNAPSHOT_UPDATES, List.of(symbols)), sessionID);
    }

    public void unsubscribe(String mdReqId, String... symbols) throws SessionNotFound {
        Session.sendToTarget(buildMarketDataRequest(mdReqId,
                SubscriptionRequestType.DISABLE_PREVIOUS_SNAPSHOT_UPDATE_REQUEST, List.of(symbols)), sessionID);
    }

    public boolean awaitLogon(long timeout, TimeUnit unit) throws InterruptedException {
        return logonLatch.await(timeout, unit);
    }

    public boolean isLoggedOn() {
        return loggedOn;
    }

    /** Text(58) del último Logout recibido, o null. */
    public String getLastLogoutText() {
        return lastLogoutText;
    }

    /** Último precio recibido para el símbolo, o null si no se ha recibido ninguno. */
    public Double getLastPrice(String symbol) {
        return lastPrices.get(symbol);
    }

    /** MDReqRejReason(281) con el que se rechazó la suscripción, o null. */
    public Character getRejectReason(String mdReqId) {
        return rejections.get(mdReqId);
    }

    /** Número de mensajes 35=X recibidos. */
    public long getUpdatesReceived() {
        return updatesReceived.get();
    }
}
