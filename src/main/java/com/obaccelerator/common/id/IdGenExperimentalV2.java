package com.obaccelerator.common.id;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Produces a unique long id.
 */
@Slf4j
public final class IdGenExperimentalV2 {

    private static final int MAX_COUNTER = 99;
    private static long MILLIS_SINCE_2019 = 1546300800000L;
    /**
     * AtomicInteger provides direct visibility of the underlying (volatile) int value to all threads
     * and it ensures atomicity of the operations if offers.
     */
    private static AtomicInteger COUNTER = new AtomicInteger(0);
    private static AtomicLong PREVIOUS_EPOCH = new AtomicLong(0);

    public synchronized static long nextId() {
        long currentEpoch = currentEpoch();
        long previousEpoch = PREVIOUS_EPOCH.get();

        if (currentEpoch < previousEpoch) {
            throw new RuntimeException("The clock is going backwards! Current epoch: "
                    + currentEpoch + ". Previous epoch: " + previousEpoch);
        }

        if (currentEpoch == previousEpoch && COUNTER.get() == MAX_COUNTER) {
            currentEpoch = waitForNextEpochMs(currentEpoch);
        }

        PREVIOUS_EPOCH.set(currentEpoch);

        int counter = getCounter();
        if (counter > MAX_COUNTER) {
            throw new RuntimeException("Counter is greater than " + MAX_COUNTER + ". Its value is " + COUNTER);
        }
        try {
            return Long.parseLong("" + currentEpoch + "" + String.format("%03d", counter));
        } catch (NumberFormatException e) {
            throw new RuntimeException("epoch: " + currentEpoch + " counter: " + counter, e);
        }
    }

    private static long waitForNextEpochMs(long currentEpoch) {
        while (currentEpoch == currentEpoch()) {
        }
        return currentEpoch();
    }

    private static int getCounter() {
        COUNTER.compareAndSet(MAX_COUNTER, 0);
        return COUNTER.getAndIncrement();
    }

    private static long currentEpoch() {
        return System.currentTimeMillis() - MILLIS_SINCE_2019;
    }
}