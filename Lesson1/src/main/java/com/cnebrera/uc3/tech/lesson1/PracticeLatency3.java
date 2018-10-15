package com.cnebrera.uc3.tech.lesson1;

import com.cnebrera.uc3.tech.lesson1.simulator.BaseSyncOpSimulator;
import com.cnebrera.uc3.tech.lesson1.simulator.SyncOpSimulSleep;
import org.HdrHistogram.ConcurrentHistogram;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Third practice, measure accumulated latency with multiple threads
 */
public class PracticeLatency3 {

    private final static long LOWEST = 1;       /* Minimum registered value */
    private final static long HIGHEST = 100;    /* Maximum registered value */
    private final static int SIGNIF = 5;           /* Significance, 5 will allow to have pretty accurate results */
    private final static int NUM_THREADS = 4;      /* Number of consumer threads to run */
    private final static int NUM_EXECUTIONS = 100; /* Number of executions per thread */
    private final static int MAX_EXPECTED_EXECUTIONS_PER_SECOND = 50; /* Expected max executions per second */

    /**
     * Main method to run the practice
     *
     * @param args command line arument
     */
    public static void main(String[] args) {
        runCalculations();
    }

    /**
     * Run the practice calculations
     */
    private static void runCalculations() {
        // Create a histogram object and give the asked parameters
        ConcurrentHistogram hg = new ConcurrentHistogram(LOWEST, HIGHEST, SIGNIF),
                hgCumul = new ConcurrentHistogram(LOWEST, HIGHEST, SIGNIF);
        // Create a sleep time simulator, it will sleep for 10 milliseconds in each call
        BaseSyncOpSimulator syncOpSimulator = new SyncOpSimulSleep(10);
        // For better comparatives
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        String[] latRes, cumLatRes;
        int size;

        // List of threads
        List<Runner> runners = new LinkedList<>();

        // Set autorresizable histograms
        hg.setAutoResize(true);
        hgCumul.setAutoResize(true);

        // Create the threads and start them
        for (int i = 0; i < NUM_THREADS; i++) {
            final Runner runner = new Runner(syncOpSimulator, hg, hgCumul);
            runners.add(runner);
            new Thread(runner).start();
        }

        // Wait for runners to finish
        runners.forEach(Runner::waitToFinish);

        // Get the outputs into a string array
        hg.outputPercentileDistribution(ps, 1.0);
        ps.flush();
        latRes = baos.toString().split("[\n]");
        
        baos.reset();

        hgCumul.outputPercentileDistribution(ps, 1.0);
        ps.flush();
        cumLatRes = baos.toString().split("[\n]");
        
        // Print them paired for a easier comparison 
        size = Arrays.stream(latRes).max(Comparator.comparingInt(String::length)).get().length();
        IntStream.range(0, Math.min(latRes.length, cumLatRes.length))
                .forEach(i -> System.out.printf("%-" + size + "." + size + "s   |   %-" + size + "." + size + "s\n", latRes[i], cumLatRes[i]));
    }

    /**
     * The runner that represent a thread execution
     */
    private static class Runner implements Runnable {
        /* The shared operation simulator */
        final BaseSyncOpSimulator syncOpSimulator;
        private final ConcurrentHistogram hg, hgCumul;

        /* True if finished */
        volatile boolean finished = false;

        /**
         * Create a new runner
         *
         * @param syncOpSimulator shared operation simulator
         */
        private Runner(BaseSyncOpSimulator syncOpSimulator, ConcurrentHistogram hg, ConcurrentHistogram hgCumul) {
            this.syncOpSimulator = syncOpSimulator;
            this.hg = hg;
            this.hgCumul = hgCumul;
        }

        @Override
        public void run() {
            // Calculate the expected time between consecutive calls, considering the number of executions per second
            final long expectedTimeBetweenCalls = TimeUnit.SECONDS.toMillis(1) / MAX_EXPECTED_EXECUTIONS_PER_SECOND;

            // Calculate the next call time, the first time should be immediate
            long nextCallTime = System.currentTimeMillis();
            long start, tot, extra = 0;

            // Execute the operation the required number of times
            for (int i = 0; i < NUM_EXECUTIONS; i++) {
                // Wait until there is the time for the next call
                while (System.currentTimeMillis() < nextCallTime);
                start = System.currentTimeMillis();

                // Execute the operation, it will sleep for 10 milliseconds
                syncOpSimulator.executeOp();

                tot = System.currentTimeMillis() - start;

                extra += tot - expectedTimeBetweenCalls;

                if (extra < 0) extra = 0;

                // Record the results
                hgCumul.recordValue(tot + extra);
                hg.recordValue(tot);

                // Calculate the next time to call execute op
                nextCallTime += expectedTimeBetweenCalls;
            }

            finished = true;
        }

        /**
         * Wait for the runner execution to complete
         */
        private void waitToFinish() {
            while (!this.finished) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
