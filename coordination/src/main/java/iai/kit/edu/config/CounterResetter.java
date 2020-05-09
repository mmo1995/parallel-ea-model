package iai.kit.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Component;

/**
 * Resets all counters in Redis
 */
public class CounterResetter {

    @Autowired
    @Qualifier("integerTemplate")
    private RedisTemplate<String,Integer> template;

    public void resetCounters() {
        RedisAtomicInteger counter;
        counter = new RedisAtomicInteger(ConstantStrings.islandMigrantsReceivedCounter, template.getConnectionFactory(),0);
        counter = new RedisAtomicInteger(ConstantStrings.initializedIslandCounter, template.getConnectionFactory(),0);
        counter = new RedisAtomicInteger(ConstantStrings.islandsWithPopulationCounter, template.getConnectionFactory(),0);
        counter = new RedisAtomicInteger(ConstantStrings.islandsWithSubscribedNeighborsCounter, template.getConnectionFactory(),0);
        counter = new RedisAtomicInteger(ConstantStrings.receivedResultsCounter, template.getConnectionFactory(), 0);
        counter = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, template.getConnectionFactory(), 0);
        counter = new RedisAtomicInteger(ConstantStrings.numberOfSlavesTopic, template.getConnectionFactory(), 0);
        counter = new RedisAtomicInteger(ConstantStrings.numberOfIslands, template.getConnectionFactory(), 0);
        counter = new RedisAtomicInteger(ConstantStrings.numberOfGenerationForOneIsland, template.getConnectionFactory(), 0);
        counter = new RedisAtomicInteger(ConstantStrings.receivedSlavesResultsCounter, template.getConnectionFactory(), 0);
        counter = new RedisAtomicInteger(ConstantStrings.islandsWithReadySlavesCounter, template.getConnectionFactory(), 0);
    }
}
