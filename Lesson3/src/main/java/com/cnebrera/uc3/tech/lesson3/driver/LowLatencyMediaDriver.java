package com.cnebrera.uc3.tech.lesson3.driver;

import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.SigIntBarrier;

/**
 * Media driver  prepared to be launched in stand alone mode. </br>
 *
 * Configured for low latency communications using an internal dedicated threading model and BusySpin idle strategy for internal threads
 */
public class LowLatencyMediaDriver
{
    /**
     * Launch the driver
     * @param args driver argument property files
     */
    public static void main(final String[] args)
    {
        MediaDriver.loadPropertiesFiles(args);

        final MediaDriver.Context ctx = new MediaDriver.Context()
                .threadingMode(ThreadingMode.DEDICATED)
                .conductorIdleStrategy(new BackoffIdleStrategy(1, 1, 1, 1))
                .receiverIdleStrategy(new BusySpinIdleStrategy())
                .senderIdleStrategy(new BusySpinIdleStrategy());

        try (final MediaDriver ignored = MediaDriver.launch(ctx))
        {
            System.out.println("Driver started...");

            new SigIntBarrier().await();
        }

        System.out.println("Driver stopped...");
    }
}
