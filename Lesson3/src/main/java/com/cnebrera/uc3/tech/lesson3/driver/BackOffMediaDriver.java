package com.cnebrera.uc3.tech.lesson3.driver;

import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.ShutdownSignalBarrier;

import static org.agrona.SystemUtil.loadPropertiesFiles;

/**
 * Media driver  prepared to be launched in stand alone mode. </br>
 *
 * Configured to reduce the usage of CPU. Not optimized for Low Latency.
 */
public class BackOffMediaDriver
{
    /**
     * Launch the driver
     * @param args driver argument property files
     */
    public static void main(final String[] args)
    {
        loadPropertiesFiles(args);

        final MediaDriver.Context ctx = new MediaDriver.Context()
                .threadingMode(ThreadingMode.SHARED)
                .sharedIdleStrategy(new BackoffIdleStrategy(1, 1, 1, 1));

        try (final MediaDriver ignored = MediaDriver.launch(ctx))
        {
            System.out.println("Driver started...");

            new ShutdownSignalBarrier().await();
        }

        System.out.println("Driver stopped...");
    }
}
