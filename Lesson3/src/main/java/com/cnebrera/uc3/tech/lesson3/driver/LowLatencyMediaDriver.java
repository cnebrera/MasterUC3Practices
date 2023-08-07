package com.cnebrera.uc3.tech.lesson3.driver;

import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.NoOpIdleStrategy;
import org.agrona.concurrent.ShutdownSignalBarrier;

import static org.agrona.SystemUtil.loadPropertiesFiles;

/**
 * Sample setup for a Media Driver that is configured for low latency communications. This configuration
 * requires sufficient CPU resource to delivery low latency performance, i.e. 3 active polling threads.
 */
public class LowLatencyMediaDriver
{
    /**
     * Main method for launching the process.
     *
     * @param args passed to the process which will be used for loading properties files.
     */
    @SuppressWarnings("try")
    public static void main(final String[] args)
    {
        loadPropertiesFiles(args);

        final MediaDriver.Context ctx = new MediaDriver.Context()
                .termBufferSparseFile(false)
                .useWindowsHighResTimer(true)
                .threadingMode(ThreadingMode.DEDICATED)
                .conductorIdleStrategy(BusySpinIdleStrategy.INSTANCE)
                .receiverIdleStrategy(NoOpIdleStrategy.INSTANCE)
                .senderIdleStrategy(NoOpIdleStrategy.INSTANCE);

        try (MediaDriver ignored = MediaDriver.launch(ctx))
        {
            System.out.println("Driver started...");

            new ShutdownSignalBarrier().await();
        }

        System.out.println("Shutdown Driver...");
    }
}
