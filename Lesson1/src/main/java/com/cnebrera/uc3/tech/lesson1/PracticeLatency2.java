package com.cnebrera.uc3.tech.lesson1;

import com.cnebrera.uc3.tech.lesson1.simulator.BaseSyncOpSimulator;
import com.cnebrera.uc3.tech.lesson1.simulator.SyncOpSimulLongOperation;

/**
 * Second practice, measure latency with and without warmup
 */
public class PracticeLatency2
{
    /**
     * Main method to run the practice
     * @param args command line arument
     */
    public static void main(String [] args)
    {
        runCalculations();
    }

    /**
     * Run the practice calculations
     */
    private static void runCalculations()
    {
        // Create a random park time simulator
        BaseSyncOpSimulator syncOpSimulator = new SyncOpSimulLongOperation();

        // Execute the operation lot of times
        for(int i = 0; i < 200000; i++)
        {
            syncOpSimulator.executeOp();
        }

        // TODO Show min, max, mean and percentiles 99 and 99,9 with and without warm up
    }
}
