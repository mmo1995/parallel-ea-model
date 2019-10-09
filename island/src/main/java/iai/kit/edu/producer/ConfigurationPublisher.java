package iai.kit.edu.producer;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes the configuration to EA Service
 */
public class ConfigurationPublisher {
    @Autowired
    IslandConfig islandConfig;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void publishAlgorithmConfig(String algorithmConfig) {
        ChannelTopic topic = new ChannelTopic(ConstantStrings.EAConfig + "." + islandConfig.getIslandNumber());
        logger.info("publishing config");
        stringTemplate.convertAndSend(topic.getTopic(), algorithmConfig);
   }
}

