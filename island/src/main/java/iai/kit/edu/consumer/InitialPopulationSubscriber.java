package iai.kit.edu.consumer;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.controller.IslandReadinessController;
import iai.kit.edu.core.Population;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Receives the initial population
 */
public class InitialPopulationSubscriber implements MessageListener {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    IslandConfig islandConfig;

    @Autowired
    IslandReadinessController islandReadinessController;

    @Autowired
    Population population;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {

        logger.debug("received initial population");
        ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
       // System.out.println(ConstantStrings.initialPopulation + "ConstantStrings.initialPopulation");
       // System.out.println(islandConfig.getIslandNumber() + "islandConfig.getIslandNumber()");
        // System.out.println(ConstantStrings.initialPopulation + "." + islandConfig.getIslandNumber() + "ConstantStrings.initialPopulation + islandConfig.getIslandNumber()");

        String initialPopulationJson = ops.get(ConstantStrings.initialPopulation + "." + islandConfig.getIslandNumber());
        population.readFromJSON(initialPopulationJson);

      //  System.out.println("initialPopulationJson  message" + initialPopulationJson);
       // System.out.println("initialPopulationJson size " + population.size());
    //    System.out.println("initialPopulationJson first one " + population.get(1).getRating());


        islandReadinessController.sendReadinessStatus(ConstantStrings.populationReady);
    }
}
