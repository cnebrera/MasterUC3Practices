package com.cnebrera.uc3.tech.lesson1.simulator;

import java.util.Random;

/**
 * Simulates an operation that takes a random number of time between the given values in nanoseconds
 */
public class SyncOpSimulRndPark extends BaseSyncOpSimulator
{
    final Random rnd = new Random();
    final long minParkTimeNanos;
    final long maxParkTimeNanos;

    public SyncOpSimulRndPark(long minParkTimeNanos, long maxParkTimeNanos)
    {
        this.minParkTimeNanos = minParkTimeNanos;
        this.maxParkTimeNanos = maxParkTimeNanos;
    }

    public void executeOp()
    {
        // Get a random park time
        final long newParkTime = Math.abs(rnd.nextLong() % maxParkTimeNanos) + minParkTimeNanos;

        // Park
        long startTime = System.nanoTime();

        while(System.nanoTime() - startTime < newParkTime);
    }
}
