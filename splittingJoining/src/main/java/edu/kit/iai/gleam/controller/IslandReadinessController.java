package edu.kit.iai.gleam.controller;


import edu.kit.iai.gleam.config.ConstantStrings;
import edu.kit.iai.gleam.producer.StartPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Receives signals from all islands if they have received the full configuration (migration, neighbor and algo config)
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sjs")
public class IslandReadinessController {
    private RestTemplate restTemplate = new RestTemplate();

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
        RedisAtomicInteger islandsWithReadySlavesCounter = new RedisAtomicInteger(ConstantStrings.islandsWithReadySlavesCounter, template.getConnectionFactory());
        if (islandStatusMessage.equals(ConstantStrings.populationReady)) {
            islandsWithPopulationCounter.incrementAndGet();
        } else if (islandStatusMessage.equals(ConstantStrings.neighborsSubscribed)) {
            islandsWithSubscribedNeighborsCounter.incrementAndGet();
        } else if (islandStatusMessage.equals(ConstantStrings.slavesReady)) {
            islandsWithReadySlavesCounter.incrementAndGet();
        }
        RedisAtomicInteger numberOfIslandsCounter = new RedisAtomicInteger(ConstantStrings.numberOfIslands, template.getConnectionFactory());
        int numberOfIslands = numberOfIslandsCounter.get();
        if (islandsWithPopulationCounter.get() == numberOfIslands && islandsWithSubscribedNeighborsCounter.get() == numberOfIslands && islandsWithReadySlavesCounter.get() == numberOfIslands) {
            resultController.reset();
            //logger.info("starting islands");
            startPublisher.publish("start evolution");
            ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.coordinationURL +"/ojm/slaves/ready/", "starting islands", String.class);
            islandsWithPopulationCounter.set(0);
            islandsWithSubscribedNeighborsCounter.set(0);
            islandsWithReadySlavesCounter.set(0);

        }
    }
}
