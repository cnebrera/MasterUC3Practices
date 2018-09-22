package com.cnebrera.uc3.tech.lesson1;

import com.cnebrera.uc3.tech.lesson1.simulator.BaseSyncOpSimulator;
import com.cnebrera.uc3.tech.lesson1.simulator.SyncOpSimulLongOperation;
import org.HdrHistogram.Histogram;

import java.util.concurrent.TimeUnit;

/**
 * Second practice, measure latency with and without warmup
 */
public class PracticeLatency2
{

    private static long LOWEST  = 1000;   /* Minimum registered value */
    private static long HIGHEST = 1300000;  /* Maximum registered value */
    private static int SIGNIF   = 5;      /* Significance, 2 will allow to have pretty accurate results */
    private static double SCALE = 1d;     /* Scale to print the results */

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
        long start, tot;

        // Warm up
        for (int nb = 1; nb <= 25; nb++) {
            System.out.println("----------------- Attempt nº" + nb + " -------------------------");
            // Execute the operation lot of times
            for(int i = 0; i < 200000; i++)
            {
                start = System.nanoTime();
                syncOpSimulator.executeOp();
                hg.recordValue(System.nanoTime() - start);
            }

            // Result printing
            System.out.println("- Min\t=\t" + hg.getMinValue());
            System.out.println("- Max\t=\t" + hg.getMaxValue());
            System.out.println("- Mean\t=\t" + hg.getMean());
            System.out.println("- 99%\t=\t" + hg.getValueAtPercentile(99));
            System.out.println("- 99.9%\t=\t" + hg.getValueAtPercentile(99.9));

            // Restart the histogram
            hg.reset();
        }

        // TODO Show min, max, mean and percentiles 99 and 99,9 with and without warm up
    }
}
