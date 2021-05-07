package com.example.hibernatetest.jpa.runnable;

import com.example.hibernatetest.jpa.service.HumanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

public class AsyncCaller implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AsyncCaller.class);

    private final Semaphore semaphore;
    private final int threadNumber;
    private final HumanService service;

    public AsyncCaller(Semaphore semaphore, int threadNumber, HumanService service) {
        this.semaphore = semaphore;
        this.threadNumber = threadNumber;
        this.service = service;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            service.increaseAge(this.threadNumber, 1L);
        } catch (InterruptedException e) {
            log.warn("Interrupted", e);
        }
    }
}
