package es.uc3m.fintech.lesson10.exchange;

import es.uc3m.fintech.lesson10.client.InitiatorApp;

import org.junit.jupiter.api.Test;
import quickfix.RejectLogon;
import quickfix.field.*;
import quickfix.fixt11.Logon;

import java.util.concurrent.TimeUnit;

import static es.uc3m.fintech.lesson10.exchange.FixTestSupport.*;
import static org.junit.jupiter.api.Assertions.*;

/** Parte 1: sesión FIX autenticada. */
class Part1SessionTest {

    @Test
    void logonOnlyWithValidCredentials() throws Exception {
        try (Exchange exchange = new Exchange("student", "uc3m-fix")) {
            assertTrue(exchange.client.awaitLogon(10, TimeUnit.SECONDS), "Logon should succeed");
        }
        try (Exchange exchange = new Exchange("student", "wrong")) {
            assertFalse(exchange.client.awaitLogon(3, TimeUnit.SECONDS), "Logon with a wrong password must fail");
        }
    }
}
