package com.cnebrera.uc3.tech.lesson1.simulator;

/**
 * Simulates an operation that require the given sleep time in milliseconds
 */
public class SyncOpSimulSleep extends BaseSyncOpSimulator
{
    final long sleepTime;

    public SyncOpSimulSleep(long sleepTime)
    {
        this.sleepTime = sleepTime;
    }

    public synchronized void executeOp()
    {
        try
        {
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
