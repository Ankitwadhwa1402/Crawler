package com.example.crawler.service;

import java.util.concurrent.*;

public class CrawlerExecutor {


    private final ExecutorService executorService;

    public CrawlerExecutor(int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Executor tasks timed out. Forcing shutdown...");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted during shutdown.");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

