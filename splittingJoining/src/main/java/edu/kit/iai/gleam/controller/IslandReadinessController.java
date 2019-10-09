package edu.kit.iai.gleam.controller;


import edu.kit.iai.gleam.config.ConstantStrings;
import edu.kit.iai.gleam.producer.StartPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.web.bind.annotation.*;

/**
 * Receives signals from all islands if they have received the full configuration (migration, neighbor and algo config)
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sjs")
public class IslandReadinessController {

    @Autowired
    ResultController resultController;

    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> template;

    @Autowired
    StartPublisher startPublisher;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/island_status/ready/", method = RequestMethod.POST)
    public void receiveIslandStatus(@RequestBody String islandStatusMessage) {

        updateCounters(islandStatusMessage);

    }

    /**
     * If all islands are ready, the start signal to start the evolution is sent to all islands.
     * @param islandStatusMessage
     */
    private synchronized void updateCounters(String islandStatusMessage) {
        RedisAtomicInteger islandsWithPopulationCounter = new RedisAtomicInteger(ConstantStrings.islandsWithPopulationCounter, template.getConnectionFactory());
        RedisAtomicInteger islandsWithSubscribedNeighborsCounter = new RedisAtomicInteger(ConstantStrings.islandsWithSubscribedNeighborsCounter, template.getConnectionFactory());
        if (islandStatusMessage.equals(ConstantStrings.populationReady)) {
            islandsWithPopulationCounter.incrementAndGet();
        } else if (islandStatusMessage.equals(ConstantStrings.neighborsSubscribed)) {
            islandsWithSubscribedNeighborsCounter.incrementAndGet();
        }
        RedisAtomicInteger numberOfIslandsCounter = new RedisAtomicInteger(ConstantStrings.numberOfIslands, template.getConnectionFactory());
        int numberOfIslands = numberOfIslandsCounter.get();
        if (islandsWithPopulationCounter.get() == numberOfIslands && islandsWithSubscribedNeighborsCounter.get() == numberOfIslands) {
            resultController.reset();
            logger.info("starting islands");
            startPublisher.publish("start evolution");
        }
    }
}
