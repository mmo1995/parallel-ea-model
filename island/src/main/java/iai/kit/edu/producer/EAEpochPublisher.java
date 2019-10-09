package iai.kit.edu.producer;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.EAEpochConfig;
import iai.kit.edu.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes configuration for one epoch to the EA Service
 */
public class EAEpochPublisher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IslandConfig islandConfig;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;


    public void publishEAEpochConfig(EAEpochConfig eaEpochConfig) {
        ChannelTopic topic = new ChannelTopic(ConstantStrings.epochTopic + "." + islandConfig.getIslandNumber());
        logger.info("publishing config");
        Gson gson = new Gson();
        String eaEpochConfigJson = gson.toJson(eaEpochConfig);
        stringTemplate.convertAndSend(topic.getTopic(), eaEpochConfigJson);
        //logger.info("publishing eaEpochConfigJson " + eaEpochConfigJson);
    }
}
