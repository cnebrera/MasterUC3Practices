package com.cnebrera.uc3.tech.lesson1;

import com.cnebrera.uc3.tech.lesson1.simulator.BaseSyncOpSimulator;
import com.cnebrera.uc3.tech.lesson1.simulator.SyncOpSimulRndPark;
import org.HdrHistogram.Histogram;
import org.HdrHistogram.HistogramLogProcessor;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

/**
 * First practice, measure latency on a simple operation
 */
public class PracticeLatency1
{
    private static long LOWEST  = TimeUnit.NANOSECONDS.toNanos(120);   /* Minimum registered value */
    private static long HIGHEST = TimeUnit.MICROSECONDS.toNanos(4200); /* Maximum registered value */
    private static int SIGNIF   = 2;      /* Significance, 2 will allow to have pretty accurate results */
    private static double SCALE = 1000d;  /* Scale from ns to ms*/

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
        BaseSyncOpSimulator syncOpSimulator = new SyncOpSimulRndPark(TimeUnit.NANOSECONDS.toNanos(100), TimeUnit.MICROSECONDS.toNanos(100));
        // Timestamp
        long start, tot;

        // Set it adjustable, for resizing the rank
        hg.setAutoResize(true);

        tot = System.currentTimeMillis();
        // Execute the operation lot of times
        for(int i = 0; i < 100000; i++)
        {
            start = System.nanoTime();
            syncOpSimulator.executeOp();
            hg.recordValue(System.nanoTime() - start);
        }

        // Total time
        tot = System.currentTimeMillis() - tot;

        // Show the percentile distribution of the latency calculation of each executeOp call
        hg.outputPercentileDistribution(System.out, SCALE);


        System.out.println("#[Min     =        " + (hg.getMinValue()/SCALE) +
                    ", Time           =        " + (tot/SCALE) + "]");
    }
}

