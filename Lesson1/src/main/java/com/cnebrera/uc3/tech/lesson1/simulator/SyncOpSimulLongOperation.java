package com.cnebrera.uc3.tech.lesson1.simulator;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simulates the execution of a long operation with loads of calculations
 */
public class SyncOpSimulLongOperation extends BaseSyncOpSimulator
{
    final LinkedHashMap<String, String> map = new LinkedHashMap<>();

    public SyncOpSimulLongOperation()
    {
        for (int i = 0; i < 1000; i++)
        {
            map.put("Key" + i, "Value" + (i + 1));
        }
    }

    public void executeOp()
    {
        final AtomicLong total = new AtomicLong();
        map.forEach((key, value) ->
        {
            if (key.endsWith("6"))
            {
                long lastValue = (long) value.charAt(value.length() - 1);
                total.addAndGet(lastValue);
            }
        });
    }
}
