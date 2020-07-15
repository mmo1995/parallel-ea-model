package edu.kit.iai.calculation.producer;

import com.google.gson.Gson;
import edu.kit.iai.calculation.config.ConstantStrings;
import edu.kit.iai.calculation.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Publishes the intermediate population to the Migration & Synchronization Service
 */
public class IntermediatePopulationPublisher {

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    @Autowired
    IslandConfig islandConfig;

    private RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * EA Service instance x always communicates with Migration & Synchronization instance x, together they
     * are one island
     * @param intermediatePopulation
     */
    public void publishIntermediatePopulation(StringBuilder intermediatePopulation) {
        /*ChannelTopic intermediatePopulationTopic = new ChannelTopic(ConstantStrings.resultPopulation+"."+islandConfig.getIslandNumber());
        stringTemplate.convertAndSend(intermediatePopulationTopic.getTopic(), intermediatePopulation.toString());*/


        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
        ops.set(ConstantStrings.resultPopulation + "." + islandConfig.getIslandNumber() + "." + islandConfig.getSlaveNumber(), intermediatePopulation.toString());
        logger.info("********************************************************************************");
        int[] islandAndSlaveNumber = {islandConfig.getIslandNumber(), islandConfig.getSlaveNumber()};
        logger.info("********************************************************************************");
        ResponseEntity<String> request = restTemplate.postForEntity(ConstantStrings.splittingJoiningURL + "/sjs/slavesPopulation/result",islandAndSlaveNumber, String.class);
        //logger.info("publishing the calculated results of one generation");
        logger.info("********************************************************************************");
    }


}
