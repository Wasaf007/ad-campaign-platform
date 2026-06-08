package com.basis.adcampaignplatform.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AuctionProcessor {

    // AtomicInteger is thread-safe counter
    // Regular int would break when multiple threads update it simultaneously
    private final AtomicInteger processedAuctions = new AtomicInteger(0);

    private final ExecutorService executorService;

    public AuctionProcessor(int threadCount) {
        // Create a pool of threads, like hiring X cashiers
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    public void processAuction(String auctionId) {
        // Submit each auction to the thread pool
        // Thread pool decides which thread handles it
        executorService.submit(() -> {
            try {
                // Simulate auction processing time (10ms)
                Thread.sleep(10);
                processedAuctions.incrementAndGet();
                System.out.println("Auction " + auctionId +
                        " processed by thread: " +
                        Thread.currentThread().getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void shutdown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }

    public int getProcessedCount() {
        return processedAuctions.get();
    }
}