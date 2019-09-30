package com.obaccelerator.common.id;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public class IdGenExperimentalV2Test {

    List<Long> resultList = new ArrayList<>();
    Set<Long> resultSet = new ConcurrentSkipListSet<>();

    private static final int IDS_PER_TASK = 10;
    private static final int NUMBER_OF_TASKS = 10;

    @Test
    public void testIdGen() throws InterruptedException, ExecutionException {
        log.info("Generating " + (IDS_PER_TASK * NUMBER_OF_TASKS) + " ids");

        ExecutorService executorService = Executors.newFixedThreadPool(10);
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

    private class GenManyIds implements Callable<List<Long>> {

        private List<Long> result = new ArrayList<>();

        @Override
        public List<Long> call() throws Exception {
            for (int i = 0; i < IDS_PER_TASK; i++) {
                // Thread.sleep(1);
                long id = IdGenExperimentalV2.nextId();
                // log.info("" + id);
                result.add(id);
            }
            return result;
        }
    }

}