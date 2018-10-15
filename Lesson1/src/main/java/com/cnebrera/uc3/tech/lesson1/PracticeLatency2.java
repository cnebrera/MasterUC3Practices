package com.cnebrera.uc3.tech.lesson1;

import com.cnebrera.uc3.tech.lesson1.simulator.BaseSyncOpSimulator;
import com.cnebrera.uc3.tech.lesson1.simulator.SyncOpSimulLongOperation;
import org.HdrHistogram.Histogram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Second practice, measure latency with and without warmup
 */
public class PracticeLatency2
{

    private final static long LOWEST  = 1000;    /* Minimum registered value */
    private final static long HIGHEST = 1300000; /* Maximum registered value */
    private final static int  SIGNIF  = 5;       /* Significance, 5 will allow to have pretty accurate results */
    private final static int  REPS    = 50;      /* Repetitions */

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
        // Create a histogram object and give the asked parameters
        Histogram hg = new Histogram(LOWEST, HIGHEST, SIGNIF);
        // Create a random park time simulator
        BaseSyncOpSimulator syncOpSimulator = new SyncOpSimulLongOperation();
        // Timestamp
        long start;

        // Set it adjustable, for resizing the rank
        hg.setAutoResize(true);

        // Warm up
        for (int nb = 1; nb <= REPS; nb++) {
            // Execute the operation lot of times
            for (int i = 0; i < 200000; i++) {
                start = System.nanoTime();
                syncOpSimulator.executeOp();
                hg.recordValue(System.nanoTime() - start);
            }

            // Result printing
            System.out.print("nº = " + nb + "\t| ");
            System.out.print("Min = " + hg.getMinValue() + "\t| ");
            System.out.print("Max = " + hg.getMaxValue() + "\t| ");
            System.out.print("Mean = " + hg.getMean() + "\t| ");
            System.out.print("99% = " + hg.getValueAtPercentile(99) + "\t| ");
            System.out.print("99.9% = " + hg.getValueAtPercentile(99.9) + "");
            System.out.println();

            // Restart the histogram
            hg.reset();
        }

        // TODO Show min, max, mean and percentiles 99 and 99,9 with and without warm up
    }
}
