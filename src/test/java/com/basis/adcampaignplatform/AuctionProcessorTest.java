package com.basis.adcampaignplatform;

import com.basis.adcampaignplatform.concurrent.AuctionProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class AuctionProcessorTest {

    @Test
    @DisplayName("Should process all auctions using multiple threads")
    void shouldProcessAllAuctions() throws InterruptedException {
        AuctionProcessor processor = new AuctionProcessor(5);

        int totalAuctions = 20;
        for (int i = 1; i <= totalAuctions; i++) {
            processor.processAuction("AUCTION-" + i);
        }

        processor.shutdown();

        assertEquals(totalAuctions, processor.getProcessedCount(),
                "All auctions should be processed");
    }

    @Test
    @DisplayName("Should process auctions faster with more threads")
    void shouldBeFasterWithMoreThreads() throws InterruptedException {
        int totalAuctions = 20;

        AuctionProcessor singleThread = new AuctionProcessor(1);
        long startSingle = System.currentTimeMillis();
        for (int i = 1; i <= totalAuctions; i++) {
            singleThread.processAuction("AUCTION-" + i);
        }
        singleThread.shutdown();
        long singleThreadTime = System.currentTimeMillis() - startSingle;

        AuctionProcessor multiThread = new AuctionProcessor(10);
        long startMulti = System.currentTimeMillis();
        for (int i = 1; i <= totalAuctions; i++) {
            multiThread.processAuction("AUCTION-" + i);
        }
        multiThread.shutdown();
        long multiThreadTime = System.currentTimeMillis() - startMulti;

        assertEquals(totalAuctions, singleThread.getProcessedCount());
        assertEquals(totalAuctions, multiThread.getProcessedCount());

        assertTrue(multiThreadTime < singleThreadTime,
                "Multi-thread should be faster. Single: " +
                        singleThreadTime + "ms, Multi: " + multiThreadTime + "ms");

        System.out.println("Single thread time: " + singleThreadTime + "ms");
        System.out.println("Multi thread time: " + multiThreadTime + "ms");
    }

    @Test
    @DisplayName("Should handle thread safety with AtomicInteger")
    void shouldHandleThreadSafely() throws InterruptedException {
        AuctionProcessor processor = new AuctionProcessor(10);

        for (int i = 1; i <= 100; i++) {
            processor.processAuction("AUCTION-" + i);
        }

        processor.shutdown();

        assertEquals(100, processor.getProcessedCount(),
                "AtomicInteger should count all 100 auctions correctly");
    }
}