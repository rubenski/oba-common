package com.obaccelerator.common.endpoint;

import com.obaccelerator.common.id.IdGenExperimental;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class UUIDGenTest {

    List<Long> resultList = new ArrayList<>();
    Set<Long> resultSet = new ConcurrentSkipListSet<>();

    private static final int IDS_PER_TASK = 10000;
    private static final int NUMBER_OF_TASKS = 500;

    @Test
    public void testParseLong() {
        long l = Long.parseLong("" + System.currentTimeMillis() + "" + 10);
        log.info("" + l);
    }

    @Test
    public void testIdGen() throws InterruptedException, ExecutionException {
        log.info("Generating " + (IDS_PER_TASK * NUMBER_OF_TASKS) + " ids");

        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        Collection<Callable<List<Long>>> jobs = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_TASKS; i++) {
            jobs.add(new GenManyIds());
        }
        List<Future<List<Long>>> futures = executorService.invokeAll(jobs);

        for (Future<List<Long>> future : futures) {
            List<Long> longs = future.get();
            resultList.addAll(longs);
            resultSet.addAll(longs);
        }

        log.info("number of ids: " + resultList.size());
        log.info("unique number of ids: " + resultSet.size());


        for (int i = 0; i < resultList.size(); i++) {
            if(i != 0) {
                long id = resultList.get(i);
                long prevID = resultList.get(i - 1);
                if(id == prevID) {
                    log.error("ID {} and prevId {} are the same", id, prevID);
                }
            }
        }
    }

    @Test
    public void testSimple() {
        for(int i = 0; i < IDS_PER_TASK; i++) {
            long id = IdGenExperimental.nextId();
        }
    }

    private class GenManyIds implements Callable<List<Long>> {

        private List<Long> result = new ArrayList<>();

        @Override
        public List<Long> call() throws Exception {
            for (int i = 0; i < IDS_PER_TASK; i++) {
                // Thread.sleep(1);
                long id = IdGenExperimental.nextId();
                result.add(id);
            }
            return result;
        }
    }
}


