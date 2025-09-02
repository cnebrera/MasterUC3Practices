package com.cnebrera.uc3.tech.lesson1;

import com.cnebrera.uc3.tech.lesson1.simulator.BaseSyncOpSimulator;
import com.cnebrera.uc3.tech.lesson1.simulator.SyncOpSimulSleep;
import org.HdrHistogram.ConcurrentHistogram;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Second practice, measure accumulated latency with multiple threads
 */
public class PracticeLatency2
{
    /** Number of consumer threads to run */
    static final int NUM_THREADS = 4;
    /** Number of executions per thread */
    static final int NUM_EXECUTIONS = 100;
    /** Expected max executions per second */
    static final int MAX_EXPECTED_EXECUTIONS_PER_SECOND = 500;

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
        // TODO Create two different empty concurrent histograms

        // Create a sleep time simulator, it will sleep for 10 milliseconds in each call
        BaseSyncOpSimulator syncOpSimulator = new SyncOpSimulSleep(10);

        // List of threads
        List<Runner> runners = new LinkedList<>();

        // Create the threads and start them
        for (int i = 0; i < NUM_THREADS; i ++)
        {
            // TODO Pass the proper histograms
            final Runner runner = new Runner(syncOpSimulator, null, null);
            runners.add(runner);
            new Thread(runner).start();
        }

        // Wait for runners to finish
        runners.forEach(Runner::waitToFinish);

        // TODO Show the percentile distribution of both histograms
    }

    /**
     * The runner that represent a thread execution
     */
    private static class Runner implements Runnable
    {
        /** The shared operation simulator */
        final BaseSyncOpSimulator syncOpSimulator;

        /** Latency histogram*/
        private final ConcurrentHistogram histogram;

        /** Accumulated latency histogram*/
        private final ConcurrentHistogram histogramAcc;

        /** True if finished */
        volatile boolean finished = false;

        /**
         * Create a new runner
         *
         * @param syncOpSimulator shared operation simulator
         * @param histogram latency histogram
         * @param histogramAcc accumulated latency histogram
         */
        private Runner(BaseSyncOpSimulator syncOpSimulator, ConcurrentHistogram histogram, ConcurrentHistogram histogramAcc)
        {
            this.syncOpSimulator = syncOpSimulator;
            this.histogram = histogram;
            this.histogramAcc = histogramAcc;
        }

        @Override
        public void run()
        {
            // Calculate the expected time between consecutive calls, considering the number of executions per second
            final long expectedTimeBetweenCalls = TimeUnit.SECONDS.toMillis(1) / MAX_EXPECTED_EXECUTIONS_PER_SECOND;

            // Calculate the next call time, the first time should be immediate
            long nextCallTime = System.currentTimeMillis();

            // Execute the operation the required number of times
            for(int i = 0; i < NUM_EXECUTIONS; i++)
            {
                // Wait until the next call must be sent
                while(System.currentTimeMillis() < nextCallTime);

                long startTime = System.currentTimeMillis();
                // Execute the operation, it will sleep for 10 milliseconds
                syncOpSimulator.executeOp();

                // TODO Compute latency for each operation in milliseconds

                // TODO Compute delay for each operation in milliseconds

                // TODO Record latency into histogram

                // TODO Compute accumulated latency and record it

                // Calculate the next time to call execute op
                nextCallTime += expectedTimeBetweenCalls;
            }

            finished = true;
        }

        /** Wait for the runner execution to complete */
        public void waitToFinish()
        {
            while(!this.finished)
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
