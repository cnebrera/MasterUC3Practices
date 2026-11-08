package es.uc3m.fintech.lesson10.exchange;

import es.uc3m.fintech.lesson10.client.InitiatorApp;

import org.junit.jupiter.api.Test;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.*;
import quickfix.fix50sp2.MarketDataRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static es.uc3m.fintech.lesson10.exchange.FixTestSupport.*;
import static org.junit.jupiter.api.Assertions.*;

/** Parte 2: market data. */
class Part2MarketDataTest {

    @Test
    void marketDataRequestHasRequestedSymbols() throws Exception {
        MarketDataRequest request = InitiatorApp.buildMarketDataRequest("MD-1",
                SubscriptionRequestType.SNAPSHOT_UPDATES, List.of("TSLA", "NVDA"));
        assertEquals("MD-1", request.getString(MDReqID.FIELD));
        assertEquals(List.of("TSLA", "NVDA"), symbols(request, NoRelatedSym.FIELD));
    }

    @Test
    void endToEndPricesAreReceived() throws Exception {
        try (Exchange exchange = new Exchange("student", "uc3m-fix")) {
            assertTrue(exchange.client.awaitLogon(10, TimeUnit.SECONDS), "Logon should succeed");
            exchange.client.subscribe("MD-1", "TSLA", "NVDA");
            assertTrue(eventually(5000, () -> exchange.client.getLastPrice("TSLA") != null
                    && exchange.client.getLastPrice("NVDA") != null), "Prices for TSLA and NVDA expected");
        }
    }

    @Test
    void validateRequestDetectsUnknownSymbol() {
        AcceptorApp app = new AcceptorApp(silentMarketData(), USERS);
        assertEquals(MDReqRejReason.UNKNOWN_SYMBOL,
                app.validateRequest(ACCEPTOR_SESSION, "MD-9", '1', List.of("TSLA", "ACME")));
    }

    private static List<String> symbols(Message message, int groupTag) throws Exception {
        List<String> symbols = new ArrayList<>();
        for (Group group : message.getGroups(groupTag)) {
            symbols.add(group.getString(Symbol.FIELD));
        }
        return symbols;
    }
}
