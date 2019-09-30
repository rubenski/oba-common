package com.obaccelerator.common.id;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Produces a unique long id.
 *
 * NOTE: do not change this class without careful consideration and testing.
 * It should be able to generate 100 million unique IDs in about 1.5 minutes.
 *
 * Note that this class relies on performance of the hardware and the MAX_COUNTER to provide unique IDs.
 * It is an inherently dangerous strategy. Set MAX_XOUNTER to 99 and run a big load (unit) test on the class
 * using a ExecutorService and 1000 threads to see what I mean.
 *
 */
@Slf4j
public final class IdGenExperimental {

    private static final int MAX_COUNTER = 999;
    private static long MILLIS_SINCE_2019 = 1546300800000L;
    /**
     * AtomicInteger provides direct visibility of the underlying (volatile) int value to all threads
     * and it ensures atomicity of the operations if offers.
     */
    private static AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * This method is synchronized, because it turns out to be the only way to make it truly thread safe.
     * The use of AtomicInteger as the counter is not enough, probably because we do multiple operations in getCounter()
     *
     * @return
     */
    public synchronized static long nextId() {
        long currentEpoch = currentEpoch();
        int counter = getCounter();
        if(counter > MAX_COUNTER) {
            throw new RuntimeException("Counter is greater than " + MAX_COUNTER + ". Its value is " + COUNTER);
        }
        try {
            return Long.parseLong("" + currentEpoch + "" + counter);
        } catch (NumberFormatException e) {
            throw new RuntimeException("epoch: " + currentEpoch + " counter: " + counter, e);
        }
    }

    private static int getCounter() {
        COUNTER.compareAndSet(MAX_COUNTER, 0);
        return COUNTER.getAndIncrement();
    }

    private static long currentEpoch() {
        return System.currentTimeMillis() - MILLIS_SINCE_2019;
    }
}