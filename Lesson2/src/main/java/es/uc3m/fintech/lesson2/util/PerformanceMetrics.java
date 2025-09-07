package es.uc3m.fintech.lesson2.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for collecting and reporting performance metrics
 * in low-latency messaging scenarios. Tracks message count, latency
 * statistics, and provides summary reporting including percentiles
 * and throughput.
 * 
 * @author Mario Cao
 * @version 1.0
 */
public class PerformanceMetrics {
    private final String protocolName;
    private final AtomicLong messageCount = new AtomicLong(0);
    private final AtomicLong totalLatency = new AtomicLong(0);
    private final List<Long> latencies = new ArrayList<>();
    private final long startTime;

    public PerformanceMetrics(String protocolName) {
        this.protocolName = protocolName;
        this.startTime = System.nanoTime();
    }

    /** Record a message with its latency */
    public void recordMessage(long latencyNanos) {
        messageCount.incrementAndGet();
        totalLatency.addAndGet(latencyNanos);
        latencies.add(latencyNanos);
    }

    /** Calculate and print performance summary */
    public void printSummary() {
        long count = messageCount.get();
        long totalLat = totalLatency.get();
        long elapsed = System.nanoTime() - startTime;

        if (count == 0) {
            System.out.printf("[%s] No messages received%n", protocolName);
            return;
        }

        double avgLatency = (double) totalLat / count / 1000.0; // Convert to microseconds
        double throughput = (count * 1_000_000_000.0) / elapsed; // messages per second

        // Calculate percentiles
        latencies.sort(Long::compareTo);
        long p50 = latencies.get((int) (count * 0.5)) / 1000; // Convert to microseconds
        long p95 = latencies.get((int) (count * 0.95)) / 1000;
        long p99 = latencies.get((int) (count * 0.99)) / 1000;
        long min = latencies.get(0) / 1000;
        long max = latencies.get(latencies.size() - 1) / 1000;

        System.out.printf("%n=== %s Performance Summary ===%n", protocolName);
        System.out.printf("Messages: %d%n", count);
        System.out.printf("Throughput: %.2f msg/sec%n", throughput);
        System.out.printf("Latency (microseconds):%n");
        System.out.printf("  Average: %.2f%n", avgLatency);
        System.out.printf("  Min: %d%n", min);
        System.out.printf("  P50: %d%n", p50);
        System.out.printf("  P95: %d%n", p95);
        System.out.printf("  P99: %d%n", p99);
        System.out.printf("  Max: %d%n", max);
        System.out.printf("Total time: %.3f seconds%n", elapsed / 1_000_000_000.0);
        System.out.println();
    }

    /** Get current message count */
    public long getMessageCount() {
        return messageCount.get();
    }

    /** Get current throughput (messages per second) */
    public double getCurrentThroughput() {
        long elapsed = System.nanoTime() - startTime;
        if (elapsed == 0)
            return 0.0;
        return (messageCount.get() * 1_000_000_000.0) / elapsed;
    }

    /** Reset metrics for a new test run */
    public void reset() {
        messageCount.set(0);
        totalLatency.set(0);
        latencies.clear();
    }
}
