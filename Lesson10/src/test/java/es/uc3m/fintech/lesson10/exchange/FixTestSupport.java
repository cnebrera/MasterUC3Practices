package es.uc3m.fintech.lesson10.exchange;

import es.uc3m.fintech.lesson10.client.InitiatorApp;

import quickfix.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * Utilidades comunes para los tests: arranca acceptor e initiator en memoria
 * sobre un puerto libre.
 */
final class FixTestSupport {
    static final String BEGIN_STRING = "FIXT.1.1";
    static final String EXCHANGE = "UC3M_EXCHANGE";
    static final String CLIENT = "STUDENT";
    static final Map<String, String> USERS = Map.of("student", "uc3m-fix", "alice", "s3cr3t");
    static final SessionID ACCEPTOR_SESSION = new SessionID(BEGIN_STRING, EXCHANGE, CLIENT);

    private static DataDictionary transportDictionary;
    private static DataDictionary appDictionary;

    private FixTestSupport() {
    }

    /** Diccionario de la capa de sesión (FIXT.1.1). */
    static synchronized DataDictionary transportDictionary() throws ConfigError {
        if (transportDictionary == null) {
            transportDictionary = new DataDictionary("FIXT11.xml");
        }
        return transportDictionary;
    }

    /** Diccionario de la capa de aplicación (FIX 5.0 SP2). */
    static synchronized DataDictionary appDictionary() throws ConfigError {
        if (appDictionary == null) {
            appDictionary = new DataDictionary("FIX50SP2.xml");
        }
        return appDictionary;
    }

    /** Serializa, vuelve a parsear y valida un mensaje de aplicación contra FIXT.1.1 + FIX 5.0 SP2. */
    static Message parseAndValidate(Message message) throws Exception {
        ValidationSettings settings = new ValidationSettings();
        Message parsed = new Message(message.toString(), transportDictionary(), appDictionary(), settings, true);
        appDictionary().validate(parsed, true, settings);
        return parsed;
    }

    /** Motor de precios que no envía nada (publicación cada minuto). */
    static MarketDataService silentMarketData() {
        return new MarketDataService(60_000, 1) {
            @Override
            protected void send(Message message, SessionID sessionID) {
            }
        };
    }

    /** Acceptor que guarda los mensajes que envía en lugar de mandarlos por la red. */
    static final class CapturingAcceptor extends AcceptorApp {
        final List<Message> sent = java.util.Collections.synchronizedList(new ArrayList<>());

        CapturingAcceptor(MarketDataService marketData) {
            super(marketData, USERS);
        }

        @Override
        protected void send(Message message, SessionID sessionID) {
            sent.add(message);
        }

        List<Message> ofType(String msgType) {
            synchronized (sent) {
                return sent.stream().filter(m -> {
                    try {
                        return msgType.equals(m.getHeader().getString(quickfix.field.MsgType.FIELD));
                    } catch (FieldNotFound e) {
                        return false;
                    }
                }).toList();
            }
        }
    }

    /** Acceptor + initiator conectados por TCP en localhost. */
    static final class Exchange implements AutoCloseable {
        final MarketDataService marketData;
        final InitiatorApp client;
        private final SocketAcceptor acceptor;
        private final SocketInitiator initiator;

        Exchange(String username, String password) throws Exception {
            this(new InitiatorApp(username, password));
        }

        Exchange(InitiatorApp client) throws Exception {
            int port = freePort();
            this.client = client;
            this.marketData = new MarketDataService(100, 7);

            SessionSettings acceptorSettings = baseSettings("acceptor");
            SessionID acceptorId = ACCEPTOR_SESSION;
            acceptorSettings.setString(acceptorId, "SocketAcceptHost", "127.0.0.1");
            acceptorSettings.setLong(acceptorId, "SocketAcceptPort", port);
            touch(acceptorSettings, acceptorId);
            acceptor = new SocketAcceptor(new AcceptorApp(marketData, USERS), new MemoryStoreFactory(),
                    acceptorSettings, new SLF4JLogFactory(acceptorSettings), new DefaultMessageFactory());
            acceptor.start();

            SessionSettings initiatorSettings = baseSettings("initiator");
            SessionID initiatorId = new SessionID(BEGIN_STRING, CLIENT, EXCHANGE);
            initiatorSettings.setString(initiatorId, "SocketConnectHost", "127.0.0.1");
            initiatorSettings.setLong(initiatorId, "SocketConnectPort", port);
            initiatorSettings.setLong(initiatorId, "HeartBtInt", 30);
            initiatorSettings.setLong(initiatorId, "ReconnectInterval", 1);
            touch(initiatorSettings, initiatorId);
            initiator = new SocketInitiator(client, new MemoryStoreFactory(), initiatorSettings,
                    new SLF4JLogFactory(initiatorSettings), new DefaultMessageFactory());
            initiator.start();
        }

        @Override
        public void close() {
            initiator.stop(true);
            acceptor.stop(true);
            marketData.shutdown();
        }
    }

    private static SessionSettings baseSettings(String connectionType) {
        SessionSettings settings = new SessionSettings();
        settings.setString("ConnectionType", connectionType);
        settings.setString("BeginString", BEGIN_STRING);
        settings.setString("DefaultApplVerID", "FIX.5.0SP2");
        settings.setString("UseDataDictionary", "Y");
        settings.setString("TransportDataDictionary", "FIXT11.xml");
        settings.setString("AppDataDictionary", "FIX50SP2.xml");
        settings.setString("StartTime", "00:00:00");
        settings.setString("EndTime", "00:00:00");
        settings.setString("ResetOnLogon", "Y");
        settings.setString("SLF4JLogHeartbeats", "N");
        return settings;
    }

    private static void touch(SessionSettings settings, SessionID id) {
        settings.setString(id, "BeginString", id.getBeginString());
    }

    private static int freePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    /** Espera hasta que se cumpla la condición o venza el plazo. */
    static boolean eventually(long timeoutMs, BooleanSupplier condition) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (condition.getAsBoolean()) {
                return true;
            }
            Thread.sleep(50);
        }
        return condition.getAsBoolean();
    }
}
