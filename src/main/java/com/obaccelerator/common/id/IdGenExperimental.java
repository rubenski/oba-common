package com.obaccelerator.common.id;

import lombok.extern.slf4j.Slf4j;

/**
 * Produces a
 */
@Slf4j
public class IdGenExperimental {

    static {
        MILLIS_SINCE_2019 = 1546300800000L;
    }

    private static long MILLIS_SINCE_2019;
    /**
     * AtomicInteger is used to guarantee immediate communication of the new COUNTER value
     * between threads. This is needed to prevent multiple threads pulling the same current epoch value
     * AND the same counter value. Apparently a normal static variable is not shared immediatey between threads,
     * resulting in threads getting the same COUNTER value, which results in duplicate ids.
     * <p>
     * Read here: https://stackoverflow.com/questions/4934913/are-static-variables-shared-between-threads
     * <p>
     * The COUNTER_MAX value of 9999 is large enough to prevent 20 threads generating 100K IDs concurrently from depleting
     * unique COUNTER values during the same epoch millisecond.
     * <p>
     * This class must be tested under a real world performance test
     */
    private static volatile int COUNTER = 0;


    public  static long nextId() {
        long currentEpoch = currentEpoch();
        int counter = getCounter();
        try {
            return Long.parseLong("" + currentEpoch + "" + counter);
        } catch (NumberFormatException e) {
            throw new RuntimeException("epoch: " + currentEpoch + " counter: " + counter);
        }
    }

    private synchronized static int getCounter() {
        if (COUNTER >= 9999) {
            COUNTER = 0;
        }
        return ++COUNTER;
    }

    private static long currentEpoch() {
        return System.currentTimeMillis() - MILLIS_SINCE_2019;
    }
}