package edu.kit.iai.gleam.producer;

import edu.kit.iai.gleam.config.ConstantStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes information to all Slaves that initial population is available
 */
public class SlavesPopulationPublisher {

    @Autowired
    @Qualifier("initialPopulationTopic")
    ChannelTopic initialPopulationTopic;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> template;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void publish(String message, String islandNumber,int slaveNumber) {
        // logger.info ("publishing in " + initialPopulationTopic.getTopic() + "." + channelID + message );
        template.convertAndSend(ConstantStrings.slavePopulation + "." +  islandNumber + "." + slaveNumber, message);
    }
}
