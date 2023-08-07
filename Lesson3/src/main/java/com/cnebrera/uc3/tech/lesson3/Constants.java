package com.cnebrera.uc3.tech.lesson3;

import java.util.concurrent.TimeUnit;

public final class Constants {

    private Constants() {
        // restrict instantiation
    }

    static final int MAX_EXECUTIONS_PER_SECOND = 100;
    static final long EXPECTED_TIME_BETWEEN_CALLS = TimeUnit.SECONDS.toMillis(1) / MAX_EXECUTIONS_PER_SECOND;
    static final int NUM_MESSAGES = 10;
    static final String CHANNEL = "aeron:udp?endpoint=localhost:40123";
    static final int STREAM_ID = 1001;

}
