package es.uc3m.fintech.lesson10.exchange;

import es.uc3m.fintech.lesson10.codec.FixCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.MarketDataRequestReject;
import quickfix.fix50sp2.MessageCracker;
import quickfix.fixt11.Logon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * UC3M Exchange - Market Data Gateway (lado acceptor / servidor).
 *
 * Parte 1: autenticación de las sesiones entrantes.
 * Parte 2: gestión de las peticiones de market data (35=V).
 *
 * @author Mario Cao
 */
public class AcceptorApp extends MessageCracker implements Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptorApp.class);

    private final MarketDataService marketDataService;
    private final Map<String, String> users;

    /**
     * @param marketDataService motor de precios
     * @param users             usuarios autorizados: Username -> Password
     */
    public AcceptorApp(MarketDataService marketDataService, Map<String, String> users) {
        this.marketDataService = marketDataService;
        this.users = Map.copyOf(users);
    }

    @Override
    public void onCreate(SessionID sessionID) {
        LOGGER.info("Session created: {}", sessionID);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        LOGGER.info("Client logged on: {}", sessionID);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        LOGGER.info("Client logged out: {}", sessionID);
        marketDataService.unsubscribeAll(sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        LOGGER.info("OUT admin {}", FixCodec.toReadable(message.toString()));
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        LOGGER.info("IN  admin {}", FixCodec.toReadable(message.toString()));

        // TODO 1.2: Solo se aceptan Logons (35=A) con un Username(553)/Password(554) válido
        //           según el mapa 'users'. Cualquier otro Logon debe rechazarse.
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

    @Override
    public void onMessage(MarketDataRequest message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        String mdReqId = message.getMDReqID().getValue();
        char requestType = message.getSubscriptionRequestType().getValue();

        // Lectura del grupo repetido NoRelatedSym(146): una entrada (Group) por símbolo.
        List<String> symbols = new ArrayList<>();
        for (Group entry : message.getGroups(NoRelatedSym.FIELD)) {
            symbols.add(entry.getString(Symbol.FIELD));
        }

        // Baja: se cancela la suscripción con ese MDReqID.
        if (requestType == SubscriptionRequestType.DISABLE_PREVIOUS_SNAPSHOT_UPDATE_REQUEST) {
            marketDataService.unsubscribe(sessionID, mdReqId);
            return;
        }

        // Cualquier otra petición: se valida y se activa o se rechaza.
        Character reason = validateRequest(sessionID, mdReqId, requestType, symbols);
        if (reason == null) {
            marketDataService.subscribe(sessionID, mdReqId, symbols);
        } else {
            send(buildReject(mdReqId, reason, rejectText(reason, mdReqId, requestType, symbols)), sessionID);
        }
    }

    /**
     * Aplica las reglas del exchange a una petición de alta.
     *
     * @return el MDReqRejReason(281) con el que hay que rechazar la petición, o null si es válida
     */
    Character validateRequest(SessionID sessionID, String mdReqId, char requestType, List<String> symbols) {
        // TODO 2.2a: Aplica la tabla de comportamiento del exchange (ver "Rules of Engagement").
        throw new UnsupportedOperationException("TODO 2.2a");
    }

    /**
     * Construye el MarketDataRequestReject (35=Y) de una petición.
     *
     * @param mdReqId identificador de la petición rechazada
     * @param reason  MDReqRejReason(281)
     * @param text    explicación legible para Text(58)
     */
    MarketDataRequestReject buildReject(String mdReqId, char reason, String text) {
        // TODO 2.2b: Construye el 35=Y (ver "Rules of Engagement").
        throw new UnsupportedOperationException("TODO 2.2b");
    }

    private String rejectText(char reason, String mdReqId, char requestType, List<String> symbols) {
        return switch (reason) {
            case MDReqRejReason.UNKNOWN_SYMBOL -> "Unknown symbol " + symbols.stream()
                    .filter(s -> !marketDataService.isKnownSymbol(s)).findFirst().orElse("");
            case MDReqRejReason.DUPLICATE_MDREQID -> "Duplicate MDReqID " + mdReqId;
            case MDReqRejReason.UNSUPPORTED_SUBSCRIPTIONREQUESTTYPE ->
                    "Unsupported SubscriptionRequestType " + requestType;
            default -> "Request rejected";
        };
    }

    /** Envía un mensaje al cliente de la sesión indicada. */
    protected void send(Message message, SessionID sessionID) {
        try {
            Session.sendToTarget(message, sessionID);
        } catch (SessionNotFound e) {
            LOGGER.error("Session not found: {}", sessionID, e);
        }
    }
}
