package edu.kit.iai.gleam.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes information to all islands when configuration is available
 */
public class ConfigurationAvailablePublisher {
    @Autowired
    @Qualifier("configurationAvailableTopic")
    ChannelTopic configurationAvailableTopic;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> template;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void publish(String message) {
        logger.info("publishing configuration available");
        template.convertAndSend(configurationAvailableTopic.getTopic(), message);
    }
}