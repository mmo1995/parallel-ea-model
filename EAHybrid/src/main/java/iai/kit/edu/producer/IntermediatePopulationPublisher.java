package iai.kit.edu.producer;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.core.Population;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes the intermediate population to the Migration & Synchronization Service
 */
public class IntermediatePopulationPublisher {

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    @Value("${island.number}")
    private int islandNumber;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * EA Service instance x always communicates with Migration & Synchronization instance x, together they
     * are one island
     * @param intermediatePopulation
     */
    public void publishIntermediatePopulation(Population intermediatePopulation) {
        ChannelTopic intermediatePopulationTopic = new ChannelTopic(ConstantStrings.intermediatePopulation+"."+islandNumber);
        Gson gson = new Gson();
        String intermediatePopulationJson = gson.toJson(intermediatePopulation.getResult());
        logger.info("publishing Intermediate PopulationJson");
        stringTemplate.convertAndSend(intermediatePopulationTopic.getTopic(), intermediatePopulationJson);
    }
}
